package rest_test;

import org.springframework.web.bind.annotation.*;

import main.java.Exceptions.AlreadyExists;
import main.java.Exceptions.LoginException;
import main.java.Exceptions.NotExists;
import main.java.data.management.DBManager;
import main.java.data.members.Destination;
import main.java.data.members.MapLocation;
import main.java.data.members.ParkingArea;
import main.java.data.members.ParkingAreas;
import main.java.data.members.ParkingSlot;
import main.java.data.members.ParkingSlotStatus;
import main.java.data.members.User;
import main.java.logic.Navigation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {
	Map<String, MapLocation> hmap;
	ArrayList<ServerParkingSlot> parkingList;
	ServerParkingSlot sps;
	ParkingAreas areas;
	Set<ServerParkingArea> parkingAreas;

	public LocationController() {
		DBManager.initialize();
		areas = new ParkingAreas();
		setUpLocations();
		setUpParkingSpots();
	}

	void setUpLocations() {
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Destination");
		hmap = new HashMap<String, MapLocation>();
		try {
			final List<ParseObject> result = query.find();
			if (result == null)
				System.out.println("empty");
			for (final ParseObject dest : result) {
				final String destName = dest.getString("name");
				hmap.put(destName, new Destination(destName).getEntrance());
			}
		} catch (final Exception e) {
			System.out.println("exception...");
		}
	}

	void setUpParkingSpots() {
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("ParkingSlot");
		parkingList = new ArrayList<ServerParkingSlot>();
		try {
			final List<ParseObject> result = query.find();
			if (result == null)
				System.out.println("empty");
			for (final ParseObject park : result)
				parkingList.add(new ServerParkingSlot(new ParkingSlot(park)));
		} catch (final Exception e) {
			System.out.println("exception...");
		}
	}

	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/Locations", produces = "application/json")
	@ResponseBody
	public String getLocations() {
		setUpLocations();
		return new JSONObject(hmap) + "";
	}
	
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/Floors", produces = "application/json")
	@ResponseBody
	public String getFloors() {
		setUpLocations();
		Map<String, List<Map<String, String>>> floors = new HashMap<>();
		for (String key : hmap.keySet())
			if (!"Ulman".equals(key))
				floors.put(key, new ArrayList<>());
			else {
				List<Map<String, String>> ulman = new ArrayList<>();
				Map<String, String> floor1 = new HashMap<>();
				floor1.put("description", "take the stairs");
				floor1.put("id", "Floor 1");
				Map<String, String> floor2 = new HashMap<>();
				floor2.put("description", "take the elevator");
				floor2.put("id", "Floor 2");
				ulman.add(floor1);
				ulman.add(floor2);
				floors.put(key, ulman);
			}
		return new JSONObject(floors) + "";
	}

	@RequestMapping(value = "/Locations/{name}", produces = "application/json")
	@ResponseBody
	public MapLocation getLocation(@PathVariable String name) {
		setUpLocations();
		return hmap.get(name);
	}

	@RequestMapping(value = "/ParkingSlots", produces = "application/json")
	@ResponseBody
	public ArrayList<ServerParkingSlot> getParkingSlots() {
		setUpParkingSpots();
		return parkingList;
	}

	@RequestMapping(value = "/ParkingSlots/{name}", produces = "application/json")
	@ResponseBody
	public ServerParkingSlot getPark(@PathVariable String name) {
		for (ServerParkingSlot ps : parkingList)
			if (ps.getName().equals(name))
				return ps;
		return null;
	}

	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/ParkingAreas", produces = "application/json")
	@ResponseBody
	public Set<ServerParkingArea> getParkingAreas() {
		parkingAreas = new HashSet<ServerParkingArea>();
		for (ParkingArea a : areas.getParkingAreas())
			parkingAreas.add(new ServerParkingArea(a));
		return parkingAreas;
	}

	@RequestMapping(value = "/ParkingAreas/{name}", produces = "application/json")
	@ResponseBody
	public ServerParkingArea getArea(@PathVariable String name) {
		for (ServerParkingArea pa : parkingAreas)
			if (pa.getName().equals(name))
				return pa;
		return null;
	}
	
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/FindPark", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public MapLocation findBestPark(@RequestParam("car") String carNumber, @RequestParam("src") String src,
			@RequestParam("dest") String dest) {
		try {
			User u = new User(carNumber);
			sps = null;

			if (u.getCurrentParking() != null)
				throw new AlreadyExists("You have a parking slot");

			u.addToLastPaths(src, dest);

			sps = new ServerParkingSlot(
					Navigation.closestParkingSlot(u, new Destination(src).getEntrance(), areas, new Destination(dest)));
			return sps.getLocation();
		} catch (ParseException | LoginException | NotExists e) {
			System.out.println("exception...");
		} catch (AlreadyExists e) {
			System.out.println((e + ""));
		}
		return null;
	}

	@RequestMapping(value = "/FindPark", produces = "application/json")
	@ResponseBody
	public ServerParkingSlot getBestPark() {
		return sps != null ? sps : null;
	}

	@RequestMapping(value = "/LeavePark", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void leavePark(@RequestParam("car") String carNumber) {
		try {
			User u = new User(carNumber);
			u.getCurrentParking().changeStatus(ParkingSlotStatus.FREE);
			u.setCurrentParking(null);
			sps = null;

		} catch (LoginException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
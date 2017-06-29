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
	// A map from the destination name to its coordinates
	Map<String, MapLocation> hmap;

	// An arrayList of all of the Parking slots in the program
	ArrayList<ServerParkingSlot> parkingList;

	// TODO - change to map of Identifier,ServerParkingSlot
	ServerParkingSlot sps;

	// A ParkingAreas object
	ParkingAreas areas;

	// An object the include all of the areas at the database
	Set<ServerParkingArea> parkingAreas;

	/**
	 * Initialize the fields above
	 */
	public LocationController() {
		DBManager.initialize();
		areas = new ParkingAreas();
		setUpLocations();
		setUpParkingSpots();
	}

	/**
	 * Initialize the hmap to have all of the coordinates
	 */
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

	/**
	 * Initialize the parkingList to have all of the parking slots
	 */
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

	/**
	 * @return a string of JSONObject for all of the Locations names and
	 *         coordinates
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/Locations", produces = "application/json")
	@ResponseBody
	public String getLocations() {
		setUpLocations();
		return new JSONObject(hmap) + "";
	}

	// TODO - update the service to work with database
	/**
	 * @return for each destination its floors with the navigation description
	 */
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

	/**
	 * Getting information of a specific Destination according to name
	 * 
	 * @param name
	 *            the location name
	 * @return the coordinates of a destination
	 */
	@RequestMapping(value = "/Locations/{name}", produces = "application/json")
	@ResponseBody
	public MapLocation getLocation(@PathVariable String name) {
		setUpLocations();
		return hmap.get(name);
	}

	/**
	 * @return all of the parking slots
	 */
	@RequestMapping(value = "/ParkingSlots", produces = "application/json")
	@ResponseBody
	public ArrayList<ServerParkingSlot> getParkingSlots() {
		setUpParkingSpots();
		return parkingList;
	}

	/**
	 * Getting information of a specific parking slot according to name
	 * 
	 * @param name
	 *            the parking slot name
	 * @return the slots details
	 */
	@RequestMapping(value = "/ParkingSlots/{name}", produces = "application/json")
	@ResponseBody
	public ServerParkingSlot getPark(@PathVariable String name) {
		for (ServerParkingSlot ps : parkingList)
			if (ps.getName().equals(name))
				return ps;
		return null;
	}

	/**
	 * @return all of the parking areas
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/ParkingAreas", produces = "application/json")
	@ResponseBody
	public Set<ServerParkingArea> getParkingAreas() {
		parkingAreas = new HashSet<ServerParkingArea>();
		for (ParkingArea a : areas.getParkingAreas())
			parkingAreas.add(new ServerParkingArea(a));
		return parkingAreas;
	}

	/**
	 * Getting information of a specific parking area according to name
	 * 
	 * @param name
	 *            the parking area name
	 * @return the area details
	 */
	@RequestMapping(value = "/ParkingAreas/{name}", produces = "application/json")
	@ResponseBody
	public ServerParkingArea getArea(@PathVariable String name) {
		for (ServerParkingArea pa : parkingAreas)
			if (pa.getName().equals(name))
				return pa;
		return null;
	}

	// TODO - change carNumber to identifier
	/**
	 * Finding the optimal parking slot for a user and save it for him according
	 * to the followings
	 * 
	 * @param carNumber
	 *            the user car number as identifier
	 * @param src
	 *            the starting position for the navigation, a Destination class
	 *            according to name
	 * @param dest
	 *            the ending position for the navigation, a Destination class
	 *            according to name
	 * @return the coordinates of the closest parking slot that been saved for
	 *         the user
	 */
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
			MapLocation srcLocation = null;
			if (!src.startsWith("$"))
				srcLocation = new Destination(src).getEntrance();
			else {
				String[] point = src.replace("$", "").substring(1, src.length() - 2).split(",");
				srcLocation = new MapLocation(Double.parseDouble(point[0].trim()), Double.parseDouble(point[1].trim()));
			}
			sps = new ServerParkingSlot(Navigation.closestParkingSlot(u, srcLocation, areas, new Destination(dest)));
			return sps.getLocation();
		} catch (ParseException | LoginException | NotExists e) {
			System.out.println("exception...");
		} catch (AlreadyExists e) {
			System.out.println((e + ""));
		}
		return null;
	}

	/**
	 * @return null if there isn't slot saved, and the slot info if we've found
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/FindPark", produces = "application/json")
	@ResponseBody
	public ServerParkingSlot getBestPark() {
		return sps != null ? sps : null;
	}

	// TODO - change to Identifier
	/**
	 * A user need to leave his old parking slot for getting another and navigating
	 * @param carNumber finding a user according to it and empty his parking slot
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/LeavePark", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String leavePark(@RequestParam("car") String carNumber) {
		JSONObject obj = new JSONObject();
		try {
			User u = new User(carNumber);
			u.getCurrentParking().changeStatus(ParkingSlotStatus.FREE);
			u.setCurrentParking(null);
			sps = null;
		} catch (Exception e) {
			return obj.put("Error", e.getMessage()) + "";
		}
		return "{}";
	}
}
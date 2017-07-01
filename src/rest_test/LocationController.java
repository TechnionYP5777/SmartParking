package rest_test;

import org.springframework.web.bind.annotation.*;

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
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {
	// A map from the destination name to its coordinates
	Map<String, MapLocation> hmap;

	// A ParkingAreas object
	ParkingAreas areas;

	// An arrayList of all of the Parking slots in the program
	JSONArray listSlot;

	// An object the include all of the areas at the database
	JSONArray listArea;

	HashMap<String, String> slotToArea;

	public JSONObject ParkingSlotSerialze(ParkingSlot s) {
		JSONObject o = new JSONObject();
		o.put("name", s == null ? "" : s.getName());
		o.put("status", s == null ? "" : s.getStatus());
		o.put("color", s == null ? "" : s.getColor());
		JSONObject l = new JSONObject();
		l.put("lat", s == null ? "" : s.getLocation().getLat());
		l.put("lon", s == null ? "" : s.getLocation().getLon());
		o.put("location", l);
		return o;
	}

	public JSONObject ParkingAreaSerialze(ParkingArea a) {
		JSONObject o = new JSONObject();
		JSONArray list = new JSONArray();
		for (ParkingSlot parkSlot : a.getParkingSlots())
			list.put(ParkingSlotSerialze(parkSlot));

		o.put("parkingSlots", list);
		JSONObject l = new JSONObject();
		l.put("lat", a.getLocation().getLat());
		l.put("lon", a.getLocation().getLon());
		o.put("location", l);
		o.put("color", a.getColor());
		o.put("name", a.getName());
		return o;
	}

	/**
	 * Initialize the fields above
	 */
	public LocationController() {
		DBManager.initialize();
		areas = new ParkingAreas();
		setUpLocations();
		slotToArea = new HashMap<String, String>();
		listSlot = new JSONArray();
		listArea = new JSONArray();
		setUpParkingSpots();
		setUpParkingAreas();
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
		try {
			final List<ParseObject> result = query.find();
			if (result == null)
				System.out.println("empty");
			for (final ParseObject park : result)
				listSlot.put(ParkingSlotSerialze(new ParkingSlot(park)));
		} catch (final Exception e) {
			System.out.println("exception...");
		}
	}

	/**
	 * Initialize the parkingList to have all of the parking slots
	 */
	void setUpParkingAreas() {
		for (ParkingArea a : areas.getParkingAreas()) {
			listArea.put(ParkingAreaSerialze(a));
			for (ParkingSlot parkSlot : a.getParkingSlots())
				slotToArea.put(parkSlot.getName(), a.getName());
		}
	}

	/**************************************************************************/

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/AreaFromSlot/{name}", produces = "application/json")
	@ResponseBody
	public String getAreaFromSlot(@PathVariable String name) {
		JSONObject o = new JSONObject();
		o.put("areaName", slotToArea.get(name));
		return o + "";
	}

	/**
	 * @return a string of JSONObject for all of the Locations names and
	 *         coordinates
	 */
	@CrossOrigin(origins = "*")
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
	@CrossOrigin(origins = "*")
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
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/Locations/{name}", produces = "application/json")
	@ResponseBody
	public MapLocation getLocation(@PathVariable String name) {
		setUpLocations();
		return hmap.get(name);
	}

	/**
	 * @return all of the parking slots
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/ParkingSlots", produces = "application/json")
	@ResponseBody
	public String getParkingSlots() {
		setUpParkingSpots();
		return listSlot + "";
	}

	/**
	 * Getting information of a specific parking slot according to name
	 * 
	 * @param name
	 *            the parking slot name
	 * 
	 * @return the slots details
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/ParkingSlots/{name}", produces = "application/json")
	@ResponseBody
	public String getPark(@PathVariable String name) {
		for (int i = 0; i < listSlot.length(); ++i)
			if (((JSONObject) listSlot.get(i)).getString("name").equals(name))
				return (JSONObject) listSlot.get(i) + "";
		return "";
	}

	/**
	 * @return all of the parking areas
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/ParkingAreas", produces = "application/json")
	@ResponseBody
	public String getParkingAreas() {
		setUpParkingAreas();
		return listArea + "";
	}

	/**
	 * Getting information of a specific parking area according to name
	 * 
	 * @param name
	 *            the parking area name
	 * @return the area details
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/ParkingAreas/{name}", produces = "application/json")
	@ResponseBody
	public String getArea(@PathVariable String name) {
		for (int i = 0; i < listArea.length(); ++i)
			if (((JSONObject) listArea.get(i)).getString("name").equals(name))
				return (JSONObject) listArea.get(i) + "";
		return "";
	}

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
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/FindPark/{key}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String findBestPark(@PathVariable String key, @RequestParam("src") String src,
			@RequestParam("dest") String dest) {
		JSONObject o = new JSONObject();
		try {
			UserState state = UserController.users.get(key);
			User u = new User(state.getUser().getCarNumber());
			if (u.getCurrentParking() != null) {
				o.put("error", "You have a parking slot");
				return o + "";
			}

			MapLocation srcLocation = null;
			if (!src.contains("Current Location")) {
				srcLocation = new Destination(src).getEntrance();
				u.addToLastPaths(src, dest);
			} else {
				String[] srcArr = src.split("\\$");
				u.addToLastPaths(srcArr[0], dest);
				String[] point = srcArr[1].substring(1, srcArr[1].length() - 2).split(",");
				srcLocation = new MapLocation(Double.parseDouble(point[0].trim()), Double.parseDouble(point[1].trim()));
			}
			ParkingSlot slot = Navigation.closestParkingSlot(u, srcLocation, areas, new Destination(dest));
			if (slot == null) {
				o.put("error", "Didn't found a slot");
				return o + "";
			}
			o.put("areaName", slotToArea.get(slot.getName()));
			o.put("lat", slot.getLocation().getLat());
			o.put("lon", slot.getLocation().getLon());
			return o + "";
		} catch (ParseException | LoginException | NotExists e) {
			System.out.println("exception... " + e);
			o.put("error", e + "");
			return o + "";
		}
	}

	/**
	 * @return null if there isn't slot saved, and the slot info if we've found
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/FindPark/{key}", produces = "application/json")
	@ResponseBody
	public String getBestPark(@PathVariable String key) {
		return ParkingSlotSerialze(UserController.users.get(key).getUser().getCurrentParking()) + "";
	}

	/**
	 * A user need to leave his old parking slot for getting another and
	 * navigating
	 * 
	 * @param carNumber
	 *            finding a user according to it and empty his parking slot
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/LeavePark/{key}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String leavePark(@PathVariable String key) {
		JSONObject obj = new JSONObject();
		try {
			UserState state = UserController.users.get(key);
			User u = new User(state.getUser().getCarNumber());
			if (u.getCurrentParking() == null) {
				obj.put("Status", "You are Free To go");
				return obj + "";
			}
			u.getCurrentParking().changeStatus(ParkingSlotStatus.FREE);
			u.setCurrentParking(null);
		} catch (Exception e) {
			obj.put("Error", e + "");
			return obj + "";
		}
		obj.put("Status", "You are free to go");
		return obj + "";
	}
}
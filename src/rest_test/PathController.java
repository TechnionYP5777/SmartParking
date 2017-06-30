package rest_test;

import org.springframework.web.bind.annotation.*;

import main.java.Exceptions.AlreadyExists;
import main.java.Exceptions.NotExists;
import main.java.data.members.MapLocation;
import main.java.data.management.DBManager;
import main.java.data.members.RoutePath;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parse4j.ParseException;
import org.springframework.stereotype.Controller;

@Controller
public class PathController {

	public PathController() {
		DBManager.initialize();
	}

	/**
	 * Get a RoutePath between area to dest
	 * 
	 * @param area
	 *            the area from which we want the walking route
	 * @param dest
	 *            the destination to which we want to be at the end of the
	 *            walking route
	 * @return Json serialization of the PathRoot or an error Json
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/GetPath", produces = "application/json")
	@ResponseBody
	public String getPath(@RequestParam("area") String area, @RequestParam("dest") String dest) {

		JSONObject obj = new JSONObject();
		try {
			RoutePath root = new RoutePath(area, dest);
			obj.put("source", root.getSource());
			obj.put("destination", root.getDestination());
			obj.put("Duration", root.getDuration());
			obj.put("Description", root.getDescription());

			JSONArray list = new JSONArray();
			ArrayList<MapLocation> path = root.getRoute();
			for (MapLocation mapLocation : path) {
				JSONObject loc = new JSONObject();
				loc.put("lat", mapLocation.getLat());
				loc.put("lon", mapLocation.getLon());
				list.put(loc);
			}

			obj.put("Route", list);

		} catch (ParseException | NotExists e) {
			obj.put("error", e.getMessage());
		}
		return obj + "";
	}

	/**
	 * Get a RoutePath between area to dest that include only the coordinates
	 * 
	 * @param area
	 *            the area from which we want the walking route
	 * @param dest
	 *            the destination to which we want to be at the end of the
	 *            walking route
	 * @return Json serialization of the PathRoot coordinates or an error Json
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/GetRoute", produces = "application/json")
	@ResponseBody
	public String GetRoute(@RequestParam("area") String area, @RequestParam("dest") String dest) {

		JSONObject obj = new JSONObject();
		try {
			RoutePath route = new RoutePath(area, dest);

			JSONArray list = new JSONArray();
			ArrayList<MapLocation> path = route.getRoute();
			for (MapLocation mapLocation : path) {
				JSONObject loc = new JSONObject();
				loc.put("lat", mapLocation.getLat());
				loc.put("lon", mapLocation.getLon());
				list.put(loc);
			}

			obj.put("Route", list);

		} catch (ParseException | NotExists e) {
			obj.put("error", e.getMessage());
		}
		return obj + "";
	}

	/**
	 * Send a RoutePath as a WrapObj (a class that used to deserialize Json to
	 * similar class) to the database and trying to add it.
	 * 
	 * @param o
	 *            the routePath as WrapObj will be changed to RoutePath
	 * @return true if added correctly else false
	 */
	@RequestMapping(value = "/SendPath", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean sendPath(@RequestBody WrapObj o) {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>(Arrays.asList(o.getPath()));
		try {
			new RoutePath(o.getParkingArea(), o.getDestination(), mp, o.getDuration(), o.getDescription());
			return true;
		} catch (ParseException | AlreadyExists | NotExists e) {
			return false;
		}
	}

	/**
	 * Getting the user last search routes
	 * 
	 * @param user
	 *            the identification of the user from whom we want to get the
	 *            last search paths
	 * @return String of JSONObject which include the sources & destinations
	 *         that the user searched last
	 */
	@RequestMapping(value = "/GetLastPaths/{key}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getLastPaths(@PathVariable String key, @RequestParam("userName") String user) {
		JSONObject obj = new JSONObject();
		if(UserController.users.get(key)==null){
			obj.put("error"," Not connected from device");
			return "" + obj;
		}
		ArrayList<String> lastRoutes = UserController.users.get(key).getUser().getLastPaths();
		if (lastRoutes == null || lastRoutes.isEmpty())
			obj.put("Status", "There isn't any saved paths");
		else {
			JSONArray list = new JSONArray();
			for (String route : lastRoutes) {
				JSONObject r = new JSONObject();
				r.put("src", route.split("\\$")[0]);
				r.put("dst", route.split("\\$")[1]);
				list.put(r);
			}
			obj.put("SavedPaths", list);
		}
		return obj + "";
	}
}
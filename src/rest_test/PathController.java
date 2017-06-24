package rest_test;

import org.springframework.web.bind.annotation.*;

import main.java.Exceptions.AlreadyExists;
import main.java.Exceptions.LoginException;
import main.java.Exceptions.NotExists;
import main.java.data.members.MapLocation;
import main.java.data.management.DBManager;
import main.java.data.members.RoutePath;
import main.java.data.members.User;

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

	@CrossOrigin(origins = "http://localhost:8100")
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

	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/GetRoute", produces = "application/json")
	@ResponseBody
	public String getRoot(@RequestParam("area") String area, @RequestParam("dest") String dest) {

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

	@RequestMapping(value = "/checkpost", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public WrapObj post(@RequestBody WrapObj o) {
		return o;
	}

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

	@RequestMapping(value = "/GetLastPaths", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getLastPaths(@RequestParam("userName") String user) {
		JSONObject obj = new JSONObject();

		try {
			ArrayList<String> lastRoutes = (new User(user)).getLastPaths();
			if (lastRoutes == null || lastRoutes.isEmpty())
				obj.put("Status", "There isn't any saved paths");
			else {
				JSONArray list = new JSONArray();
				for (String route : lastRoutes){
					JSONObject r = new JSONObject();
					r.put("src", route.split("\\$")[0]);
					r.put("dst", route.split("\\$")[1]);
					list.put(r);
				}
				
				obj.put("SavedPaths", list);
			}
		} catch (LoginException e) {
			obj.put("Error", e.getMessage());
		}
		return obj + "";
	}
}
package rest_test;

import org.springframework.web.bind.annotation.*;
import main.java.Exceptions.NotExists;
import main.java.data.members.MapLocation;
import main.java.data.management.DBManager;
import main.java.data.members.RootPath;
import java.util.ArrayList;
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
			RootPath root = new RootPath(area, dest);
			obj.put("source", root.getSource());
			obj.put("destination", root.getDestination());
			obj.put("Duration", root.getDuration());
			obj.put("Description", root.getDescription());

			JSONArray list = new JSONArray();
			ArrayList<MapLocation> path = root.getRoot();
			for (MapLocation mapLocation : path) {
				JSONObject loc = new JSONObject();
				loc.put("lat", mapLocation.getLat());
				loc.put("lon", mapLocation.getLon());
				list.put(loc);
			}

			obj.put("Root", list);

		} catch (ParseException | NotExists e) {
			obj.put("error", e.getMessage());
		}
		return obj + "";
	}

	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/GetRoot", produces = "application/json")
	@ResponseBody
	public String getRoot(@RequestParam("area") String area, @RequestParam("dest") String dest) {

		JSONObject obj = new JSONObject();
		try {
			RootPath root = new RootPath(area, dest);

			JSONArray list = new JSONArray();
			ArrayList<MapLocation> path = root.getRoot();
			for (MapLocation mapLocation : path) {
				JSONObject loc = new JSONObject();
				loc.put("lat", mapLocation.getLat());
				loc.put("lon", mapLocation.getLon());
				list.put(loc);
			}

			obj.put("Root", list);

		} catch (ParseException | NotExists e) {
			obj.put("error", e.getMessage());
		}
		return obj + "";
	}

	@RequestMapping(value = "/checkpost" , method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public WrapObj post(@RequestBody WrapObj o) {
		return o;
	}
	// post service add path: source,dest,desc,dur,
}
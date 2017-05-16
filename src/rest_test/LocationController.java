package rest_test;

import org.springframework.web.bind.annotation.*;

import main.java.data.management.DBManager;
import main.java.data.members.Destination;
import main.java.data.members.MapLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {

	public LocationController() {
		DBManager.initialize();
	}

	@RequestMapping(value = "/Locations", produces = "application/json")
	@ResponseBody
	public String getLocations() {

		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Destination");
		Map<String, MapLocation> hmap = new HashMap<String, MapLocation>();
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

		//System.out.println(hmap);
		return new JSONObject(hmap) + "";
	}
}
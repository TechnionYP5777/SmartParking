package main.java.logic;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import main.java.data.members.*;
import main.java.util.LogPrinter;

public class Navigation {

	private static JSONObject getInnerJSON(final String url) {
		final JSONParser $ = new JSONParser();
		try {
			return (JSONObject) ((JSONArray) ((JSONObject) ((JSONArray) ((JSONObject) $
					.parse(IOUtils.toString(new URL(url), StandardCharsets.UTF_8))).get("rows")).get(0))
							.get("elements")).get(0);
		} catch (ParseException | IOException ¢) {
			LogPrinter.createLogFile(¢);
		}
		return null;
	}

	private static String createURL(final MapLocation source, final MapLocation target, final boolean walkingMode) {
		String $ = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + source.getLat()
				+ "," + source.getLon() + "&destinations=" + target.getLat() + "," + target.getLon()
				+ "&key=AIzaSyC_zyT9qStf8gR6Jw44kKgaDw2dAuHF6bk";// AIzaSyDw25loi0t1ms-bCuLPHS2Bm9aEIvyu9Wo";
		if (walkingMode)
			$ += "&mode=walking";

		return $;
	}

	public static long getDistance(final MapLocation source, final MapLocation target, final boolean walkingMode) {
		final JSONObject $ = getInnerJSON(createURL(source, target, walkingMode));
		return $ == null ? 0 : (long) ((JSONObject) $.get("distance")).get("value");
	}

	public static long getDuration(final MapLocation source, final MapLocation target, final boolean walkingMode) {
		final JSONObject $ = getInnerJSON(createURL(source, target, walkingMode));
		return !$.containsKey("duration") || !((JSONObject) $.get("duration")).containsKey("value") ? Integer.MAX_VALUE
				: $ != null ? (long) ((JSONObject) $.get("duration")).get("value") : 0;
	}

	public static ParkingSlot closestParkingSlot(final User u, final MapLocation currentLocation, final ParkingAreas a,
			final Destination d) throws org.parse4j.ParseException {

		ParkingSlot park = null;
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("ParkingSlot");
		query.whereEqualTo("status", ParkingSlotStatus.FREE.ordinal());
		query.whereGreaterThanOrEqualTo("color", u.getSticker().ordinal());
		List<ParseObject> slotsList = query.find();
		if (slotsList == null)
			return null;
		long minDuration = Integer.MAX_VALUE;
		for (final ParseObject parkingSlot : slotsList) {
			ParkingSlot slot = new ParkingSlot(parkingSlot);
			final long duration = getDuration(slot.getLocation(), d.getEntrance(), true);
			if (duration < minDuration) {
				park = slot;
				minDuration = duration;
			}
		}
		if (park != null) {
			u.setCurrentParking(park);
			park.changeStatus(ParkingSlotStatus.TAKEN);
		}
		return park;
	}
}

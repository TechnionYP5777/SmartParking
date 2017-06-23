package main.java.data.members;

import java.util.List;

import org.parse4j.ParseException;
import org.parse4j.ParseGeoPoint;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import main.java.data.management.DBManager;
import main.java.util.LogPrinter;

/**
 * @author Toma
 * @since 12.11.16 This class represent a parking slot
 */
public class ParkingSlot extends dbMember {

	// The slot's name (id). Should be a unique value.
	private String name;

	// The slot's status. Can be either free, taken or unavailable
	private ParkingSlotStatus status;

	// The slot's color. Can be any sticker color
	private StickersColor color;

	// The slot's location
	private MapLocation location;


	/* Constructors */

	// Create a new parking slot. Will result in a new slot in the DB.
	public ParkingSlot(final String name, final ParkingSlotStatus status, final StickersColor color,
		 final MapLocation location) throws ParseException {
		validateArgument(status, color, location);

		DBManager.initialize();
		setParseObject("ParkingSlot");
		setName(name);
		setStatus(status);
		setColor(color);
		setLocation(location);
		setObjectId();
		parseObject.save();
	}

	public ParkingSlot(final ParseObject obj) throws ParseException {
		DBManager.initialize();
		parseObject = obj;
		name = parseObject.getString("name");
		status = ParkingSlotStatus.values()[parseObject.getInt("status")];
		color = StickersColor.values()[parseObject.getInt("color")];
		final ParseGeoPoint geo = parseObject.getParseGeoPoint("location");
		location = new MapLocation(geo.getLatitude(), geo.getLongitude());
		objectId = parseObject.getObjectId();
		parseObject.save();
	}

	public ParkingSlot(final String name) throws ParseException {
		DBManager.initialize();

		final ParseQuery<ParseObject> query = ParseQuery.getQuery("ParkingSlot");
		query.whereEqualTo("name", name);
		final ParseObject parseObject = query.find().get(0);

		this.parseObject = parseObject;
		this.name = this.parseObject.getString("name");
		status = ParkingSlotStatus.values()[this.parseObject.getInt("status")];
		color = StickersColor.values()[this.parseObject.getInt("color")];
		final ParseGeoPoint geo = this.parseObject.getParseGeoPoint("location");
		location = new MapLocation(geo.getLatitude(), geo.getLongitude());
		objectId = this.parseObject.getObjectId();
		this.parseObject.save();
	}

	public static String ParkingNameByLocation(final MapLocation l) {
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("ParkingSlot");
		query.whereNear("location", new ParseGeoPoint(l.getLat(), l.getLon()));
		query.limit(1);
		try {
			final List<ParseObject> $ = query.find();
			return $ == null || $.isEmpty() ? null : $.get(0).getString("name");
		} catch (final Exception e) {
			return null;
		}
	}

	/* Getters */

	public String getName() {
		return name;
	}

	public ParkingSlotStatus getStatus() {
		return status;
	}

	public StickersColor getColor() {
		return color;
	}

	public MapLocation getLocation() {
		return location;
	}


	/* Setters */

	private void setName(final String name) throws ParseException {
		this.name = name;
		parseObject.put("name", name);
		parseObject.save();
	}

	private void setStatus(final ParkingSlotStatus ¢) throws ParseException {
		status = ¢;
		parseObject.put("status", ¢.ordinal());
		parseObject.save();
	}

	public void setColor(final StickersColor ¢) throws ParseException {
		color = ¢;
		parseObject.put("color", ¢.ordinal());
		parseObject.save();
	}

	private void setLocation(final MapLocation ¢) throws ParseException {
		location = ¢;
		parseObject.put("location", new ParseGeoPoint(¢.getLat(), ¢.getLon()));
		parseObject.save();
	}

	/* Methods */

	private void validateArgument(final ParkingSlotStatus s, final StickersColor c,
			final MapLocation l) throws IllegalArgumentException {
		if (s == null)
			throw new IllegalArgumentException("status can not be empty!");
		if (c == null)
			throw new IllegalArgumentException("color can not be empty!");
		if (l == null)
			throw new IllegalArgumentException("location can not be empty!");
	}

	private ParseObject findContaingParkingArea() {
		final ParseQuery<ParseObject> $ = ParseQuery.getQuery("ParkingArea");
		$.whereEqualTo("parkingSlots", parseObject);
		try {
			return $.find().get(0);
		} catch (final ParseException ¢) {
			System.out.println("Could not find the containing area");
			LogPrinter.createLogFile(¢);
			return null;
		}
	}

	public String findContainingParkingArea() {
		return (String) findContaingParkingArea().get("name");
	}

	public void changeStatus(final ParkingSlotStatus newStatus) {
		try {
			setStatus(newStatus);
		} catch (final ParseException ¢) {
			System.out.println("could not set the slot's status");
			LogPrinter.createLogFile(¢);
		}
	};

	public void removeParkingSlotFromDB() throws ParseException {
		deleteParseObject();
	}

	public void removeParkingSlotFromAreaAndDB() throws ParseException {
		new ParkingArea(findContainingParkingArea()).removeParkingSlot(this);
	}

	/***
	 * for now only delete from the DB the current parking
	 */
	@Override
	public void deleteParseObject() throws ParseException {
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("PMUser");
		query.whereEqualTo("currentParking", getParseObject());
		try {
			final List<ParseObject> users = query.find();
			if (users != null) {
				users.get(0).remove("currentParking");
				users.get(0).save();
			}
		} catch (final Exception e) {
			return;
		}
		parseObject.delete();
	}

}

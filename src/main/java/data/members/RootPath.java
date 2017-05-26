package main.java.data.members;

import java.util.ArrayList;
import java.util.List;

import org.parse4j.ParseException;
import org.parse4j.ParseGeoPoint;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import main.java.Exceptions.*;
import main.java.data.management.DBManager;

/**
 * @Author DavidCohen55
 */
public class RootPath extends dbMember {
	private String source;
	private String destination;
	private ArrayList<MapLocation> root;
	private double duration;
	private String description;

	private static final String SOURCE = "source";
	private static final String DESTINATION = "destination";
	private static final String ROOT = "Root";
	private static final String DURATION = "Duration";
	private static final String DESCRIPTION = "Description";
	private static final String TABLE_NAME = "Path";

	private static ParseObject getPathParse(final String src, final String dest) {
		final ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
		query.whereEqualTo(SOURCE, src);
		query.whereEqualTo(DESTINATION, dest);
		query.limit(1);
		try {
			final List<ParseObject> $ = query.find();
			return $ == null || $.isEmpty() ? null : $.get(0);
		} catch (final Exception e) {
			return null;
		}
	}

	public static boolean PathExists(final String src, final String dest) {
		return getPathParse(src, dest) != null;
	}

	public RootPath(final String src, final String dest, final ArrayList<MapLocation> root, int dur, String desc)
			throws ParseException, AlreadyExists, NotExists {
		DBManager.initialize();

		if (!ParkingArea.areaExists(src) && !Destination.destinationExists(dest))
			throw new NotExists("Didn't find src or dest");

		ParseObject po = getPathParse(src, dest);
		if (po == null)
			setParseObject(TABLE_NAME);
		else {
			if (po.getDouble(DURATION) < dur)
				throw new AlreadyExists("Better path exists");
			this.parseObject = po;
		}

		this.setSource(src);
		this.setDestination(dest);
		this.setRoot(root);
		this.setDuration(dur);
		this.setDescription(desc);
		this.setObjectId();
	}

	public RootPath(final String src, final String dest) throws ParseException, AlreadyExists, NotExists {
		DBManager.initialize();

		if (!ParkingArea.areaExists(src) && !Destination.destinationExists(dest))
			throw new NotExists("Didn't find src or dest");

		if (!PathExists(src, dest))
			throw new NotExists("Didn't find the path");

		final ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
		query.whereEqualTo(SOURCE, src);
		query.whereEqualTo(DESTINATION, dest);
		query.limit(1);
		final List<ParseObject> $ = query.find();
		if ($ == null || $.isEmpty())
			throw new NotExists("Didn't find Object");

		this.parseObject = $.get(0);
		this.setObjectId();
		this.setSource(this.parseObject.getString(SOURCE));
		this.setDestination(this.parseObject.getString(DESTINATION));
		this.setDuration(this.parseObject.getDouble(DURATION));
		this.setDescription(this.parseObject.getString(DESCRIPTION));
		
		this.root = new ArrayList<MapLocation>();
		List<Object> points = this.parseObject.getList(ROOT);
		for (Object o : points)
			this.root.add((new MapLocation(((ParseGeoPoint) o).getLatitude(), ((ParseGeoPoint) o).getLongitude())));

		setObjectId();
	}

	public void setDestination(final String dest) throws ParseException, NotExists {
		if (!Destination.destinationExists(dest))
			throw new NotExists("Didn't find destination");
		this.destination = dest;
		parseObject.put(DESTINATION, dest);
		parseObject.save();
	}

	public void setSource(final String src) throws ParseException, NotExists {
		if (!ParkingArea.areaExists(src))
			throw new NotExists("Didn't find source");
		this.source = src;
		parseObject.put(SOURCE, src);
		parseObject.save();
	}

	public void setRoot(final ArrayList<MapLocation> root) throws ParseException {
		this.root = root;
		ArrayList<ParseGeoPoint> pRoot = new ArrayList<ParseGeoPoint>();
		for (MapLocation m : root)
			pRoot.add(new ParseGeoPoint(m.getLat(), m.getLon()));
		parseObject.put(ROOT, pRoot);
		parseObject.save();
	}

	public void setDuration(double dur) throws ParseException {
		this.duration = dur;
		parseObject.put(DURATION, dur);
		parseObject.save();
	}

	public void setDescription(String desc) throws ParseException {
		this.description = desc;
		parseObject.put(DESCRIPTION, desc);
		parseObject.save();
	}

	public String getSource() {
		return this.source;
	}

	public String getDestination() {
		return this.destination;
	}

	public ArrayList<MapLocation> getRoot() {
		return root;
	}

	public double getDuration() {
		return this.duration;
	}

	public String getDescription() {
		return this.description;
	}
}
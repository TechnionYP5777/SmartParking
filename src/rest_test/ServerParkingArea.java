package rest_test;

import java.util.HashSet;
import java.util.Set;

import main.java.data.members.MapLocation;
import main.java.data.members.ParkingArea;
import main.java.data.members.ParkingSlot;
import main.java.data.members.StickersColor;

public class ServerParkingArea {

	private String name;
	private StickersColor color;
	private MapLocation location;
	private Set<ServerParkingSlot> parkingSlots;

	public ServerParkingArea(ParkingArea p) {
		color = p.getColor();
		name = p.getName();
		location = p.getLocation();
		parkingSlots = new HashSet<ServerParkingSlot>();

		for (ParkingSlot parkSlot : p.getParkingSlots())
			parkingSlots.add(new ServerParkingSlot(parkSlot));
	}

	public StickersColor getColor() {
		return color;
	}

	public void setColor(StickersColor c) {
		this.color = c;
	}

	public MapLocation getLocation() {
		return location;
	}

	public void setLocation(MapLocation l) {
		this.location = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ServerParkingSlot> getParkingSlots() {
		return parkingSlots;
	}

	public void setParkingSlots(Set<ServerParkingSlot> ss) {
		this.parkingSlots = ss;
	}
}
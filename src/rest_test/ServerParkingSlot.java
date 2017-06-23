package rest_test;

import main.java.data.members.MapLocation;
import main.java.data.members.ParkingSlot;
import main.java.data.members.ParkingSlotStatus;
import main.java.data.members.StickersColor;

public class ServerParkingSlot {
	private String name;
	private ParkingSlotStatus status;
	private StickersColor color;
	private MapLocation location;

	public ServerParkingSlot(ParkingSlot parkSlot) {
		this.name = parkSlot.getName();
		this.status = parkSlot.getStatus();
		this.color = parkSlot.getColor();
		this.location = parkSlot.getLocation();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParkingSlotStatus getStatus() {
		return status;
	}

	public void setStatus(ParkingSlotStatus s) {
		this.status = s;
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
}

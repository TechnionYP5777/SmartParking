package rest_test;
import main.java.data.members.MapLocation;

public class WrapObj{
	public MapLocation[] path;
	public String parkingArea;
	public String destination;
	public String description;
	public double duration;
	
	public MapLocation[] getPath() {
		return path;
	}

	public void setPath(MapLocation[] path) {
		this.path = path;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public void setParkingArea(String parkingArea) {
		this.parkingArea = parkingArea;
	}

	public String getParkingArea(){
		return this.parkingArea;
	}
	
	public WrapObj(){	}
	
	
	public int Size(){
		return this.path.length;
	}
	

}

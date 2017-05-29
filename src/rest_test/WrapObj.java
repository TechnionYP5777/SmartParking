package rest_test;
import main.java.data.members.MapLocation;

public class WrapObj{
	public MapLocation[] path;
	public String parkingArea;
	public String destination;
	public String description;
	public double duration;
	
	public WrapObj(){	}
	
	public int Size(){
		return this.path.length;
	}
	
	public String getParkingArea(){
		return this.parkingArea;
	}
}

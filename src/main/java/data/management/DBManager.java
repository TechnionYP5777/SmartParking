package main.java.data.management;

import org.parse4j.Parse;

/* DBManager class - class representing the database manager
@author DavidCohen55
@since 2017-03-27
*/

public class DBManager {
	private static final String appId = "ParkingNav";
	private static final String restKey = "2139f-231ff2-738ff";
	private static final String serverUrl = "https://parkingnevserver.herokuapp.com/parse";

	public static void initialize() {
		Parse.initialize(appId, restKey, serverUrl);
	}
}
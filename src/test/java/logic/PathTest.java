package test.java.logic;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.parse4j.ParseException;

import main.java.Exceptions.AlreadyExists;
import main.java.Exceptions.NotExists;
import main.java.data.members.Destination;
import main.java.data.members.MapLocation;
import main.java.data.members.RootPath;
import main.java.util.LogPrinter;

public class PathTest {

	@Test
	public void test() {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		mp.add(new MapLocation(23, 23));
		mp.add(new MapLocation(24, 24));
		mp.add(new MapLocation(25, 25));

		try {
			RootPath p = new RootPath("Ulman", "Sports Center", mp);
			Assert.assertEquals("Ulman", p.getSource());
			Assert.assertEquals("Sports Center", p.getDestination());
			Assert.assertEquals(3, p.getRoot().size());
			p.deleteParseObject();
		} catch (ParseException | AlreadyExists | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}

	}
}
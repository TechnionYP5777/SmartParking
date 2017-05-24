package test.java.logic;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.parse4j.ParseException;

import main.java.Exceptions.AlreadyExists;
import main.java.Exceptions.NotExists;
import main.java.data.members.MapLocation;
import main.java.data.members.RootPath;

public class PathTest {

	@Test
	public void test() {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		mp.add(new MapLocation(23, 23));
		mp.add(new MapLocation(24, 24));
		mp.add(new MapLocation(25, 25));

		try {
			RootPath p = new RootPath("Ulman", "Sports Center", mp);
		} catch (ParseException e) {
			Assert.fail();
		} catch (AlreadyExists e) {
			Assert.fail();
		} catch (NotExists e) {
			Assert.fail();
		}

	}
}
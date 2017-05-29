package test.java.logic;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.parse4j.ParseException;

import main.java.Exceptions.AlreadyExists;
import main.java.Exceptions.NotExists;
import main.java.data.members.MapLocation;
import main.java.data.members.RoutePath;
import main.java.util.LogPrinter;

public class PathTest {

	@Before
	public void BeforePathTest() {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		mp.add(new MapLocation(23, 23));
		mp.add(new MapLocation(24, 24));
		mp.add(new MapLocation(25, 25));
		try {
			new RoutePath("Makak", "Sports Center", mp, 5, "Turn Left and then Straight");
		} catch (ParseException | AlreadyExists | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}

	@After
	public void AfterPathTest() {
		try {
			new RoutePath("Makak", "Sports Center").deleteParseObject();
		} catch (ParseException | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}

	}

	// check that the constructor works
	@Test
	public void test01() {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		mp.add(new MapLocation(23, 23));
		mp.add(new MapLocation(24, 24));

		try {
			RoutePath p = new RoutePath("Makak", "Sports Center");
			Assert.assertEquals("Makak", p.getSource());
			Assert.assertEquals("Sports Center", p.getDestination());
			Assert.assertEquals(3, p.getRoute().size());
			Assert.assertTrue(p.getDuration() == 5);
			Assert.assertEquals("Turn Left and then Straight", p.getDescription());

		} catch (ParseException | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}

		try {
			RoutePath p2 = new RoutePath("Makak", "Sports Center", mp, 1, "Turn Right");
			Assert.assertEquals("Makak", p2.getSource());
			Assert.assertEquals("Sports Center", p2.getDestination());
			Assert.assertTrue(p2.getDuration() == 1);
			Assert.assertEquals(2, p2.getRoute().size());
			Assert.assertEquals("Turn Right", p2.getDescription());
		} catch (AlreadyExists | ParseException | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}

		try {
			new RoutePath("Makak", "Sports Center", mp, 9, "Turn Right");
		} catch (ParseException | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		} catch (AlreadyExists e) {
			Assert.assertEquals("Better path exists", e.getMessage());
		}
	}

	// check that the setters work
	@Test
	public void test02() {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		mp.add(new MapLocation(23, 23));
		RoutePath p = null;

		try {
			p = new RoutePath("Makak", "Sports Center");
			p.setSource("Pool");
			p.setDestination("Industrial Engineering Faculty");
			p.setDescription("Turn Right");
			p.setDuration(3);
			p.setRoot(mp);

			Assert.assertFalse(RoutePath.PathExists("Makak", "Sports Center"));
			Assert.assertTrue(RoutePath.PathExists("Pool", "Industrial Engineering Faculty"));

			RoutePath r = new RoutePath("Pool", "Industrial Engineering Faculty");
			Assert.assertEquals(r.getSource(), p.getSource());
			Assert.assertEquals(r.getDestination(), p.getDestination());
			Assert.assertEquals(r.getRoute().size(), p.getRoute().size());
			Assert.assertTrue(r.getDuration() == p.getDuration());
			Assert.assertEquals(r.getDescription(), p.getDescription());

		} catch (ParseException | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}

		try {
			p.setSource("Pool1");
		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		} catch (NotExists e) {
			Assert.assertEquals("Didn't find source", e.getMessage());
		}

		try {
			p.setDestination("Industrial Engineering Faculty1");
		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		} catch (NotExists e) {
			Assert.assertEquals("Didn't find destination", e.getMessage());
		}
		
		try {
			p.setSource("Makak");
			p.setDestination("Sports Center");

		} catch (ParseException | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}

	// check that the root isn't found
	@Test
	public void test03() {
		try {
			new RoutePath("Pool", "Industrial Engineering Faculty");
			throw new AlreadyExists("Path already exists");
		} catch (ParseException | AlreadyExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		} catch (NotExists e) {
			Assert.assertEquals("Didn't find the path", e.getMessage());
		}
	}
}
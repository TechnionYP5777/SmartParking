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
import main.java.util.LogPrinter;

public class PathTest {

	// check that the constructor works
	@Test
	public void test01() {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		mp.add(new MapLocation(23, 23));
		mp.add(new MapLocation(24, 24));
		mp.add(new MapLocation(25, 25));
		RootPath p = null;
		try {
			p = new RootPath("Ulman", "Sports Center", mp);
			Assert.assertEquals("Ulman", p.getSource());
			Assert.assertEquals("Sports Center", p.getDestination());
			Assert.assertEquals(3, p.getRoot().size());
		} catch (ParseException | AlreadyExists | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
		try {
			RootPath p2 = new RootPath("Ulman", "Sports Center", mp);
		} catch (ParseException | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		} catch (AlreadyExists e) {
			Assert.assertEquals("Already exists", e.getMessage());
		}

		try {
			p.deleteParseObject();
		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}

	}

	// check that the constructor gets rootPath by src and dest
	@Test
	public void test02() {

		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		mp.add(new MapLocation(23, 23));
		mp.add(new MapLocation(24, 24));
		mp.add(new MapLocation(25, 25));
		RootPath p = null;
		try {
			p = new RootPath("Ulman", "Sports Center", mp);
			RootPath r = new RootPath("Ulman", "Sports Center");
			Assert.assertEquals(r.getSource(), p.getSource());
			Assert.assertEquals(r.getDestination(), p.getDestination());
			Assert.assertEquals(r.getRoot().size(), p.getRoot().size());
			p.deleteParseObject();
		} catch (ParseException | AlreadyExists | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}

	@Test
	public void test03() {
		ArrayList<MapLocation> mp = new ArrayList<MapLocation>();
		RootPath p = null;
		try {
			p = new RootPath("Ulman", "Sports Center", mp);
			p.setSource("Computer Science Faculty");
			p.setDestination("Industrial Engineering Faculty");
			mp.add(new MapLocation(23, 23));
			p.setRoot(mp);
			Assert.assertFalse(RootPath.PathExists("Ulman", "Sports Center"));
			Assert.assertTrue(RootPath.PathExists("Computer Science Faculty", "Industrial Engineering Faculty"));
			RootPath r = new RootPath("Computer Science Faculty", "Industrial Engineering Faculty");
			Assert.assertEquals(r.getSource(), p.getSource());
			Assert.assertEquals(r.getDestination(), p.getDestination());
			Assert.assertEquals(r.getRoot().size(), p.getRoot().size());
			p.deleteParseObject();
		} catch (ParseException | AlreadyExists | NotExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}
	
	// check that the root isn't found
	@Test
	public void test04() {
		try {
			RootPath p = new RootPath("Computer Science Faculty", "Industrial Engineering Faculty");
		} catch (ParseException | AlreadyExists e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		} catch (NotExists e) {
			Assert.assertEquals("Didn't find the path", e.getMessage());
		}
	}	
}
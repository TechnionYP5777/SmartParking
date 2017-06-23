package test.java.logic;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import main.java.data.members.ParkingSlot;
import main.java.data.members.StickersColor;
import main.java.util.LogPrinter;
import main.java.data.members.ParkingSlotStatus;
import main.java.data.members.MapLocation;
import main.java.data.members.ParkingArea;

public class parkingSlotTest {

	@Test
	public void test0() {

		// Create a new parking slot in the DB
		// Please note that EVERY activation of this test will result in a new
		// testSlot row in the DB
		try {
			final ParkingSlot p = new ParkingSlot("testSlotA", ParkingSlotStatus.FREE, StickersColor.RED,
					new MapLocation(32.778153, 35.021855));
			assert p != null;
			p.deleteParseObject();
		} catch (final Exception ¢) {
			LogPrinter.createLogFile(¢);
			Assert.fail();
		}
	}

	@Test
	public void testGetContainingArea() {
		try {
			final ParkingSlot slot1 = new ParkingSlot("testSlotB", ParkingSlotStatus.FREE, StickersColor.RED,
					new MapLocation(0, 0));
			final Set<ParkingSlot> slots = new HashSet<ParkingSlot>();
			slots.add(slot1);
			final ParkingArea area = new ParkingArea("areaSlotTestB", new MapLocation(0, 0), slots, StickersColor.RED);
			Assert.assertEquals(area.getName(), slot1.findContainingParkingArea());

			area.deleteParseObject();
			slot1.deleteParseObject();

		} catch (final Exception ¢) {
			LogPrinter.createLogFile(¢);
			Assert.fail();
		}
	}

	@Test
	public void testRemoveSlotFromArea() {
		try {
			// Arrange
			final ParkingSlot slot1 = new ParkingSlot("testSlotC", ParkingSlotStatus.FREE, StickersColor.RED,
					new MapLocation(0, 0));
			final Set<ParkingSlot> slots = new HashSet<ParkingSlot>();
			slots.add(slot1);
			final ParkingArea area = new ParkingArea("areaSlotTestC", new MapLocation(0, 0), slots, StickersColor.RED);

			// Act + assert
			slot1.deleteParseObject();
			// slot1.removeParkingSlotFromAreaAndDB();
			area.deleteParseObject();
		} catch (final Exception ¢) {
			LogPrinter.createLogFile(¢);
			Assert.fail();
		}
	}

	@Test
	public void getSlotByName() {
		try {
			// Arrange
			final ParkingSlot slot1 = new ParkingSlot("testSlotD", ParkingSlotStatus.FREE, StickersColor.RED,
					new MapLocation(0, 0));

			// Assert
			Assert.assertEquals(slot1.getObjectId(), new ParkingSlot("testSlotD").getObjectId());

			// Cleanup
			slot1.deleteParseObject();

		} catch (final Exception ¢) {
			LogPrinter.createLogFile(¢);
			Assert.fail();
		}
	}

}
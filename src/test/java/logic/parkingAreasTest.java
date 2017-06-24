package test.java.logic;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.parse4j.ParseException;

import main.java.data.members.*;
import main.java.util.LogPrinter;

public class parkingAreasTest {
	ParkingSlot slot1, slot2, slot3, slot4;
	ParkingArea area1, area2;
	ParkingAreas areas;

	// insert new ParkingArea and ParkingSlots
	@Before
	public void beforeTest() {
		try {
			slot1 = new ParkingSlot("parkingAreasTest1", ParkingSlotStatus.TAKEN, StickersColor.GREEN,
					new MapLocation(32.123, 32.123));
			slot2 = new ParkingSlot("parkingAreasTest2", ParkingSlotStatus.TAKEN, StickersColor.GREEN,
					new MapLocation(0, 0));
			slot3 = new ParkingSlot("parkingAreasTest3", ParkingSlotStatus.TAKEN, StickersColor.RED,
					new MapLocation(32.123, 32.123));
			slot4 = new ParkingSlot("parkingAreasTest4", ParkingSlotStatus.TAKEN, StickersColor.RED,
					new MapLocation(0, 0));

			Set<ParkingSlot> slots = new HashSet<ParkingSlot>();
			slots.add(slot1);
			slots.add(slot2);
			area1 = new ParkingArea("t1", new MapLocation(0, 0), slots, StickersColor.GREEN);

			slots = new HashSet<ParkingSlot>();
			slots.add(slot3);
			slots.add(slot4);
			area2 = new ParkingArea("t2", new MapLocation(0, 0), slots, StickersColor.RED);
			areas = new ParkingAreas(null);
			areas.addParkingArea(area1);
			areas.addParkingArea(area2);

		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}

	// remove new ParkingArea and ParkingSlots
	@After
	public void afterTest() {
		try {
			area1.removeParkingAreaFromDB();
			area2.removeParkingAreaFromDB();
			/*slot1.deleteParseObject();
			slot2.deleteParseObject();
			slot3.deleteParseObject();
			slot4.deleteParseObject();*/
			areas.deleteParseObject();
		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}

	@Test
	public void test1() {
		Assert.assertEquals(6, new ParkingAreas().getParkingAreas().size());
		
		Assert.assertEquals(2, areas.getParkingAreas().size());

		Assert.assertEquals(2, areas.getNumOfSlotsByArea(area1));
		Assert.assertEquals(2, areas.getNumOfTakenByArea(area1));
		Assert.assertEquals(0, areas.getNumOfFreeByArea(area1));

		slot1.changeStatus(ParkingSlotStatus.FREE);
		Assert.assertEquals(1, areas.getNumOfFreeByArea(area1));
		Assert.assertEquals(1, areas.getNumOfTakenByArea(area1));

		slot2.changeStatus(ParkingSlotStatus.FREE);
		Assert.assertEquals(2, areas.getNumOfFreeByArea(area1));
		Assert.assertEquals(0, areas.getNumOfTakenByArea(area1));
	}

	@Test
	public void test2() {
		Assert.assertEquals(2, areas.getParkingAreas().size());
		slot1.changeStatus(ParkingSlotStatus.FREE);
		slot2.changeStatus(ParkingSlotStatus.FREE);
		Assert.assertEquals(2, areas.getNumOfFreeSlots());
		Assert.assertEquals(2, areas.getNumOfTakenSlots());

		Assert.assertEquals(2, areas.getNumOfFreeByArea(area1));
		Assert.assertEquals(0, areas.getNumOfTakenByArea(area1));
		Assert.assertEquals(0, areas.getNumOfFreeByArea(area2));
		Assert.assertEquals(2, areas.getNumOfTakenByArea(area2));

		// -------------------------------------------------------

		slot3.changeStatus(ParkingSlotStatus.FREE);
		Assert.assertEquals(3, areas.getNumOfFreeSlots());
		Assert.assertEquals(1, areas.getNumOfTakenSlots());

		Assert.assertEquals(1, areas.getNumOfFreeByArea(area2));
		Assert.assertEquals(1, areas.getNumOfTakenByArea(area2));
	}

	@Test
	public void test3() {
		try {
			slot1.changeStatus(ParkingSlotStatus.FREE);
			Assert.assertTrue("parkingAreasTest1".equalsIgnoreCase(areas.getParkingslotByArea(area1).getName()));
			Assert.assertFalse("parkingAreasTest2".equalsIgnoreCase(areas.getParkingslotByArea(area1).getName()));
			
			slot1.changeStatus(ParkingSlotStatus.TAKEN);
			slot2.changeStatus(ParkingSlotStatus.FREE);
			Assert.assertTrue("parkingAreasTest2".equalsIgnoreCase(areas.getParkingslotByArea(area1).getName()));
		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}
}

/*
 * @Test public void test9() throws ParseException { // Arrange final
 * Set<ParkingSlot> slots = new HashSet<ParkingSlot>(); final MapLocation loc =
 * new MapLocation(0, 0); final ParkingArea area1 = new ParkingArea("t1", loc,
 * slots, StickersColor.RED), area2 = new ParkingArea("t2", loc, slots,
 * StickersColor.RED), area3 = new ParkingArea("t3", loc, slots,
 * StickersColor.RED); final Set<ParkingArea> a = new HashSet<ParkingArea>();
 * a.add(area1); a.add(area2); a.add(area3);
 * 
 * // Act final ParkingAreas areas = new ParkingAreas(a); final List<String>
 * names = areas.getParkingAreasNames();
 * 
 * // Assert Assert.assertEquals(3, names.size()); assert names.contains("t1");
 * assert names.contains("t2"); assert names.contains("t3");
 * 
 * // Cleanup areas.deleteParseObject(); area1.deleteParseObject();
 * area2.deleteParseObject(); area3.deleteParseObject(); }
 * 
 * @Test public void test10() throws ParseException { // Arrange final
 * ParkingSlot slot1 = new ParkingSlot("testS1", ParkingSlotStatus.FREE,
 * StickersColor.RED, new MapLocation(0, 0)), slot2 = new ParkingSlot("testS2",
 * ParkingSlotStatus.FREE, StickersColor.RED, new MapLocation(0, 0)); final
 * Set<ParkingSlot> slots = new HashSet<ParkingSlot>(); slots.add(slot1);
 * slots.add(slot2);
 * 
 * final MapLocation loc = new MapLocation(0, 0); final ParkingArea area = new
 * ParkingArea("t1", loc, slots, StickersColor.RED);
 * 
 * final Set<ParkingArea> a = new HashSet<ParkingArea>(); a.add(area); final
 * ParkingAreas areas = new ParkingAreas(a);
 * 
 * // Act final HashMap<String, StickersColor> colors =
 * areas.getParkingAreasColor(); final HashMap<String, MapLocation> locations =
 * areas.getParkingAreasLocation();
 * 
 * // Assert Assert.assertEquals(1, colors.size()); Assert.assertEquals(1,
 * locations.size()); Assert.assertEquals(StickersColor.RED, colors.get("t1"));
 * Assert.assertEquals(loc, locations.get("t1"));
 * 
 * // Cleanup areas.deleteParseObject(); area.deleteParseObject();
 * slot1.deleteParseObject(); slot2.deleteParseObject(); }
 */
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
			final Set<ParkingArea> a = new HashSet<ParkingArea>();
			a.add(area1);
			a.add(area2);
			areas = new ParkingAreas(a);

		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}

	// remove new ParkingArea and ParkingSlots
	@After
	public void afterTest() {
		try {
			area1.deleteParseObject();
			area2.deleteParseObject();
			slot1.deleteParseObject();
			slot2.deleteParseObject();
			slot3.deleteParseObject();
			slot4.deleteParseObject();
			areas.deleteParseObject();
		} catch (ParseException e) {
			LogPrinter.createLogFile(e);
			Assert.fail();
		}
	}

	@Test
	public void test1() {
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

		//-------------------------------------------------------
		
		slot3.changeStatus(ParkingSlotStatus.FREE);
		Assert.assertEquals(3, areas.getNumOfFreeSlots());
		Assert.assertEquals(1, areas.getNumOfTakenSlots());
		
		Assert.assertEquals(1, areas.getNumOfFreeByArea(area2));
		Assert.assertEquals(1, areas.getNumOfTakenByArea(area2));
	}

	
}

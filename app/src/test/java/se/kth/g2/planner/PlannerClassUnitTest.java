package se.kth.g2.planner;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;

/**
 * Created by sieken on 2017-04-28.
 */

public class PlannerClassUnitTest {

    PlannerClass planner;
    String[] testListOfEventTitles = {"Hansel", "Gretel", "Ashking", "Arthas", "Little Red Riding Hood", "Arthur"};
    ArrayList<ListObject> testListOfEvents;
    TimePool timePool = new TimePool();

    @Before
    public void initScheduleTest() {
        planner = new PlannerClass();
        testListOfEvents = new ArrayList<ListObject>();
        for (String title : testListOfEventTitles) {
            testListOfEvents.add(new ListObject(title));
        }
    }

    @Test
    public void generateScheduleTest() {
        ArrayList<ListObject> mixedList = (ArrayList<ListObject>) testListOfEvents.clone();

        Calendar dates = Calendar.getInstance();
        dates.setTimeInMillis(timePool.current.getTimeInMillis());
        dates.add(Calendar.DATE, 1);
        dates.set(Calendar.HOUR_OF_DAY, 9);
        dates.set(Calendar.MINUTE, 0);

        // Set some mock properties
        // Hansel
        mixedList.get(0).initPlannedCal();
        mixedList.get(0).setPlanned(true);
        mixedList.get(0).setStartMs(dates.getTimeInMillis());
        dates.add(Calendar.HOUR_OF_DAY, 2);
        mixedList.get(0).setEndMs(dates.getTimeInMillis());

        // Gretel
        mixedList.get(1).setEstimate(30);

        // Ashking
        mixedList.get(2).initDeadlineCal();
        mixedList.get(2).setDeadline(true);
        mixedList.get(2).setDeadlineDate(2017, 5, 3, 22, 0);
        mixedList.get(2).setEstimate(93);

        // Arthas
        mixedList.get(3).initPlannedCal();
        mixedList.get(3).setPlanned(true);
        dates.add(Calendar.WEEK_OF_YEAR, 1);
        dates.set(Calendar.HOUR_OF_DAY, 10);
        mixedList.get(3).setStartMs(dates.getTimeInMillis());
        dates.add(Calendar.HOUR_OF_DAY, 1);
        mixedList.get(3).setEndMs(dates.getTimeInMillis());

        // Riding hood
        mixedList.get(4).initDeadlineCal();
        mixedList.get(4).setDeadline(true);
        mixedList.get(4).setDeadlineDate(2017, 4, 30, 22, 0);
        mixedList.get(4).setEstimate(120);

        // Arthur
        mixedList.get(5).setEstimate(45);
        mixedList.get(5).setDeadline(false);
        mixedList.get(5).setPlanned(false);

        ArrayList<ScheduleObject> listOfScheduledObjects = planner.generateSchedule(mixedList);
        System.out.println("Mixed events");
        Schedule.printSchedule(listOfScheduledObjects);


        ArrayList<ListObject> unplannedList = new ArrayList<>();

        ListObject object1 = new ListObject();
        object1.setTitle("object1");
        object1.setEstimate(30);
        object1.setDeadline(false);
        object1.setPlanned(false);
        unplannedList.add(object1);

        ListObject object2 = new ListObject();
        object2.setTitle("object2");
        object2.setEstimate(45);
        object2.setDeadline(false);
        object2.setPlanned(false);
        unplannedList.add(object2);

        ListObject object3 = new ListObject();
        object3.setTitle("object3");
        object3.setEstimate(60);
        object3.setDeadline(false);
        object3.setPlanned(false);
        unplannedList.add(object3);

        ListObject object4 = new ListObject();
        object4.setTitle("object4");
        object4.setEstimate(70);
        object4.setDeadline(false);
        object4.setPlanned(false);
        unplannedList.add(object4);

        ListObject object5 = new ListObject();
        object5.setTitle("object5");
        object5.setEstimate(90);
        object5.setDeadline(false);
        object5.setPlanned(false);
        unplannedList.add(object5);

        ListObject object6 = new ListObject();
        object6.setTitle("object6");
        object6.setEstimate(80);
        object6.setDeadline(false);
        object6.setPlanned(false);
        unplannedList.add(object6);

        ListObject object7 = new ListObject();
        object7.setTitle("object7");
        object7.setEstimate(15);
        object7.setDeadline(false);
        object7.setPlanned(false);
        unplannedList.add(object7);

        listOfScheduledObjects = planner.generateSchedule(unplannedList);
        System.out.println("Unplanned events");
        Schedule.printSchedule(listOfScheduledObjects);

        ArrayList<ListObject> deadlineOnlyEvents = new ArrayList<>();

        dates.set(Calendar.HOUR_OF_DAY, 19);
        ListObject deadLine1 = new ListObject("deadline1", "deadline");
        deadLine1.setEstimate(40);
        deadLine1.setDeadlineMs(dates.getTimeInMillis());
        deadlineOnlyEvents.add(deadLine1);

        dates.set(Calendar.HOUR_OF_DAY, 10);
        ListObject deadLine2 = new ListObject("deadline2", "deadline");
        deadLine2.setEstimate(120);
        deadLine2.setDeadlineMs(dates.getTimeInMillis());
        deadlineOnlyEvents.add(deadLine2);

        dates.add(Calendar.DATE, 1);
        dates.set(Calendar.HOUR_OF_DAY, 10);
        ListObject deadLine3 = new ListObject("deadline3", "deadline");
        deadLine3.setEstimate(93);
        deadLine3.setDeadlineMs(dates.getTimeInMillis());
        deadlineOnlyEvents.add(deadLine3);

        dates.set(Calendar.HOUR_OF_DAY, 15);
        ListObject deadLine4 = new ListObject("deadline3", "deadline");
        deadLine4.setEstimate(93);
        deadLine4.setDeadlineMs(dates.getTimeInMillis());
        deadlineOnlyEvents.add(deadLine4);

        /*
        for (ScheduleObject sO : listOfScheduledObjects) {
            System.out.println(sO.listObject.getTitle());
        }
        assertEquals(listOfScheduledObjects.size(), mixedList.size());
        assertFalse(listOfScheduledObjects.get(listOfScheduledObjects.size() - 1).listObject.getTitle()
                        != "Arthas");
        */

        listOfScheduledObjects = planner.generateSchedule(deadlineOnlyEvents);
        System.out.println("Deadline events");
        Schedule.printSchedule(listOfScheduledObjects);

    }

   @Test
    public void handlePlannedEvents() throws Exception {
       System.out.println("MANUAL TEST:");
       System.out.println("CHECK THAT THE FOLLOWING DATES REMOVES CORRESPONDING TIME SLOTS FROM TIMEPOOL");
       ListObject hansel = testListOfEvents.get(0);
       hansel.initPlannedCal();
       ListObject arthas = testListOfEvents.get(3);
       arthas.initPlannedCal();
       ListObject collideWithArthas = new ListObject("Collision", "planned");

       Calendar dates = timePool.current;

       // Hansel planned from 9-10
       TimePool testTimePool = new TimePool();
       dates.add(Calendar.DATE, 1);
       dates.set(Calendar.HOUR_OF_DAY, 9);
       dates.set(Calendar.MINUTE, 0);
       hansel.setStartMs(dates.getTimeInMillis());
       dates.add(Calendar.HOUR_OF_DAY, 1);
       hansel.setEndMs(dates.getTimeInMillis());

       // Arthas planned from 9-13
       dates.add(Calendar.DATE, 1);
       dates.set(Calendar.HOUR_OF_DAY, 9);
       arthas.setStartMs(dates.getTimeInMillis());
       dates.add(Calendar.HOUR_OF_DAY, 4);
       arthas.setEndMs(dates.getTimeInMillis());

       dates.set(Calendar.HOUR_OF_DAY, 10);
       dates.set(Calendar.MINUTE, 0);
       collideWithArthas.setStartMs(dates.getTimeInMillis());
       dates.add(Calendar.HOUR_OF_DAY, 4);
       collideWithArthas.setEndMs(dates.getTimeInMillis());
       testListOfEvents.add(collideWithArthas);

       System.out.println("Readable dates, hansel: " + new Date(hansel.getStartMs()) + " - " + new Date(hansel.getEndMs()));
       System.out.println("Readable dates, arthas: " + new Date(arthas.getStartMs()) + " - " + new Date(arthas.getEndMs()));
       System.out.println("Readable dates, collideWithArthas: " + new Date(collideWithArthas.getStartMs()) + " - " + new Date(collideWithArthas.getEndMs()));


       testTimePool.trimToFit(hansel);
       testTimePool.trimToFit(arthas);
       testTimePool.trimToFit(collideWithArthas);
       PlannerUtil.chronoSort(testTimePool.timeBlocks);
       for (TimePool.TimeBlock t : testTimePool.timeBlocks) {
           Calendar test = Calendar.getInstance();
           test.setTimeInMillis(t.startTime());
           System.out.print(test.get(Calendar.DATE) + "/" + (test.get(Calendar.MONTH) + 1) + ", "
                   + test.get(Calendar.HOUR_OF_DAY) + ":" + (test.get(Calendar.MINUTE) + 1) + " - ");
           test.setTimeInMillis(t.endTime());
           System.out.println(test.get(Calendar.HOUR_OF_DAY) + ":" + test.get(Calendar.MINUTE));
       }
    }

    @Test
    public void filterListTests() {
        ArrayList<ListObject> clonedList = (ArrayList<ListObject>) testListOfEvents.clone();
        for (int i = 0; i < testListOfEventTitles.length - 1; i++) {
            assertEquals(testListOfEvents.get(i).getTitle(), clonedList.get(i).getTitle());
            assertNotSame(testListOfEvents.get(i).getTitle(), clonedList.get(i + 1).getTitle());
        }

        // Set some mock properties
        // Hansel
        clonedList.get(0).initPlannedCal();
        clonedList.get(0).setPlanned(true);
        clonedList.get(0).setStartDate(2017, 4, 28, 10, 0);
        clonedList.get(0).setEndDate(2017, 4, 28, 12, 0);

        // Gretel
        clonedList.get(1).setEstimate(30);

        // Ashking
        clonedList.get(2).initDeadlineCal();
        clonedList.get(2).setDeadline(true);
        clonedList.get(2).setDeadlineDate(2017, 5, 3, 22, 0);

        // Arthas
        clonedList.get(3).initPlannedCal();
        clonedList.get(3).setPlanned(true);
        clonedList.get(3).setStartDate(2017, 4, 29, 13, 0);
        clonedList.get(3).setEndDate(2017, 4, 29, 15, 0);

        // Riding hood
        clonedList.get(4).initDeadlineCal();
        clonedList.get(4).setDeadline(true);
        clonedList.get(4).setDeadlineDate(2017, 4, 30, 22, 0);

        // Arthur
        clonedList.get(5).setEstimate(60);

        // Test if filtering planned events works
        ArrayList<ListObject> plannedEvents;
        plannedEvents = planner.getPlannedEvents(clonedList);
        assertEquals(plannedEvents.size(), 2);
        assertEquals(clonedList.size(), 4);
        assertEquals(plannedEvents.get(0).getTitle(), testListOfEventTitles[0]);
        assertEquals(plannedEvents.get(1).getTitle(), testListOfEventTitles[3]);

        // Test same for deadline events
        ArrayList<ListObject> deadlineEvents;
        deadlineEvents = planner.getDeadlineEvents(clonedList);
        assertEquals(deadlineEvents.size(), 2);
        assertEquals(clonedList.size(), 2);
        assertEquals(deadlineEvents.get(0).getTitle(), testListOfEventTitles[2]);
        assertEquals(deadlineEvents.get(1).getTitle(), testListOfEventTitles[4]);
    }
}

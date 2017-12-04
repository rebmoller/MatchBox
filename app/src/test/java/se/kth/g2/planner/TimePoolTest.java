package se.kth.g2.planner;

import android.database.DatabaseErrorHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Suite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sieken on 2017-05-02.
 */
public class TimePoolTest {

    TimePool timePool;
    String[] testListOfEventTitles = {"Hansel", "Gretel", "Ashking", "Arthas", "Little Red Riding Hood", "Arthur"};
    ArrayList<ListObject> testListOfEvents;

    @Before
    public void setupTimePool() {
        timePool = new TimePool();
        testListOfEvents = new ArrayList<ListObject>();
        for (String title : testListOfEventTitles) {
            testListOfEvents.add(new ListObject(title));
        }

        Calendar dates = Calendar.getInstance();

        // Set some mock properties
        // Hansel, planned event, relative tomorrow
        testListOfEvents.get(0).initPlannedCal();
        testListOfEvents.get(0).setPlanned(true);
        testListOfEvents.get(0).setStartDate(dates.YEAR, dates.MONTH, dates.DAY_OF_MONTH + 1, 10, 00);
        testListOfEvents.get(0).setEndDate(dates.YEAR, dates.MONTH, dates.DAY_OF_MONTH + 1, 15, 00);

        // Gretel
        testListOfEvents.get(1).setEstimate(30);

        // Ashking
        testListOfEvents.get(2).initDeadlineCal();
        testListOfEvents.get(2).setDeadline(true);
        testListOfEvents.get(2).setDeadlineDate(2017, 5, 3, 22, 0);

        // Arthas
        testListOfEvents.get(3).initPlannedCal();
        testListOfEvents.get(3).setPlanned(true);
        testListOfEvents.get(3).setStartDate(2017, 4, 29, 13, 0);
        testListOfEvents.get(3).setEndDate(2017, 4, 29, 15, 0);

        // Riding hood
        testListOfEvents.get(4).initDeadlineCal();
        testListOfEvents.get(4).setDeadline(true);
        testListOfEvents.get(4).setDeadlineDate(2017, 4, 30, 22, 0);

        // Arthur
        testListOfEvents.get(5).setEstimate(60);
    }

    @Test
    public void sortByBlockSizeTest() throws Exception {
        TimePool testTimePool = new TimePool();
        int head;
        int tail = 0;
        testTimePool.timeBlocks.get(0).shrinkSize(30);
        testTimePool.timeBlocks.get(3).shrinkSize(15);
        testTimePool.timeBlocks.get(4).shrinkSize(10);
        PlannerUtil.sortByBlockSize(testTimePool.timeBlocks);
        for (head = 1; head < testTimePool.timeBlocks.size(); head++) {
            assertTrue(testTimePool.timeBlocks.get(head).size()
                    >= testTimePool.timeBlocks.get(tail).size());
            tail++;
        }
    }

    @Test
    public void chronoSortTest() throws Exception {
        TimePool testTimePool = new TimePool();
        int head;
        int tail = 0;
        PlannerUtil.sortByBlockSize(testTimePool.timeBlocks);
        PlannerUtil.chronoSort(testTimePool.timeBlocks);
        for (head = 1; head < testTimePool.timeBlocks.size(); head++) {
            assertTrue(testTimePool.timeBlocks.get(head).startTime()
            > testTimePool.timeBlocks.get(tail).startTime());
        }
    }

    @Test
    public void trimToFitTest() throws Exception {
        System.out.println("MANUAL TEST:");
        System.out.println("CHECK THAT THE FOLLOWING DATES REMOVES CORRESPONDING TIME SLOTS FROM TIMEPOOL");
        ListObject hansel = testListOfEvents.get(0);
        ListObject arthas = testListOfEvents.get(3);
        ListObject collideWithArthas = new ListObject("Collision", "planned");

        Calendar dates = timePool.current;

        TimePool testTimePool = new TimePool();
        dates.add(Calendar.DATE, 1);
        dates.set(Calendar.HOUR_OF_DAY, 9);
        dates.set(Calendar.MINUTE, 0);
        hansel.setStartMs(dates.getTimeInMillis());
        dates.add(Calendar.HOUR_OF_DAY, 1);
        hansel.setEndMs(dates.getTimeInMillis());

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
            System.out.print(test.get(Calendar.DATE) + "/" + test.get(Calendar.MONTH) + ", "
            + test.get(Calendar.HOUR_OF_DAY) + ":" + test.get(Calendar.MINUTE) + " - ");
            test.setTimeInMillis(t.endTime());
            System.out.println(test.get(Calendar.HOUR_OF_DAY) + ":" + test.get(Calendar.MINUTE));
        }
    }

    @Test
    public void addTime() throws Exception {
        assertTrue(timePool.timeBlocks.size() > 0);
        Calendar firstBlock = Calendar.getInstance();
        firstBlock.setTimeInMillis(timePool.timeBlocks.get(0).startTime());
        if (timePool.current.get(Calendar.HOUR_OF_DAY) > timePool.restrictions.end) {
            assertEquals(timePool.current.get(Calendar.DAY_OF_MONTH), firstBlock.get(Calendar.DAY_OF_MONTH) - 1);
        } else {
            assertEquals(timePool.current.get(Calendar.DATE), firstBlock.get(Calendar.DATE));
        }
       for (int i = 0; i < timePool.timeBlocks.size() - 1; i++) {
           assertTrue(timePool.timeBlocks.get(i) != null);
           assertTrue(timePool.timeBlocks.get(i).size() > 0);
           assertNotEquals(timePool.timeBlocks.get(i), timePool.timeBlocks.get(i+1));
        }
        assertEquals(timePool.startOfNext.get(Calendar.WEEK_OF_YEAR) - 1,
                timePool.current.get(Calendar.WEEK_OF_YEAR));
        assertEquals(timePool.startOfNext.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
    }
}
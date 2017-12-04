package se.kth.g2.planner;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by sieken on 2017-05-02.
 */
public class TimeBlockTest {

    TimePool timePool;
    ArrayList<TimePool.TimeBlock> timeBlocks;
    TimePool.TimeBlock testBlock;

    @Before
    public void setupTimeBlocks() {
        timePool = new TimePool();
        timeBlocks = timePool.timeBlocks;
        if (timeBlocks.size() < 1) {
            System.out.println("Apparently not enough time");
            timePool.addTime();
        }
        testBlock = timeBlocks.get(0);
        System.out.println("Testblock original start time: " + new Date(testBlock.startTime()));
    }

    @Test
    public void shrinkSize() throws Exception {
        int testSize = testBlock.size();
        testBlock.shrinkSize(15);
        assertEquals(testBlock.size(), testSize - 15);
        testBlock.shrinkSize(-15);
        assertEquals(testBlock.size(), testSize);
    }

    @Test
    public void setEnd() throws Exception {
        int testSize = testBlock.size();
        long realEnd = testBlock.endTime();
        long testEnd = testBlock.endTime() - (15*60*1000);
        testBlock.setEnd(testEnd);
        assertEquals(testBlock.size(), testSize - 15);
        testBlock.setEnd(realEnd);
        assertEquals(testBlock.size(), testSize);
    }

    @Test
    public void shiftStartTimeByMinutes() throws Exception {
        int testSize = testBlock.size();
        long realStart = testBlock.startTime();
        long realEnd = testBlock.endTime();
        testBlock.shiftStartTimeByMinutes(15);
        assertEquals(testBlock.startTime(), realStart + 15*60*1000);
        assertEquals(testBlock.endTime(), realEnd + 15*60*1000);
        assertEquals(testBlock.size(), testSize);
        testBlock.shiftStartTimeByMinutes(-15);
        assertEquals(testBlock.startTime(), realStart);
        assertEquals(testBlock.endTime(), realEnd);
        assertEquals(testBlock.size(), testSize);
    }

    @Test
    public void deadLineMatchesTest() throws Exception {
        Calendar dates = Calendar.getInstance();
        dates.setTime(timePool.current.getTime());
        ListObject testDeadlineObject = new ListObject("Sylvanas", "deadline");
        dates.add(Calendar.DATE, -1);
        dates.set(Calendar.HOUR_OF_DAY, 21);
        testDeadlineObject.setDeadlineMs(dates.getTimeInMillis());
        assertFalse(testBlock.deadLineMatches(testDeadlineObject));
        dates.add(Calendar.DATE, 2);
        testDeadlineObject.setDeadlineMs(dates.getTimeInMillis());
        assertTrue(testBlock.deadLineMatches(testDeadlineObject));
    }

    @Test
    public void split() throws Exception {
        long splitTime = testBlock.startTime() + (testBlock.endTime() - testBlock.startTime())/2;
        long originalStartTime = testBlock.startTime();
        long originalEndTime = testBlock.endTime();
        TimePool.TimeBlock remainingPostSplit = testBlock.split(splitTime);
        assertEquals(testBlock.startTime(), originalStartTime);
        assertEquals(remainingPostSplit.startTime(), splitTime);
        assertTrue(testBlock.size() > 0 && remainingPostSplit.size() > 0);
        assertEquals(testBlock.size(), (int) ((splitTime - originalStartTime)/60000));
        assertEquals(remainingPostSplit.size(), (int) ((originalEndTime - splitTime)/60000));
    }
}
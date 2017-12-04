package se.kth.g2.planner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Elias on 2017-04-20.
 * All methods in ListObject.java seem to be working as intended,
 * generating all the expected responses. (2017-04-20 Elias)
 */

@RunWith(MockitoJUnitRunner.class)
public class ListObject_Test {

    ListObject testListObject1;
    ListObject testListObject2;
    ListObject testListObject3;
    ListObject testListObject4;
    ListObject testListObject5;

    Calendar copyStartCal;
    Calendar copyEndCal;
    Calendar copyDeadlineCal;

    Calendar exampleCal;
    Calendar exampleStart;
    Calendar exampleEnd;
    Calendar exampleDeadline;

    Calendar testGetStart;
    Calendar testGetEnd;
    Calendar testGetDeadline;

    @Before
    public void testNormalEvent() {
        testListObject1 = new ListObject("Test1");
        testListObject1.setTitle("Testet1");

        testListObject1.setDeadline(false);
        testListObject1.setPlanned(true);
    }

    @Before
    public void testPlannedEvent() {
        testListObject2 = new ListObject("Test2", "planned");
        testListObject2.setEstimate(120);
        testListObject2.setStartDate(2017, 4, 20, 13, 0);
        testListObject2.setEndDate(2017, 6, 21, 14, 30);

        copyStartCal = testListObject2.getListObjectStart();
        copyEndCal = testListObject2.getListObjectEnd();

        exampleStart = testListObject2.getListObjectStart();
        exampleEnd = testListObject2.getListObjectEnd();
    }

    @Before
    public void testDeadlineEvent() {
        testListObject3 = new ListObject("Test3", 120, "deadline");
        testListObject3.setDeadlineDate(2018, 1, 12, 23, 5);

        exampleCal = Calendar.getInstance();
        exampleCal.set(2018, 1, 12, 23, 5);

        exampleDeadline = testListObject3.getListObjectDeadline();

        copyDeadlineCal = testListObject3.getListObjectDeadline();
    }

    @Before
    public void testSetsGets() {
        testListObject4 = new ListObject();
        testListObject4.setListObjectStart(Calendar.getInstance());

        testListObject4.setStartMs(50);

        testListObject5 = new ListObject();

        testGetStart = Calendar.getInstance();
        testGetEnd = Calendar.getInstance();
        testGetDeadline = Calendar.getInstance();

        testListObject5.setListObjectStart(testGetStart);
        testListObject5.setListObjectEnd(testGetEnd);
        testListObject5.setListObjectDeadline(testGetDeadline);
    }

   @Test
    public void test1 () {
        // assertEquals("Testetet", testListObject1.getTitle()); // fail
        assertEquals("Testet1", testListObject1.getTitle());

        // assertEquals(true, testListObject1.hasDeadline()); // fail
        assertEquals(false, testListObject1.hasDeadline()); // pass

        // assertEquals(false, testListObject1.isPlanned()); // fail
        assertEquals(true, testListObject1.isPlanned()); // pass
    }

    @Test
    public void test2 () {
        // assertEquals("Test", testListObject2.getTitle());
        assertEquals("Test2", testListObject2.getTitle());

        // assertEquals(60, testListObject2.getEstimate()); // fail
        assertEquals(120, testListObject2.getEstimate()); // pass

        // assertEquals(false, testListObject2.isPlanned()); // fail;
        assertEquals(true, testListObject2.isPlanned()); // pass

        // assertEquals(true, testListObject2.hasDeadline()); // fail
        assertEquals(false, testListObject2.hasDeadline()); // pass

        // assertEquals(2016, testListObject2.getStartYear()); // fail
        assertEquals(2017, testListObject2.getStartYear()); // pass

        // assertEquals(2016, testListObject2.getEndYear()); // fail
        assertEquals(2017, testListObject2.getEndYear()); // pass

        // assertEquals(0, testListObject2.getStartMonth()); // fail
        assertEquals(4, testListObject2.getStartMonth()); // pass

        // assertEquals(19, testListObject2.getStartDay()); // fail
        assertEquals(20, testListObject2.getStartDay()); // pass

        // assertEquals(12, testListObject2.getStartTime()); // fail
        assertEquals(13, testListObject2.getStartHour()); // pass

        // assertEquals(01, testListObject2.getStartMinute()); // fail
        assertEquals(0, testListObject2.getStartMinute()); // pass

        // assertEquals(2016, testListObject2.getEndYear()); // fail
        assertEquals(2017, testListObject2.getEndYear()); // pass

        // assertEquals(7, testListObject2.getEndMonth()); // fail
        assertEquals(6, testListObject2.getEndMonth()); // pass

        // assertEquals(20, testListObject2.getEndDay()); // fail
        assertEquals(21, testListObject2.getEndDay()); // pass

        // assertEquals(13, testListObject2.getEndHour()); // fail
        assertEquals(14, testListObject2.getEndHour()); // pass

        // assertEquals(25, testListObject2.getEndMinute()); // fail
        assertEquals(30, testListObject2.getEndMinute()); // pass

        // assertEquals(5, testListObject2.getStartMs()); // fail
        assertEquals(copyStartCal.getTimeInMillis(), testListObject2.getStartMs()); // pass

        // assertEquals(29, testListObject2.getEndMs()); // fail
        assertEquals(copyEndCal.getTimeInMillis(), testListObject2.getEndMs()); // pass

        // assertEquals(12, testListObject2.getDeadlineMs()); // fail
        assertEquals(copyDeadlineCal.getTimeInMillis(), testListObject3.getDeadlineMs()); // pass

        // testListObject3.setDeadlineDate(2018, 1, 12, 23, 05);
        assertEquals(exampleCal.getTimeInMillis(), testListObject3.getDeadlineMs());

    }

    @Test
    public void test3 () {
        // assertEquals(2017, testListObject3.getDeadlineYear()); // fail
        assertEquals(2018, testListObject3.getDeadlineYear()); // pass

        // assertEquals(2, testListObject3.getDeadlineMonth()); // fail
        assertEquals(1, testListObject3.getDeadlineMonth()); // pass

        // assertEquals(21, testListObject3.getDeadlineDay()); // fail
        assertEquals(12, testListObject3.getDeadlineDay()); // pass

        // assertEquals(12, testListObject3.getDeadlineHour()); // fail
        assertEquals(23, testListObject3.getDeadlineHour()); // pass

        // assertEquals(21, testListObject3.getDeadlineMinute()); // fail
        assertEquals(5, testListObject3.getDeadlineMinute()); // pass

        // assertEquals(false, testListObject3.hasDeadline()); // fail
        assertEquals(true, testListObject3.hasDeadline()); // pass

        // assertEquals(true, testListObject3.isPlanned()); // fail
        assertEquals(false, testListObject3.isPlanned()); // pass

        // assertEquals(copyEndCal, testListObject2.getListObjectStart()); // fail
        assertEquals(copyStartCal, testListObject2.getListObjectStart()); // pass

        // assertEquals(copyEndCal, testListObject3.getListObjectDeadline()); // fail
        assertEquals(copyDeadlineCal, testListObject3.getListObjectDeadline()); // pass

        // assertEquals(exampleCal, testListObject2.getListObjectStart()); // fail
        assertEquals(exampleStart, testListObject2.getListObjectStart()); // pass

        // assertEquals(exampleCal, testListObject2.getListObjectEnd()); // fail
        assertEquals(exampleEnd, testListObject2.getListObjectEnd()); // pass

        // assertEquals(exampleCal, testListObject3.getListObjectDeadline()); // fail
        assertEquals(exampleDeadline, testListObject3.getListObjectDeadline()); // pass

        // assertEquals(29, testListObject4.getStartMs()); // fail
        assertEquals(50, testListObject4.getStartMs()); // pass

        // assertEquals(exampleEnd, testListObject5.getListObjectStart()); // fail
        assertEquals(testGetStart, testListObject5.getListObjectStart()); // pass

        // assertEquals(exampleStart, testListObject5.getListObjectEnd()); // fail
        assertEquals(testGetEnd, testListObject5.getListObjectEnd()); // pass

        // assertEquals(exampleEnd, testListObject5.getListObjectDeadline()); // fail
        assertEquals(testGetDeadline, testListObject5.getListObjectDeadline()); // pass
    }
}


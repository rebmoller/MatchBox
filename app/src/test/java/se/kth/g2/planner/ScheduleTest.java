package se.kth.g2.planner;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Anna on 2017-05-04.
 */

/* Unit-test for Schedule, modified ScheduleObject to only receive
* a listObject. Need to be integrated with TimePool when it is up
* and running. */
public class ScheduleTest {
    @Test
    public void addEvent() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60, "planned");
        ListObject e2 = new ListObject("JENS SUGER", 120, "planned");
        e1.setStartDate(2016,0,7,15,55);
        e2.setStartDate(2017,0,5,16,55);
        ScheduleObject s1 = new ScheduleObject(e1);
        ScheduleObject s2 = new ScheduleObject(e2);
        ArrayList<ScheduleObject> events = new ArrayList<>();

        Schedule.addEvent(s2, events);
        Schedule.addEvent(s1, events);

        System.out.println("TITLE OF EVENT:" + " " + events.get(0).listObject.getTitle());
        System.out.println("WEEK OF YEAR:" + " " + events.get(0).getWeekOfYear());
        System.out.println();
        System.out.println("TITLE OF EVENT:" + " " + events.get(1).listObject.getTitle());
        System.out.println("WEEK OF YEAR:" + " " + events.get(1).getWeekOfYear());


    }

    @Test
    public void printSchedule() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60, "planned");
        ListObject e2 = new ListObject("JENS SUGER", 120, "planned");
        e1.setStartDate(2016,0,7,15,55);
        e2.setStartDate(2017,0,5,16,55);
        ScheduleObject s1 = new ScheduleObject(e1);
        ScheduleObject s2 = new ScheduleObject(e2);
        ArrayList<ScheduleObject> events = new ArrayList<>();

        Schedule.addEvent(s2, events);
        Schedule.addEvent(s1, events);
        Schedule.printSchedule(events);


    }
}
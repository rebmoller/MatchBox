package se.kth.g2.planner;
import java.util.ArrayList;

import java.util.ArrayList;

/**
 * Created by Anna on 2017-05-04.
 */

public class Schedule {

    /* Adds event to the event list, list gets sorted by scheduleSortByMillis
    * in increasing order */

    public static void addEvent(ScheduleObject event, ArrayList<ScheduleObject> events){
        events.add(event);
        PlannerUtil.scheduleSortByMillis(events);
    }

    /* Prints out the schedule, events within the same day
    will be formatted as follows:

    YEAR: int
    MONTH: int
    DAY: int

    EVENT: String
    START OF EVENT: Calendar Date
    UNTIL: Calendar Date
    :
    * */
    public static void printSchedule(ArrayList<ScheduleObject> events){
        for(int i = 0; i < events.size(); i++){
            if(i == 0){
                System.out.println("YEAR:" + " " + events.get(i).getStartYear());
                System.out.println("MONTH:" + " " + events.get(i).getStartMonth());
                System.out.println("DAY:" + " " + events.get(i).getStartDay());
                System.out.println();
            }
            else{
                if(events.get(i - 1).getStartDay() == events.get(i).getStartDay()
                        && events.get(i-1).getWeekOfYear() == events.get(i).getWeekOfYear()
                        && events.get(i-1).getStartYear() == events.get(i).getStartYear()){
                }
                else{
                    System.out.println("YEAR:" + " " + events.get(i).getStartYear());
                    System.out.println("MONTH:" + " " + events.get(i).getStartMonth());
                    System.out.println("DAY:" + " " + events.get(i).getStartDay());
                    System.out.println();
                }
            }
            System.out.println("EVENT:" + " " + events.get(i).listObject.getTitle());
            System.out.println("START OF EVENT:" + " " + events.get(i).scheduledStartTime.getTime());
            System.out.println("UNTIL:" + " " +  events.get(i).scheduledEndTime.getTime());
            System.out.println();
        }
    }
}

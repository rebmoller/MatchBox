package se.kth.g2.planner;

import java.util.Calendar;

/**
 * Created by David Jacobsson.
 * Modified by Love Stark
 */

public class DatabaseContract {
    /**
     * STATIC CONSTANTS
     */
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "userobjects.db";

    /**
     * OBJECT CONSTANTS
     */
    public static final String TEXT_TYPE = "TEXT";
    public static final String INT_TYPE = "INTEGER";
    public static final String COMMA_SEP = ",";

    public static final String LIST_TABLE_NAME = "list_table";
    public static final String LIST_COL_ID = "_id";
    public static final String LIST_COL_TITLE = "title";
    public static final String LIST_COL_ESTIMATE = "estimate";
    public static final String LIST_COL_IMPORTANCE = "importanceLevel";
    public static final String LIST_COL_ISIMPORTANT = "isImportant";
    public static final String LIST_COL_ISIMPORTED = "isImported";
    public static final String LIST_COL_ISPLANNED = "isPlanned";
    public static final String LIST_COL_HASDEADLINE = "hasDeadline";
    public static final String LIST_COL_START = "listObjectStart";
    public static final String LIST_COL_END = "listObjectEnd";
    public static final String LIST_COL_DEADLINE = "listObjectDeadline";

    public static final String SCHEDULE_TABLE_NAME = "schedule_table";
    public static final String SCHEDULE_COL_ID = "_id";
    public static final String SCHEDULE_COL_SIZE = "scheduleObjectSize";
    public static final String SCHEDULE_COL_START = "scheduleObjectStartMS";
    public static final String SCHEDULE_COL_END = "scheduleObjectEndMS";

    public static final String CREATE_LIST_TABLE = "CREATE TABLE " + LIST_TABLE_NAME + " (" +
            LIST_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LIST_COL_TITLE + " TEXT, " +
            LIST_COL_ESTIMATE + " INTEGER, " +
            LIST_COL_IMPORTANCE + " INTEGER, " +
            LIST_COL_ISIMPORTANT + " INTEGER, " +
            LIST_COL_ISIMPORTED + " INTEGER, " +
            LIST_COL_ISPLANNED + " BOOL, " +
            LIST_COL_HASDEADLINE + " BOOL, " +
            LIST_COL_START + " INTEGER, " +
            LIST_COL_END + " INTEGER, " +
            LIST_COL_DEADLINE + " INTEGER " +
            ");";
    public static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + SCHEDULE_TABLE_NAME + " (" +
            SCHEDULE_COL_ID + " INTEGER, " +
            SCHEDULE_COL_SIZE + " INTEGER, " +
            SCHEDULE_COL_START + " INTEGER, " +
            SCHEDULE_COL_END + " INTEGER " +
            ");";

    /**
     * Contructor
     */
    private DatabaseContract(){}

    /**
     * MOCK OBJECTS
     */
    public static ListObject mockListObject1(){
        ListObject object = new ListObject();
        object.setTitle("Sprint Demo");
        object.setEstimate(15);
        object.setImportanceLevel(3);
        object.setImportance(true);
        object.setImported(false);
        object.setPlanned(false);
        object.setDeadline(true);
        /**listObjectStart -> null **/
        /**listObjectEnd -> null **/
        object.initDeadlineCal();
        object.setDeadlineDate(2017, 4, 25, 10, 0);
        return object;
    }
    public static ListObject mockListObject2(){
        ListObject object = new ListObject();
        object.setTitle("Meeting");
        object.setEstimate(60);
        object.setImportanceLevel(2);
        object.setImportance(true);
        object.setImported(false);
        object.setPlanned(true);
        object.setDeadline(false);
        object.initPlannedCal();
        object.setStartDate(1974,5,30,15,30);
        object.setEndDate(1974, 5, 30, 16, 30);
        /**listObjectDeadline -> null **/
        return object;
    }
    public static ListObject mockListObject3(){
        ListObject object = new ListObject();
        object.setTitle("Date");
        object.setEstimate(180);
        object.setImportanceLevel(1);
        object.setImportance(true);
        object.setImported(false);
        object.setPlanned(true);
        object.setDeadline(false);
        object.initPlannedCal();
        object.setStartDate(2017,4,28,21,0);
        object.setEndDate(2017, 04, 29, 2, 30);
        /**listObjectDeadline -> null **/
        return object;
    }
    public static ListObject mockListObject4(){
        ListObject object = new ListObject();
        object.setTitle("Application");
        object.setEstimate(20);
        object.setImportanceLevel(0);
        object.setImportance(false);
        object.setImported(false);
        object.setPlanned(false);
        object.setDeadline(true);
        /**listObjectStart -> null **/
        /**listObjectEnd -> null **/
        object.initDeadlineCal();
        object.setDeadlineDate(2017, 4, 15, 10, 0);
        return object;
    }

    public static ScheduleObject mockScheduleObject1(){
        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();
        ListObject aba = mockListObject2();
        aba.setID(1);
        ScheduleObject object = new ScheduleObject(aba, tb);
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 4, 25, 10, 0);
    //    object.setID(1);
        object.setSize(10);
   //     object.setComparableStartTime(cal.getTimeInMillis());
        cal.set(2016, 4, 26, 10, 0);
     //   object.setComparableEndTime(cal.getTimeInMillis());

        return object;
    }

    public static ScheduleObject mockScheduleObject2(){
        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();
        ListObject aba = mockListObject2();
        aba.setID(2);
        ScheduleObject object = new ScheduleObject(aba, tb);
        Calendar cal = Calendar.getInstance();
        cal.set(1999, 1, 25, 10, 0);
    //    object.setID(2);
        object.setSize(55);
        object.setComparableStartTime(cal.getTimeInMillis());
        cal.set(1999, 5, 26, 10, 0);
        object.setComparableEndTime(cal.getTimeInMillis());

        return object;
    }

    public static ScheduleObject mockScheduleObject3(){
        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();
        ListObject aba = mockListObject2();
        aba.setID(3);
        ScheduleObject object = new ScheduleObject(aba, tb);
        Calendar cal = Calendar.getInstance();
        cal.set(1975, 4, 28, 10, 0);
  //      object.setID(11);
        object.setSize(23);
   //     object.setComparableStartTime(cal.getTimeInMillis());
        cal.set(1976, 4, 29, 10, 30);
   //     object.setComparableEndTime(cal.getTimeInMillis());

        return object;
    }

    public static ScheduleObject mockScheduleObject4(){
        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();
        ListObject aba = mockListObject2();
        aba.setID(4);
        ScheduleObject object = new ScheduleObject(aba, tb);
        Calendar cal = Calendar.getInstance();
        cal.set(2003, 4, 25, 10, 10);
  //      object.setID(10000123);
        object.setSize(1022);
   //     object.setComparableStartTime(cal.getTimeInMillis());
        cal.set(2003, 4, 26, 10, 10);
   //     object.setComparableEndTime(cal.getTimeInMillis());

        return object;
    }




}

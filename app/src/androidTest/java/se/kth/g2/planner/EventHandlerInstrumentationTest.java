package se.kth.g2.planner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;


import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.util.ArrayList;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;


/**
 * Created by J2DX on 2017-04-28.
 */
@RunWith(AndroidJUnit4.class)
public class EventHandlerInstrumentationTest {
    /**
     * GLOBAL POINTERS
     */
    private PlannerDBMS plannerDBMS;
    private SQLiteDatabase database;
    private Cursor cursor;
    private Context context;
    private EventHandler eventHandler;
    private ListObject[] expectedListObjects;
    private ScheduleObject[] expectedScheduleObjects;

    /**
     * Database setup method,
     * Runs BEFORE EACH execution of a test method
     */
    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DatabaseContract.DATABASE_NAME);
        eventHandler = EventHandler.getInstance();
        eventHandler.init(context);

        plannerDBMS = new PlannerDBMS(context);
        database = plannerDBMS.getWritableDatabase();

        expectedListObjects = new ListObject[]{
            DatabaseContract.mockListObject1(),
            DatabaseContract.mockListObject2(),
            DatabaseContract.mockListObject3(),
            DatabaseContract.mockListObject4()
        };
        expectedScheduleObjects = new ScheduleObject[]{
            DatabaseContract.mockScheduleObject1(),
            DatabaseContract.mockScheduleObject2(),
            DatabaseContract.mockScheduleObject3(),
            DatabaseContract.mockScheduleObject4()
        };
    }

    /**
     * Teardown method
     * Runs AFTER EACH execution of a test method
     */
    @After
    public void tearDown() throws Exception {
        database.close();
        plannerDBMS.close();
    }

    @Test
    public void testInit(){
        ArrayList<ListObject> loadOutList;
        ArrayList<ScheduleObject> loadOutSchedule;
        loadOutList = eventHandler.getListObjectsEvent();
        loadOutSchedule = eventHandler.generateScheduleObjectList();
        assertEquals(0, loadOutList.size());
        assertEquals(0, loadOutSchedule.size());

        for(ListObject expectedObject : expectedListObjects){
            expectedObject.setID(eventHandler.addListItemEvent(expectedObject));
        }
        for(ScheduleObject expectedObject : expectedScheduleObjects){
            eventHandler.addScheduleItemEvent(expectedObject);
        }

        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedScheduleObjects.length, cursor.getCount());
        loadOutSchedule = plannerDBMS.fetchScheduleTable();
        assertEquals(expectedScheduleObjects.length, loadOutSchedule.size());

        eventHandler.syncDatabaseTablesEvent();
        loadOutList = eventHandler.getListObjectsEvent();
        assertEquals(expectedListObjects.length, loadOutList.size());
        loadOutSchedule = eventHandler.generateScheduleObjectList();
        assertEquals(expectedScheduleObjects.length, loadOutSchedule.size());

        int arrayPointer = 0;
        for(ListObject expectedListObject : loadOutList){
            assertEquals(expectedListObject, loadOutSchedule.get(arrayPointer).getListObject());
            arrayPointer++;
        }
    }

    @Test
    public void testGetInstance() throws Exception {
        /**
         * Test that class is a singleton
         */
        EventHandler ehTest = EventHandler.getInstance();
        assertEquals(eventHandler, ehTest);
    }

    @Test
    public void testAddListItemEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         */
        for(ListObject o: expectedListObjects){
            o.setID(eventHandler.addListItemEvent(o));
        }
        /**
         * Make query to database
         * Check that number of entries and all members match inserted mock objects
         */
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertEquals(expectedListObjects.length, cursor.getCount());
        cursor.moveToFirst();

        for(ListObject o : expectedListObjects){
            assertEquals(o.getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_ID)));
            assertEquals(o.getTitle(), cursor.getString(cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE)));
            assertEquals(o.getEstimate(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ESTIMATE)));

            if(o.getIsImportant() == true){
                assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
                assertEquals(o.getImportanceLevel(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_IMPORTANCE)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
            }
            if(o.getIsImported() == true){
                assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
            }
            if(o.getIsPlanned() == true){
                assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
            }
            if(o.getHasDeadline() == true){
                assertEquals(1 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
            } else {
                assertEquals(0 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
            }

            if(o.getListObjectStart() != null){
                assertEquals(o.getStartMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_START)));
            }
            if(o.getListObjectEnd() != null){
                assertEquals(o.getEndMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_END)));
            }
            if(o.getListObjectDeadline() != null){
                assertEquals(o.getDeadlineMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE)));
            }
            cursor.moveToNext();
        }
    }

    @Test
    public void testAddScheduleItemEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         */
        for(ScheduleObject o: expectedScheduleObjects){
            eventHandler.addScheduleItemEvent(o);
        }
        /**
         * Make query to database
         * Check that number of entries and all members match inserted mock objects
         */
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedScheduleObjects.length, cursor.getCount());
        cursor.moveToFirst();

        for(ScheduleObject o : expectedScheduleObjects){
            assertEquals(o.getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
            assertEquals(o.getSize(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));

            if(o.getScheduledStartTime() != null){
                assertEquals(o.getComparableStartTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
            }
            if(o.getScheduledEndTime() != null){
                assertEquals(o.getComparableEndTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));
            }
            cursor.moveToNext();
        }
    }

    @Test
    public void testRemoveListItemEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         * Set ID's for mock objects
         */
        for(ListObject o: expectedListObjects){
            o.setID(eventHandler.addListItemEvent(o));
        }

        /**
         * Check that number of entries match number of mock objects
         */
        int numOfEntries = expectedListObjects.length;
        String sqlQuery = "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME;
        String sqlQueryByID = "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME + " WHERE " + DatabaseContract.LIST_COL_ID + " = ?";
        cursor = database.rawQuery(sqlQuery, null);
        assertEquals(numOfEntries, cursor.getCount());

        /**
         * Remove entries and check that ONLY expected entries dissappear
         */

        for(ListObject o : expectedListObjects){
            eventHandler.removeListItemEvent(o);
            numOfEntries--;
            cursor = database.rawQuery(sqlQuery, null);
            assertEquals(numOfEntries, cursor.getCount());
            cursor = database.rawQuery(sqlQueryByID, new String[]{Long.toString(o.getID())});
            assertEquals(0, cursor.getCount());
        }
    }

    @Test
    public void testRemoveScheduleItemEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         */
        for(ScheduleObject o: expectedScheduleObjects){
            eventHandler.addScheduleItemEvent(o);
        }
        /**
         * Make query to database
         * Check that number of entries and all members match inserted mock objects
         */
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedScheduleObjects.length, cursor.getCount());

        /**
         * Check that number of entries match number of mock objects
         */
        int numOfEntries = expectedScheduleObjects.length;
        String sqlQuery = "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME;
        String sqlQueryByID = "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME + " WHERE " + DatabaseContract.SCHEDULE_COL_ID + " = ?";
        cursor = database.rawQuery(sqlQuery, null);
        assertEquals(numOfEntries, cursor.getCount());

        /**
         * Remove entries and check that ONLY expected entries dissappear
         */
        for(ScheduleObject o : expectedScheduleObjects){
            eventHandler.removeScheduleItemEvent(o);
            numOfEntries--;
            cursor = database.rawQuery(sqlQuery, null);
            assertEquals(numOfEntries, cursor.getCount());
            cursor = database.rawQuery(sqlQueryByID, new String[]{Long.toString(o.getID())});
            assertEquals(0, cursor.getCount());
        }
    }

    @Test
    public void testRemoveListEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         * Check that number of entries match number of mock objects
         */
        for(ListObject o: expectedListObjects){
            eventHandler.addListItemEvent(o);
        }
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertNotSame(0, expectedListObjects.length);
        assertEquals(expectedListObjects.length, cursor.getCount());

        /**
         * Call removeListEvent()
         * Check that nothing remains in database
         */
        eventHandler.removeListEvent();
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertEquals(0, cursor.getCount());
    }

    @Test
    public void testRemoveScheduleTableEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         * Check that number of entries in DB match expected expected
         */
        for(ScheduleObject o: expectedScheduleObjects){
            eventHandler.addScheduleItemEvent(o);
        }
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedScheduleObjects.length, cursor.getCount());

        /**
         * Call removeListEvent()
         * Check that nothing remains in database
         */
        eventHandler.removeScheduleTableEvent();
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(0, cursor.getCount());
    }

    @Test
    public void testModifyListItemEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         */
        for(ListObject o: expectedListObjects){
            o.setID(eventHandler.addListItemEvent(o));
        }

        /**
         * Shuffle mock objects ID's
         * Call modifyListItemEvent() with new ID's
         * Check that updated entries match expected
         * TODO tests around null writing bug, members that should be updated to null are ignored when modifying
         */
        expectedListObjects[0].setID(expectedListObjects[1].getID());       /**object 1 sets ID = 2**/
        eventHandler.modifyListItemEvent(expectedListObjects[0]);           /**object 2 will look like object 1**/

        String sqlQueryByTitle = "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME + " WHERE " + DatabaseContract.LIST_COL_ID + " = ?";
        cursor = database.rawQuery(sqlQueryByTitle, new String[]{Long.toString(expectedListObjects[1].getID())});
        cursor.moveToFirst();

        assertEquals(1, cursor.getCount());
        assertEquals(expectedListObjects[0].getTitle(), cursor.getString(cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE)));
        assertEquals(expectedListObjects[0].getEstimate(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ESTIMATE)));

        if(expectedListObjects[0].isImportant() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
            assertEquals(expectedListObjects[0].getImportanceLevel(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_IMPORTANCE)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
        }
        if(expectedListObjects[0].isImported() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        }
        if(expectedListObjects[0].isPlanned() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        }
        if(expectedListObjects[0].hasDeadline() == true){
            assertEquals(1 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        } else {
            assertEquals(0 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        }

        if(expectedListObjects[0].getListObjectStart() != null){
            assertEquals(expectedListObjects[0].getStartMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_START)));
        }
        if(expectedListObjects[0].getListObjectEnd() != null){
            assertEquals(expectedListObjects[0].getEndMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_END)));
        }
        if(expectedListObjects[0].getListObjectDeadline() != null){
            assertEquals(expectedListObjects[0].getDeadlineMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE)));
        }

        /**
         * Check second modification
         */
        expectedListObjects[2].setID(expectedListObjects[3].getID());       /**object 3 sets ID = 4**/
        eventHandler.modifyListItemEvent(expectedListObjects[2]);       /** object 4 will look like object 3**/

        sqlQueryByTitle = "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME + " WHERE " + DatabaseContract.LIST_COL_ID + " = ?";
        cursor = database.rawQuery(sqlQueryByTitle, new String[]{Long.toString(expectedListObjects[3].getID())});
        cursor.moveToFirst();

        assertEquals(1, cursor.getCount());
        assertEquals(expectedListObjects[2].getTitle(), cursor.getString(cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE)));
        assertEquals(expectedListObjects[2].getEstimate(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ESTIMATE)));

        if(expectedListObjects[2].isImportant() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
            assertEquals(expectedListObjects[2].getImportanceLevel(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_IMPORTANCE)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
        }
        if(expectedListObjects[2].isImported() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        }
        if(expectedListObjects[2].isPlanned() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        }
        if(expectedListObjects[2].hasDeadline() == true){
            assertEquals(1 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        } else {
            assertEquals(0 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        }

        if(expectedListObjects[2].getListObjectStart() != null){
            assertEquals(expectedListObjects[2].getStartMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_START)));
        }
        if(expectedListObjects[2].getListObjectEnd() != null){
            assertEquals(expectedListObjects[2].getEndMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_END)));
        }
        if(expectedListObjects[2].getListObjectDeadline() != null){
            assertEquals(expectedListObjects[2].getDeadlineMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE)));
        }

    }

    @Test
    public void testModifyScheduleItemEvent() throws Exception {
        /**
         * Perform insertion via Eventhandler class
         */
        for(ScheduleObject o: expectedScheduleObjects){
            eventHandler.addScheduleItemEvent(o);
        }
        /**
         * Make query to database
         * Check that number of entries and all members match inserted mock objects
         */
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedScheduleObjects.length, cursor.getCount());

        /**
         * Shuffle mock objects ID's
         * Call modifyListItemEvent() with new ID's
         * Check that updated entries match expected
         */
        Calendar tempCal = Calendar.getInstance();
        expectedScheduleObjects[0].setSize(60);                             /**object 1 sets size = 60**/
        tempCal.set(2017,1,1,12,0);
        expectedScheduleObjects[0].setScheduledStartTime(tempCal);          /**object 1 sets startTime = feb 1, 2017, 12:00**/
        tempCal.set(2017,1,1,13,0);
        expectedScheduleObjects[0].setScheduledEndTime(tempCal);            /**object 1 sets startTime = feb 1, 2017, 13:00**/
        eventHandler.modifyScheduleItemEvent(expectedScheduleObjects[0]);   /**object 1 is now modified**/

        String sqlQueryByID = "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME + " WHERE " + DatabaseContract.SCHEDULE_COL_ID + " = ?";
        cursor = database.rawQuery(sqlQueryByID, new String[]{Long.toString(expectedScheduleObjects[0].getID())});
        cursor.moveToFirst();

        assertEquals(1, cursor.getCount());
        assertEquals(expectedScheduleObjects[0].getID(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
        assertEquals(expectedScheduleObjects[0].getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));

        assertEquals(expectedScheduleObjects[0].getComparableStartTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
        assertEquals(expectedScheduleObjects[0].getComparableEndTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));

        /**
         * Check second modification
         */
        expectedScheduleObjects[2].setSize(30);                             /**object 3 sets size = 60**/
        tempCal.set(2017,1,2,14,45);
        expectedScheduleObjects[2].setScheduledStartTime(tempCal);          /**object 3 sets startTime = feb 2, 2017, 14:45**/
        tempCal.set(2017,1,2,15,15);
        expectedScheduleObjects[2].setScheduledEndTime(tempCal);            /**object 3 sets startTime = feb 2, 2017, 15:15**/
        eventHandler.modifyScheduleItemEvent(expectedScheduleObjects[2]);   /**object 3 is now modified**/

        String sqlQueryByTitle = "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME + " WHERE " + DatabaseContract.SCHEDULE_COL_ID + " = ?";
        cursor = database.rawQuery(sqlQueryByTitle, new String[]{Long.toString(expectedScheduleObjects[2].getID())});
        cursor.moveToFirst();

        assertEquals(1, cursor.getCount());
        assertEquals(expectedScheduleObjects[2].getID(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
        assertEquals(expectedScheduleObjects[2].getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));

        assertEquals(expectedScheduleObjects[2].getComparableStartTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
        assertEquals(expectedScheduleObjects[2].getComparableEndTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));
    }

    @Test
    public void testRefreshPlanningEvent() throws Exception {
//        /**
//         * Perform insertion via Eventhandler class
//         * Check that number of entries in DB match expected expected
//         */
//        for(ScheduleObject o: expectedScheduleObjects){
//            eventHandler.addScheduleItemEvent(o);
//        }
//        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
//        assertEquals(expectedScheduleObjects.length, cursor.getCount());
//
//        /**
//         * Perform refresh, check that new entries in DB correspond to argument list
//         */
//        ArrayList<ScheduleObject> argumentList = new ArrayList<>();
//        argumentList.add(expectedScheduleObjects[1]);
//        argumentList.add(expectedScheduleObjects[3]);
//
//
//
//        eventHandler.refreshPlanningEvent(argumentList);
//        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
//        assertEquals(argumentList.size(), cursor.getCount());
//
//        cursor.moveToFirst();
//
//        int arrayPointer = 0;
//        while(!cursor.isAfterLast()){
//            assertEquals(argumentList.get(arrayPointer).getID(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
//            assertEquals(argumentList.get(arrayPointer).getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));
//            assertEquals(argumentList.get(arrayPointer).getComparableStartTime(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
//            assertEquals(argumentList.get(arrayPointer).getComparableEndTime(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));
//            cursor.moveToNext();
//            arrayPointer++;
//        }
    }
}

package se.kth.g2.planner;
/**
 * Created by David Jacobsson on 2017-04-23.
 */

import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class PlannerDBMS_InstrumentationTest {

    /**
     * GLOBAL POINTERS
     */
    private PlannerDBMS plannerDBMS;
    private SQLiteDatabase database;

    /**
     * Database setup method,
     * Runs BEFORE EACH execution of a test method
     */
    @Before
    public void setUp() throws Exception{
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DatabaseContract.DATABASE_NAME);

        plannerDBMS = new PlannerDBMS(context);
        database = plannerDBMS.getWritableDatabase();

        String[] databaseList = context.databaseList();

        assertEquals(2, databaseList.length);
        assertEquals(DatabaseContract.DATABASE_VERSION, database.getVersion());
        assertEquals(DatabaseContract.DATABASE_NAME, plannerDBMS.getDatabaseName());
        //TODO make conclusive tests for path
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
    public void testOnCreate() throws Exception {
        /**
         * Set pointers
         */
        Cursor cursor;
        String[] expectedColumnNames;
        String[] actualColumnNames;
        int arraypointer;

        /**
         * Assert database is open
         * Perform query for entire list_table
         * Extract list of column names from cursor and match with expected
         */
        assertEquals(true, database.isOpen());
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        actualColumnNames = cursor.getColumnNames();
        expectedColumnNames = new String[]{
                DatabaseContract.LIST_COL_ID,
                DatabaseContract.LIST_COL_TITLE,
                DatabaseContract.LIST_COL_ESTIMATE,
                DatabaseContract.LIST_COL_IMPORTANCE,
                DatabaseContract.LIST_COL_ISIMPORTANT,
                DatabaseContract.LIST_COL_ISIMPORTED,
                DatabaseContract.LIST_COL_ISPLANNED,
                DatabaseContract.LIST_COL_HASDEADLINE,
                DatabaseContract.LIST_COL_START,
                DatabaseContract.LIST_COL_END,
                DatabaseContract.LIST_COL_DEADLINE
        };
        assertEquals(expectedColumnNames.length, cursor.getColumnCount());
        arraypointer = 0;
        for (String actualColumnName: actualColumnNames){
            assertEquals(expectedColumnNames[arraypointer], actualColumnName);
            arraypointer++;
        }

        /**
         * Repeat tests for schedule_table
         */
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        actualColumnNames = cursor.getColumnNames();
        expectedColumnNames = new String[]{
                DatabaseContract.SCHEDULE_COL_ID,
                DatabaseContract.SCHEDULE_COL_SIZE,
                DatabaseContract.SCHEDULE_COL_START,
                DatabaseContract.SCHEDULE_COL_END
        };
        assertEquals(expectedColumnNames.length, cursor.getColumnCount());
        arraypointer = 0;
        for (String actualColumnName: actualColumnNames){
            assertEquals(expectedColumnNames[arraypointer], actualColumnName);
            arraypointer++;
        }
    }

    //TODO implement testOnUgrade()
//    @Test
//    public void testOnUpgrade() throws Exception {
//    }

    @Test
    public void testInsertToDB() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ListObject[] expectedObjects = new ListObject[]{
                DatabaseContract.mockListObject1(),
                DatabaseContract.mockListObject2(),
                DatabaseContract.mockListObject3(),
                DatabaseContract.mockListObject4()
        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ListObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database
         * Perform query for all entries, in all columns in list_table
         * instantiate cursor for navigation of result
         * move cursor to first row in result
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());

        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        cursor.moveToFirst();

        for(ListObject expectedObject : expectedObjects){
            /**
             * Check basic members
             */
            assertEquals(expectedObject.getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_ID)));
            assertEquals(expectedObject.getTitle(), cursor.getString(cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE)));
            assertEquals(expectedObject.getEstimate(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ESTIMATE)));
            assertEquals(expectedObject.getImportanceLevel(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_IMPORTANCE)));

            /**
             * Check Boolean members
             */
            if(expectedObject.getIsImportant() == true){
                assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
            }
            if(expectedObject.getIsImported() == true){
                assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
            }
            if(expectedObject.getIsPlanned() == true){
                assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
            }
            if(expectedObject.getHasDeadline() == true){
                assertEquals(1 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
            } else {
                assertEquals(0 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
            }

            /**
             * Check Schedule members
             */
            if(expectedObject.getListObjectStart() != null){
                assertEquals(expectedObject.getStartMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_START)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_START)));
            }
            if(expectedObject.getListObjectEnd() != null){
                assertEquals(expectedObject.getEndMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_END)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_END)));
            }
            if(expectedObject.getListObjectDeadline() != null){
                assertEquals(expectedObject.getDeadlineMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE)));
            } else {
                assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE)));
            }
            cursor.moveToNext();
        }
    }

    @Test
    public void testInsertScheduleToDB() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        String sqlQuery;
        Cursor cursor;
        ScheduleObject[] expectedObjects = new ScheduleObject[]{
                DatabaseContract.mockScheduleObject1(),
                DatabaseContract.mockScheduleObject2(),
                DatabaseContract.mockScheduleObject3(),
                DatabaseContract.mockScheduleObject4(),

        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ScheduleObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database
         * Perform query for all entries, in all columns in list_table
         * instantiate cursor for navigation of result
         * move cursor to first row in result
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());

        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        cursor.moveToFirst();

        for(ScheduleObject expectedObject : expectedObjects){
            /**
             * Check basic members
             */
            assertEquals(expectedObject.getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
            assertEquals(expectedObject.getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));
            assertEquals(expectedObject.getComparableStartTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
            assertEquals(expectedObject.getComparableEndTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));

            cursor.moveToNext();
        }
    }

    @Test
    public void testInsertScheduleListToDB() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ArrayList<ScheduleObject> expectedObjects = new ArrayList<ScheduleObject>();

        expectedObjects.add(DatabaseContract.mockScheduleObject1());
        expectedObjects.add(DatabaseContract.mockScheduleObject2());
        expectedObjects.add(DatabaseContract.mockScheduleObject3());
        expectedObjects.add(DatabaseContract.mockScheduleObject4());



        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        plannerDBMS.insertToDB(expectedObjects);
        /*
        for(ScheduleObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }*/

        /**
         * Reopen database
         * Perform query for all entries, in all columns in list_table
         * instantiate cursor for navigation of result
         * move cursor to first row in result
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());

        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        cursor.moveToFirst();

        for(ScheduleObject expectedObject : expectedObjects){
            /**
             * Check basic members
             */
            assertEquals(expectedObject.getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
            assertEquals(expectedObject.getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));
            assertEquals(expectedObject.getComparableStartTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
            assertEquals(expectedObject.getComparableEndTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));

            cursor.moveToNext();
        }
    }

    @Test
    public void testDeleteListObjectFromDB() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        long expectedID;
        int remainingObjects;
        ListObject[] expectedObjects = new ListObject[]{
                DatabaseContract.mockListObject1(),
        /*        DatabaseContract.mockListObject2(),
                DatabaseContract.mockListObject3(),
                DatabaseContract.mockListObject4()*/
        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ListObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database after insertion
         * Confirm isOpen()
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());

        /**
         * Assert all items have been inserted correctly and given expected ID's
         */
        expectedID = 1;
        for(ListObject expectedObject : expectedObjects){
            cursor = database.rawQuery(
                    "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME + " WHERE " + DatabaseContract.LIST_COL_ID + " = ?",
                    new String[]{Long.toString(expectedID)});
            cursor.moveToFirst();

            assertEquals(1, cursor.getCount());
            assertEquals(expectedID, expectedObject.getID());
            assertEquals(expectedObject.getTitle(), cursor.getString(cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE)));

            expectedID++;
        }

        /**
         * Delete all items in expectedObjects array from database
         * Assert that each object is present i database as expected before deletion
         * Assert that ONLY expected entry has been removed after each deletion
         */
        remainingObjects = expectedObjects.length;
        for(ListObject expectedObject : expectedObjects){
            cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
            assertEquals(remainingObjects, cursor.getCount());

            plannerDBMS.deleteListObjectFromDB(expectedObject.getID());
            database = plannerDBMS.getWritableDatabase();

            cursor = database.rawQuery(
                    "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME + " WHERE " + DatabaseContract.LIST_COL_ID + " = ?",
                    new String[]{Long.toString(expectedObject.getID())});
            assertEquals(0, cursor.getCount());

            remainingObjects--;
        }

        /**
         * Assert that database is completely empty
         */
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertEquals(0, cursor.getCount());
    }



    @Test
    public void testDeleteScheduleObjectFromDB() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        long expectedID;
        int remainingObjects;
        ScheduleObject[] expectedObjects = new ScheduleObject[]{
                DatabaseContract.mockScheduleObject1(),
                DatabaseContract.mockScheduleObject2(),
                DatabaseContract.mockScheduleObject3(),
                DatabaseContract.mockScheduleObject4()
        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ScheduleObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database after insertion
         * Confirm isOpen()
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());

        /**
         * Assert all items have been inserted correctly and given expected ID's
         */
        expectedID = 1;
        for(ScheduleObject expectedObject : expectedObjects){
            cursor = database.rawQuery(
                    "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME + " WHERE " + DatabaseContract.SCHEDULE_COL_ID + " = ?",
                    new String[]{Long.toString(expectedID)});
            cursor.moveToFirst();

            assertEquals(1, cursor.getCount());
            assertEquals(expectedID, expectedObject.getID());
            assertEquals(expectedObject.getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));

            expectedID++;
        }

        /**
         * Delete all items in expectedObjects array from database
         * Assert that each object is present i database as expected before deletion
         * Assert that ONLY expected entry has been removed after each deletion
         */
        remainingObjects = expectedObjects.length;
        for(ScheduleObject expectedObject : expectedObjects){
            cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
            assertEquals(remainingObjects, cursor.getCount());

            plannerDBMS.deleteScheduleObjectFromDB(expectedObject.getID());
            database = plannerDBMS.getWritableDatabase();

            cursor = database.rawQuery(
                    "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME + " WHERE " + DatabaseContract.SCHEDULE_COL_ID + " = ?",
                    new String[]{Long.toString(expectedObject.getID())});
            assertEquals(0, cursor.getCount());

            remainingObjects--;
        }

        /**
         * Assert that database is completely empty
         */
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(0, cursor.getCount());
    }



    @Test
    public void testDeleteListTable() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ListObject[] expectedObjects = new ListObject[]{
                DatabaseContract.mockListObject1(),
                DatabaseContract.mockListObject2(),
                DatabaseContract.mockListObject3(),
                DatabaseContract.mockListObject4()
        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ListObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database after insertion
         * Confirm isOpen()
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertEquals(expectedObjects.length, cursor.getCount());

        /**
         * Call deleteFromDB() method
         */
        plannerDBMS.deleteListTable();

        /**
         * Reopen database
         * Query database and check that entries are 0
         */
        database = plannerDBMS.getWritableDatabase();
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertEquals(0, cursor.getCount());
    }

    @Test
    public void testDeleteScheduleTable(){
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ScheduleObject[] expectedObjects = new ScheduleObject[]{
                DatabaseContract.mockScheduleObject1(),
                DatabaseContract.mockScheduleObject2(),
                DatabaseContract.mockScheduleObject3(),
                DatabaseContract.mockScheduleObject4()
        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ScheduleObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database after insertion
         * Confirm isOpen()
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedObjects.length, cursor.getCount());

        /**
         * Call deleteFromDB() method
         */
        plannerDBMS.deleteScheduleTable();

        /**
         * Reopen database
         * Query database and check that entries are 0
         */
        database = plannerDBMS.getWritableDatabase();
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(0, cursor.getCount());
    }

    @Test
    public void testUpdateDB() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ListObject[] expectedObjects = new ListObject[]{
                DatabaseContract.mockListObject1(),
                DatabaseContract.mockListObject2(),
                DatabaseContract.mockListObject3(),
                DatabaseContract.mockListObject4()
        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ListObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database after insertion
         * Confirm isOpen() and contains expected number of entries
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertEquals(expectedObjects.length, cursor.getCount());

        /**
         * Check that all ID's match expected values
         */
        cursor.moveToFirst();
        long expectedID = 1;
        while(!cursor.isAfterLast()){
            assertEquals(expectedID, cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_ID)));
            expectedID++;
            cursor.moveToNext();
        }


        /**
         * Change member values on mock objects
         * Perform updateDB() on two entries
         */
        expectedObjects[0].setID(2);                /**Mock object 1 sets ID = 2**/
        expectedObjects[2].setID(4);                /**Mock object 3 sets ID = 4**/
        plannerDBMS.updateDB(expectedObjects[0]);   /**Entry 2 now looks like mock object 1**/
        plannerDBMS.updateDB(expectedObjects[2]);   /**Entry 4 now looks like mock object 3**/

        /**
         * Perform ID specific queries, check that all column entries match expected value
         * entry with ID = 2 SHOULD NOW MATCH expectedObjects[0] eg. mockObject1
         */
        database = plannerDBMS.getWritableDatabase();
        cursor = database.rawQuery(
                "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME + " WHERE " + DatabaseContract.LIST_COL_ID + " = ?",
                new String[]{Long.toString(expectedObjects[1].getID())});
        cursor.moveToFirst();

        /**
         * Check regular members
         */
        assertEquals(1, cursor.getCount());
        assertEquals(expectedObjects[0].getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_ID)));
        assertEquals(expectedObjects[0].getTitle(), cursor.getString(cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE)));
        assertEquals(expectedObjects[0].getEstimate(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ESTIMATE)));

        /**
         * Check Boolean members
         */
        if(expectedObjects[0].getIsImportant() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
        }
        if(expectedObjects[0].getIsImported() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        }
        if(expectedObjects[0].getIsPlanned() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        }
        if(expectedObjects[0].getHasDeadline() == true){
            assertEquals(1 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        } else {
            assertEquals(0 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        }

        /**
         * Check Schedule members
         */
        assertEquals(0, cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_START)));
        assertEquals(0, cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_END)));
        assertEquals(expectedObjects[0].getDeadlineMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE)));


        /**
         * Entry with ID = 4 SHOULD NOW MATCH ecpectedObjects[2] eg. mockObject3
         */
        cursor = database.rawQuery(
                "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME + " WHERE " + DatabaseContract.LIST_COL_ID + " = ?",
                new String[]{Long.toString(expectedObjects[3].getID())});
        cursor.moveToFirst();

        /**
         * Check regular members
         */
        assertEquals(1, cursor.getCount());
        assertEquals(expectedObjects[2].getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_ID)));
        assertEquals(expectedObjects[2].getTitle(), cursor.getString(cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE)));
        assertEquals(expectedObjects[2].getEstimate(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ESTIMATE)));

        /**
         * Check Boolean members
         */
        if(expectedObjects[2].getIsImportant() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT)));
        }
        if(expectedObjects[2].getIsImported() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED)));
        }
        if(expectedObjects[2].getIsPlanned() == true){
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        } else {
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED)));
        }
        if(expectedObjects[2].getHasDeadline() == true){
            assertEquals(1 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        } else {
            assertEquals(0 , cursor.getInt(cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE)));
        }

        /**
         * Check Schedule members
         */
        assertEquals(expectedObjects[2].getStartMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_START)));
        assertEquals(expectedObjects[2].getEndMs(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_END)));
        assertEquals(0, cursor.getLong(cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE)));


    }

    @Test
    public void testUpdateScheduleDB() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ScheduleObject[] expectedObjects = new ScheduleObject[]{
                DatabaseContract.mockScheduleObject1(),
                DatabaseContract.mockScheduleObject2(),
                DatabaseContract.mockScheduleObject3(),
                DatabaseContract.mockScheduleObject4()
        };

        /**
         * Check that database is open before running insert
         * Perform insert and set ID members of mock objects
         */
        assertEquals(true, database.isOpen());
        for(ScheduleObject expectedObject : expectedObjects){
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        /**
         * Reopen database after insertion
         * Confirm isOpen() and contains expected number of entries
         */
        database = plannerDBMS.getWritableDatabase();
        assertEquals(true, database.isOpen());
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedObjects.length, cursor.getCount());

        /**
         * Check that all ID's match expected values
         */
        cursor.moveToFirst();
        long expectedID = 1;
        while(!cursor.isAfterLast()){
            assertEquals(expectedID, cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
            expectedID++;
            cursor.moveToNext();
        }


        /**
         * Change member values on mock objects
         * Perform updateDB() on two entries
         */
        expectedObjects[0].setSize(15);
        expectedObjects[0].setComparableStartTime(12345);
        expectedObjects[0].setComparableEndTime(54321);
        expectedObjects[2].setSize(15555);
        expectedObjects[2].setComparableStartTime(12345666);
        expectedObjects[2].setComparableEndTime(666218);
        plannerDBMS.updateDB(expectedObjects[0]);   /**Entry 2 now looks like mock object 1**/
        plannerDBMS.updateDB(expectedObjects[2]);   /**Entry 4 now looks like mock object 3**/

        /**
         * Perform ID specific queries, check that all column entries match expected value
         * entry with ID = 2 SHOULD NOW MATCH expectedObjects[0] eg. mockObject1
         */
        database = plannerDBMS.getWritableDatabase();
        cursor = database.rawQuery(
                "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME + " WHERE " + DatabaseContract.SCHEDULE_COL_ID + " = ?",
                new String[]{Long.toString(expectedObjects[0].getID())});
        cursor.moveToFirst();

        /**
         * Check regular members
         */
        assertEquals(1, cursor.getCount());
        assertEquals(expectedObjects[0].getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
        assertEquals(expectedObjects[0].getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));
        assertEquals(expectedObjects[0].getComparableStartTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
        assertEquals(expectedObjects[0].getComparableEndTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));



        /**
         * Entry with ID = 4 SHOULD NOW MATCH ecpectedObjects[2] eg. mockObject3
         */
        cursor = database.rawQuery(
                "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME + " WHERE " + DatabaseContract.SCHEDULE_COL_ID + " = ?",
                new String[]{Long.toString(expectedObjects[2].getID())});
        cursor.moveToFirst();

        /**
         * Check regular members
         */
        assertEquals(1, cursor.getCount());
        assertEquals(expectedObjects[2].getID(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID)));
        assertEquals(expectedObjects[2].getSize(), cursor.getInt(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE)));
        assertEquals(expectedObjects[2].getComparableStartTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START)));
        assertEquals(expectedObjects[2].getComparableEndTime(), cursor.getLong(cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END)));


    }

    @Test
    public void testFetchListTable() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ListObject[] expectedObjects = new ListObject[]{
                DatabaseContract.mockListObject1(),
                DatabaseContract.mockListObject2(),
                DatabaseContract.mockListObject3(),
                DatabaseContract.mockListObject4()
        };

        /**
         * Perform fetch from empty database
         * Check that array of size 0 is returned
         */
        ArrayList<ListObject> emptyFetch = plannerDBMS.fetchListTable();
        assertEquals(0, emptyFetch.size());
        /**
         * Insert mock objects in database
         * Reopen database
         * Confirm that number of entries in DB match expected value and ID's
         */
        for (ListObject expectedObject: expectedObjects) {
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        database = plannerDBMS.getWritableDatabase();
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME, null);
        assertEquals(expectedObjects.length, cursor.getCount());

        /**
         * Perform fetchListTable twice
         * Compare entries in returned list with mock objects
         * Compare the returned lists to check that they match each other
         */
        ArrayList<ListObject> fetchedList1 = plannerDBMS.fetchListTable();
        ArrayList<ListObject> fetchedList2 = plannerDBMS.fetchListTable();
        assertEquals(expectedObjects.length, fetchedList1.size());
        assertEquals(expectedObjects.length, fetchedList2.size());

        int arrayPointer = 0;
        for (ListObject fetchedObject : fetchedList1) {
            /**Regular members**/
            assertEquals(expectedObjects[arrayPointer].getID(), fetchedObject.getID());
            assertEquals(expectedObjects[arrayPointer].getTitle(), fetchedObject.getTitle());
            assertEquals(expectedObjects[arrayPointer].getEstimate(), fetchedObject.getEstimate());
            assertEquals(expectedObjects[arrayPointer].getImportanceLevel(), fetchedObject.getImportanceLevel());
            /**Boolean members**/
            assertEquals(expectedObjects[arrayPointer].getIsImportant(), fetchedObject.getIsImportant());
            assertEquals(expectedObjects[arrayPointer].getIsImported(), fetchedObject.getIsImported());
            assertEquals(expectedObjects[arrayPointer].getIsPlanned(), fetchedObject.getIsPlanned());
            assertEquals(expectedObjects[arrayPointer].getHasDeadline(), fetchedObject.getHasDeadline());
            /**Schedule members**/
            if(expectedObjects[arrayPointer].getListObjectStart() != null){
                assertEquals(expectedObjects[arrayPointer].getStartMs(), fetchedObject.getStartMs());
            } else {
                assertEquals(null, fetchedObject.getListObjectStart());
            }
            if(expectedObjects[arrayPointer].getListObjectEnd() != null){
                assertEquals(expectedObjects[arrayPointer].getEndMs(), fetchedObject.getEndMs());
            } else {
                assertEquals(null, fetchedObject.getListObjectEnd());
            }
            if(expectedObjects[arrayPointer].getListObjectDeadline() != null){
                assertEquals(expectedObjects[arrayPointer].getDeadlineMs(), fetchedObject.getDeadlineMs());
            } else {
                assertEquals(null, fetchedObject.getListObjectDeadline());
            }
            arrayPointer++;
        }

        /**
         * Check elements in second arraylist
         */
        arrayPointer = 0;
        for (ListObject fetchedObject : fetchedList2) {
            /**Regular members**/
            assertEquals(expectedObjects[arrayPointer].getID(), fetchedObject.getID());
            assertEquals(expectedObjects[arrayPointer].getTitle(), fetchedObject.getTitle());
            assertEquals(expectedObjects[arrayPointer].getImportanceLevel(), fetchedObject.getImportanceLevel());
            /**Boolean members**/
            assertEquals(expectedObjects[arrayPointer].getIsImportant(), fetchedObject.getIsImportant());
            assertEquals(expectedObjects[arrayPointer].getIsImported(), fetchedObject.getIsImported());
            assertEquals(expectedObjects[arrayPointer].getIsPlanned(), fetchedObject.getIsPlanned());
            assertEquals(expectedObjects[arrayPointer].getHasDeadline(), fetchedObject.getHasDeadline());
            /**Schedule members**/
            if(expectedObjects[arrayPointer].getListObjectStart() != null){
                assertEquals(expectedObjects[arrayPointer].getStartMs(), fetchedObject.getStartMs());
            } else {
                assertEquals(null, fetchedObject.getListObjectStart());
            }
            if(expectedObjects[arrayPointer].getListObjectEnd() != null){
                assertEquals(expectedObjects[arrayPointer].getEndMs(), fetchedObject.getEndMs());
            } else {
                assertEquals(null, fetchedObject.getListObjectEnd());
            }
            if(expectedObjects[arrayPointer].getListObjectDeadline() != null){
                assertEquals(expectedObjects[arrayPointer].getDeadlineMs(), fetchedObject.getDeadlineMs());
            } else {
                assertEquals(null, fetchedObject.getListObjectDeadline());
            }
            arrayPointer++;
        }
    }


    @Test
    public void testFetchScheduleTable() throws Exception {
        /**
         * Set pointers and create list with mock objects
         */
        Cursor cursor;
        ScheduleObject[] expectedObjects = new ScheduleObject[]{
                DatabaseContract.mockScheduleObject1(),
                DatabaseContract.mockScheduleObject2(),
                DatabaseContract.mockScheduleObject3(),
                DatabaseContract.mockScheduleObject4()
        };

        /**
         * Perform fetch from empty database
         * Check that array of size 0 is returned
         */
        ArrayList<ScheduleObject> emptyFetch = plannerDBMS.fetchScheduleTable();
        assertEquals(0, emptyFetch.size());
        /**
         * Insert mock objects in database
         * Reopen database
         * Confirm that number of entries in DB match expected value and ID's
         */
        for (ScheduleObject expectedObject: expectedObjects) {
            expectedObject.setID(plannerDBMS.insertToDB(expectedObject));
        }

        database = plannerDBMS.getWritableDatabase();
        cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME, null);
        assertEquals(expectedObjects.length, cursor.getCount());

        /**
         * Perform fetchListTable twice
         * Compare entries in returned list with mock objects
         * Compare the returned lists to check that they match each other
         */
        ArrayList<ScheduleObject> fetchedList1 = plannerDBMS.fetchScheduleTable();
        ArrayList<ScheduleObject> fetchedList2 = plannerDBMS.fetchScheduleTable();
        assertEquals(expectedObjects.length, fetchedList1.size());
        assertEquals(expectedObjects.length, fetchedList2.size());

        int arrayPointer = 0;
        for (ScheduleObject fetchedObject : fetchedList1) {
            /**Regular members**/
            assertEquals(expectedObjects[arrayPointer].getID(), fetchedObject.getID());
            assertEquals(expectedObjects[arrayPointer].getSize(), fetchedObject.getSize());
            assertEquals(expectedObjects[arrayPointer].getComparableStartTime(), fetchedObject.getComparableStartTime());
            assertEquals(expectedObjects[arrayPointer].getComparableEndTime(), fetchedObject.getComparableEndTime());

            arrayPointer++;
        }

        /**
         * Check elements in second arraylist
         */
        arrayPointer = 0;
        for (ScheduleObject fetchedObject : fetchedList2) {
            /**Regular members**/
            assertEquals(expectedObjects[arrayPointer].getID(), fetchedObject.getID());
            assertEquals(expectedObjects[arrayPointer].getSize(), fetchedObject.getSize());
            assertEquals(expectedObjects[arrayPointer].getComparableStartTime(), fetchedObject.getComparableStartTime());
            assertEquals(expectedObjects[arrayPointer].getComparableEndTime(), fetchedObject.getComparableEndTime());

            arrayPointer++;
        }
    }



    //TODO implement testPrintRowDB()
//    @Test
//    public void testPrintRowInDB() throws Exception {
//    }

    //TODO implement testPrintDB()
//    @Test
//    public void testPrintDB() throws Exception {
//    }

    //TODO implement testEntriesInDB()
//    @Test
//    public void testEntriesInDB() throws Exception {
//    }

    //TODO implement testColumnsInDB()
//    @Test
//    public void testColumnsInDB() throws Exception {
//    }
}
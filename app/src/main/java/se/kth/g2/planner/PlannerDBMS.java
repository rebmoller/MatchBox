package se.kth.g2.planner;
/**
 * Created by David Jacobsson
 * Modified by Love Stark
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class PlannerDBMS extends SQLiteOpenHelper{
    /**
     * Constructor method
     */
    public PlannerDBMS(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DatabaseContract.CREATE_LIST_TABLE);
        database.execSQL(DatabaseContract.CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.LIST_TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SCHEDULE_TABLE_NAME);
        onCreate(database);
    }

    /**
     * Create new entry in DATABASE and insert @param
     * Returns TABLE unique ID
     */
    public long insertToDB(ListObject object){
        SQLiteDatabase db = this.getWritableDatabase();                                 /** Open database **/
        ContentValues values = new ContentValues();                                     /** Create ContentValues and put all column values**/
        values.put(DatabaseContract.LIST_COL_TITLE, object.getTitle());                 /** Add title **/
        values.put(DatabaseContract.LIST_COL_ESTIMATE, object.getEstimate());           /** Add estimate **/
        values.put(DatabaseContract.LIST_COL_IMPORTANCE, object.getImportanceLevel());  /** Add importanceLevel **/
        values.put(DatabaseContract.LIST_COL_ISIMPORTANT, object.getIsImportant());        /** Add importance bool **/
        values.put(DatabaseContract.LIST_COL_ISIMPORTED, object.getIsImported());          /** Add imported bool **/
        values.put(DatabaseContract.LIST_COL_ISPLANNED, object.getIsPlanned());            /** Add planned bool **/
        values.put(DatabaseContract.LIST_COL_HASDEADLINE, object.getHasDeadline());        /** Add deadline bool **/
        if (object.getListObjectStart() != null) {
            values.put(DatabaseContract.LIST_COL_START, object.getStartMs());           /** Add startMs **/
        }
        else {
            values.put(DatabaseContract.LIST_COL_START, 0);                             /** Add zero when object is null **/
        }
        if (object.getListObjectEnd() != null){
            values.put(DatabaseContract.LIST_COL_END, object.getEndMs());               /** Add endMs **/
        }
        else{
            values.put(DatabaseContract.LIST_COL_END, 0);                               /** Add zero when object is null **/
        }
        if (object.getListObjectDeadline() != null) {
            values.put(DatabaseContract.LIST_COL_DEADLINE, object.getDeadlineMs());     /** Add deadlineMs **/
        }
        else {
            values.put(DatabaseContract.LIST_COL_DEADLINE, 0);                          /** Add zero when object is null **/
        }
        /**
         * Insert into database
         * Close database
         * Return supplied ID value
         */
        long newID = db.insert(DatabaseContract.LIST_TABLE_NAME, null, values);
        db.close();
        return newID;
    }

    public long insertToDB(ScheduleObject object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.SCHEDULE_COL_ID, object.getID());
        values.put(DatabaseContract.SCHEDULE_COL_SIZE, object.getSize());
        if(object.scheduledStartTime != null) {
            values.put(DatabaseContract.SCHEDULE_COL_START, object.getComparableStartTime());
        }
        else{
            values.put(DatabaseContract.SCHEDULE_COL_START, 0);
        }
        if(object.scheduledEndTime != null) {
            values.put(DatabaseContract.SCHEDULE_COL_END, object.getComparableEndTime());
        }
        else{
            values.put(DatabaseContract.SCHEDULE_COL_END, 0);
        }
        db.insert(DatabaseContract.SCHEDULE_TABLE_NAME, null, values);


        db.close();
        return object.getID();
    }

    public void insertToDB(ArrayList<ScheduleObject> scheduleList){
        SQLiteDatabase db = this.getWritableDatabase();
        for (ScheduleObject object : scheduleList){
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.SCHEDULE_COL_ID, object.getID());
            values.put(DatabaseContract.SCHEDULE_COL_SIZE, object.getSize());
            if(object.scheduledStartTime != null) {
                values.put(DatabaseContract.SCHEDULE_COL_START, object.getComparableStartTime());
            }
            else{
                values.put(DatabaseContract.SCHEDULE_COL_START, 0);
            }
            if(object.scheduledEndTime != null) {
                values.put(DatabaseContract.SCHEDULE_COL_END, object.getComparableEndTime());
            }
            else{
                values.put(DatabaseContract.SCHEDULE_COL_END, 0);
            }
            db.insert(DatabaseContract.SCHEDULE_TABLE_NAME, null, values);
        }
        db.close();
    }

    /**
     * Delete entry matching the id of @param from corresponding table in DATABASE
     */
    public void deleteListObjectFromDB(long id) {
        /**
         * Open database
         * Perform deletion from Database via query by @param
         */
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                DatabaseContract.LIST_TABLE_NAME,
                DatabaseContract.LIST_COL_ID + " = ?",
                new String[] { Long.toString(id) });
        db.close();
    }

    public void deleteScheduleObjectFromDB(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.SCHEDULE_TABLE_NAME, DatabaseContract.SCHEDULE_COL_ID + " =?",
                new String[] { Long.toString(id) });
        db.close();
    }

    /**
     * Delete ALL current entries from corresponding table
     * DATABASE will not be removed
     */
    public void deleteListTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.LIST_TABLE_NAME, null, null);
        db.close();
    }

    public void deleteScheduleTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.SCHEDULE_TABLE_NAME, null, null);
        db.close();
    }


    /**
     * Finds entry matching id of @param and replaces it with @param in DATABASE
     * NOTE! Id remains unchanged
     */
    public void updateDB(ListObject object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.LIST_COL_TITLE, object.getTitle());                 /** Add title **/
        values.put(DatabaseContract.LIST_COL_ESTIMATE, object.getEstimate());           /** Add estimate **/
        if(object.getIsImportant()){
            values.put(
                    DatabaseContract.LIST_COL_IMPORTANCE,
                    object.getImportanceLevel());                                       /** Add importanceLevel **/
        } else {
            values.put(DatabaseContract.LIST_COL_IMPORTANCE, 0);                        /** Nullify importanceLevel **/
        }
        values.put(DatabaseContract.LIST_COL_ISIMPORTANT, object.getIsImportant());        /** Add importance bool **/
        values.put(DatabaseContract.LIST_COL_ISIMPORTED, object.getIsImported());          /** Add imported bool **/
        values.put(DatabaseContract.LIST_COL_ISPLANNED, object.getIsPlanned());            /** Add planned bool **/
        values.put(DatabaseContract.LIST_COL_HASDEADLINE, object.getHasDeadline());        /** Add deadline bool **/
        if (object.getListObjectStart() != null) {
            values.put(DatabaseContract.LIST_COL_START, object.getStartMs());           /** Add startMs **/
        }
        else {
            values.put(DatabaseContract.LIST_COL_START, 0);                             /** Add zero when object is null **/
        }
        if (object.getListObjectEnd() != null){
            values.put(DatabaseContract.LIST_COL_END, object.getEndMs());               /** Add endMs **/
        }
        else{
            values.put(DatabaseContract.LIST_COL_END, 0);                               /** Add zero when object is null **/
        }
        if (object.getListObjectDeadline() != null) {
            values.put(DatabaseContract.LIST_COL_DEADLINE, object.getDeadlineMs());     /** Add deadlineMs **/
        }
        else {
            values.put(DatabaseContract.LIST_COL_DEADLINE, 0);                          /** Add zero when object is null **/
        }
        /**
         * Perform update with new column entries
         */
        db.update(DatabaseContract.LIST_TABLE_NAME, values, DatabaseContract.LIST_COL_ID + " = ?",
                new String[]{String.valueOf(object.getID())});
        db.close();
    }

    public void updateDB(ScheduleObject object){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.SCHEDULE_COL_SIZE, object.getSize());
        if(object.scheduledStartTime != null) {
            values.put(DatabaseContract.SCHEDULE_COL_START, object.getComparableStartTime());
        }
        else{
            values.put(DatabaseContract.SCHEDULE_COL_START, 0);
        }
        if(object.scheduledEndTime != null) {
            values.put(DatabaseContract.SCHEDULE_COL_END, object.getComparableEndTime());
        }
        else{
            values.put(DatabaseContract.SCHEDULE_COL_END, 0);
        }
        db.update(DatabaseContract.SCHEDULE_TABLE_NAME, values, DatabaseContract.SCHEDULE_COL_ID + " = ?",
                new String[]{String.valueOf(object.getID())});
    }

    /**
     * Returns array with all elements in corresponding table from DATABASE
     * NOTE! sorted in order of oldest entry first
     */
    public ArrayList<ListObject> fetchListTable() {
        ArrayList<ListObject> objects = new ArrayList<>();
        ListObject object;                                      /**pointer for object creation**/

        /**
         * Make query for entire database
         */
        SQLiteDatabase database = this.getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME;
        Cursor cursor = database.rawQuery(sqlQuery, null);

        /**
         * Check if query returned nothing
         */
        if(!cursor.moveToFirst()){
            database.close();
            return objects;
        }


        /**
         * add columnindex variables to simplify nested method calls
         */
        int idColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_ID);
        int titleColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_TITLE);
        int estimateColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_ESTIMATE);
        int importanceLevelColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_IMPORTANCE);
        int isImportantColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTANT);
        int isImportedColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_ISIMPORTED);
        int isPlannedColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_ISPLANNED);
        int hasDeadlineColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_HASDEADLINE);
        int startColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_START);
        int endColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_END);
        int DeadlineColIndex = cursor.getColumnIndex(DatabaseContract.LIST_COL_DEADLINE);

        while (!cursor.isAfterLast()){
            object = new ListObject();                                              /**create new object**/

            object.setID(cursor.getLong(idColIndex));                               /**copy ID**/
            object.setTitle(cursor.getString(titleColIndex));                       /**copy title**/
            object.setEstimate(cursor.getInt(estimateColIndex));                    /**copy estimate**/

            if(cursor.getInt(isImportantColIndex) > 0){
                object.setImportance(true);                                         /**copy isImportant = true **/
                object.setImportanceLevel(cursor.getInt(importanceLevelColIndex));  /**copy importanceLevel**/
            } else {
                object.setImportance(false);                                        /**copy isImportant = false **/
                object.setImportanceLevel(0);                                       /**nullify importanceLevel**/
            }

            if(cursor.getInt(isImportedColIndex) > 0){                              /** if isImported > 0 **/
                object.setImported(true);                                           /** set isImported = true **/
            } else {
                object.setImported(false);                                          /** set isImported = false **/
            }

            if (cursor.getInt(isPlannedColIndex) > 0){                              /**if isPlanned > 0 then**/
                object.setPlanned(true);                                            /**set isPlanned = true**/
                object.initPlannedCal();
                object.setStartMs(cursor.getLong(startColIndex));                   /**copy listObjectStart**/
                object.setEndMs(cursor.getLong(endColIndex));                       /**copy listObjectEnd**/
            } else {
                object.setPlanned(false);                                           /**set isPlanned = false, leave start & end members void**/
            }

            if (cursor.getInt(hasDeadlineColIndex) > 0){                            /**if hasDeadline > 0 then**/
                object.setDeadline(true);                                           /**set hasDeadline = true**/
                object.initDeadlineCal();
                object.setDeadlineMs(cursor.getLong(DeadlineColIndex));             /**copy listObjectDeadline**/
            } else {
                object.setDeadline(false);                                          /**set hasDeadline = false, leave deadline member void**/
            }

            objects.add(object);
            cursor.moveToNext();
        }
        database.close();
        return objects;
    }

    public ArrayList<ScheduleObject> fetchScheduleTable() {
        ArrayList<ScheduleObject> objects = new ArrayList<>();
        ScheduleObject object;                                      /**pointer for object creation**/
        /**
         * Make query for entire database
         */
        SQLiteDatabase database = this.getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + DatabaseContract.SCHEDULE_TABLE_NAME;
        Cursor cursor = database.rawQuery(sqlQuery, null);

        /**
         * Check if query returned nothing
         */
        if(!cursor.moveToFirst()){
            database.close();
            return objects;
        }

        /**
         * add columnindex variables to simplify nested method calls
         */
        int idColIndex = cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_ID);
        int sizeColIndex = cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_SIZE);
        int startColIndex = cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_START);
        int endColIndex = cursor.getColumnIndex(DatabaseContract.SCHEDULE_COL_END);

        while(!cursor.isAfterLast()){
            object = new ScheduleObject();                                              /**create new object**/
            object.setID(cursor.getLong(idColIndex));
            object.setSize(cursor.getInt(sizeColIndex));
            object.setComparableStartTime(cursor.getLong(startColIndex));
            object.setComparableEndTime(cursor.getLong(endColIndex));
            objects.add(object);
            cursor.moveToNext();
        }

        database.close();
        return objects;
    }

    /*****************************
     *****DEBUGGING METHODS*******
     ****************************/

    /**
     * Fetch single entry from DB
     * TODO remove or change name of method, not part f API currently
     */
    public ListObject getObject(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.LIST_TABLE_NAME, new String[]{
                        DatabaseContract.LIST_COL_ID,
                        DatabaseContract.LIST_COL_TITLE,
                        DatabaseContract.LIST_COL_ESTIMATE,
                        DatabaseContract.LIST_COL_ISPLANNED,
                        DatabaseContract.LIST_COL_HASDEADLINE,
                        DatabaseContract.LIST_COL_START,
                        DatabaseContract.LIST_COL_END,
                        DatabaseContract.LIST_COL_DEADLINE},
                        DatabaseContract.LIST_COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ListObject obj = new ListObject();
        obj.setID(Integer.parseInt(cursor.getString(0)));
        obj.setTitle(cursor.getString(1));
        obj.setEstimate(cursor.getInt(2));
        if(cursor.getInt(3) == 1){
            obj.setPlanned(true);
            obj.initPlannedCal();
            obj.setStartMs(cursor.getLong(5));
            obj.setEndMs(cursor.getLong(6));
        } else {
            obj.setPlanned(false);
        }
        if(cursor.getInt(4) == 1){
            obj.setDeadline(true);
            obj.initDeadlineCal();
            obj.setDeadlineMs(cursor.getLong(7));
        } else {
            obj.setDeadline(false);
        }
// return ListObject
        return obj;
    }

    /**
     * print all column entries corresponding to @param in DATABASE
     */
    public void printRowInDB(long id) {
        ListObject obj = new ListObject();
        obj = getObject(id);
        Log.d("Id           :", Long.toString(obj.getID()));
        Log.d("Name         :", obj.getTitle());
        Log.d("Estimate     :", Integer.toString(obj.getEstimate()));
        Log.d("isPlanned    :", Boolean.toString(obj.getIsPlanned()));
        Log.d("hasDeadline  :", Boolean.toString(obj.getHasDeadline()));
        if(obj.getIsPlanned()){
            Log.d("startCal     :", Long.toString(obj.getStartMs()));
            Log.d("endCal       :", Long.toString(obj.getEndMs()));
        }
        else{
            Log.d("startCal     :", "null");
            Log.d("endCal       :", "null");
        }
        if(obj.getHasDeadline()) {
            Log.d("Deadline     :", Long.toString(obj.getDeadlineMs()));
        }
        else
            Log.d("DeadlineCal     :", "null");
    }

    /**
     * Prints all entries in DATABASE to terminal formatted as:
     * ----------------------------------
     * Id       : {value}
     * Name     : {value of name}
     * Estimate : {value of estimate}
     * Priority : {value of priority}
     * !!OTHER PROPERTIES TO BE ADDED!!
     * ----------------------------------
     */
    public void printDB() {
        //  List<Integer> results = new ArrayList<>()
        SQLiteDatabase db = this.getReadableDatabase();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseContract.LIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                printRowInDB(cursor.getLong(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    /**
     * Returns number of entries/rows currently in the DATABASE
     */
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DatabaseContract.LIST_TABLE_NAME);
        return numRows;
    }

    /**
     * Returns the number of columns in DATABASE
     */
    public int columnsInDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.LIST_TABLE_NAME, new String[]{
                        DatabaseContract.LIST_COL_ID,
                        DatabaseContract.LIST_COL_TITLE,
                        DatabaseContract.LIST_COL_ESTIMATE,
                        DatabaseContract.LIST_COL_ISPLANNED,
                        DatabaseContract.LIST_COL_HASDEADLINE,
                        DatabaseContract.LIST_COL_START,
                        DatabaseContract.LIST_COL_END,
                        DatabaseContract.LIST_COL_DEADLINE},
                        DatabaseContract.LIST_COL_ID + "=?",
                null, null, null, null, null);
        int columns = cursor.getColumnCount();
        return columns;
    }
}
package se.kth.g2.planner;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Anna on 2017-04-19.
 * Modified by David Henriksson
 * Modified by David Jacobsson
 * Modified by Jens Egeland
 */

/**
 * EventHandler handles all events from all views.
 * Use getInstance to access the singleton EventHandler instance.
 */

public class EventHandler {
    private static final String TAG = "debugTAG";
    /**
     * EventHandler instance members
     */
    private ArrayList<ListObject> listObjects;
    private ArrayList<ScheduleObject> scheduleObjects;
    private PlannerDBMS plannerDBMS;

    /**
     * Constructor for singleton instance
     */
    private static final EventHandler instance = new EventHandler();

    private EventHandler() {
    }

    static EventHandler getInstance() {
        return instance;
    }

    /**
     * Initialising method
     */
    public void init(Context context){
        plannerDBMS = new PlannerDBMS(context);
        syncDatabaseTablesEvent();
    }

    public void syncDatabaseTablesEvent(){
        populateList();
        populateSchedule();

        int index = 0;
        while(index < scheduleObjects.size()){
            ScheduleObject scheduleObject = scheduleObjects.get(index);
            int result = PlannerUtil.objectIdBinarySearch(listObjects, scheduleObject.getID());
            if(result != -1){
                scheduleObject.setListObject(listObjects.get(result));
                index++;
            } else {
                scheduleObjects.remove(scheduleObject);
            }
        }
    }

    /**
     * Getters for local members
     */
    public ArrayList<ListObject> getListObjectsEvent() {
        return this.listObjects;
    }
    public ArrayList<ScheduleObject> generateScheduleObjectList() {
        return this.scheduleObjects;
    }

    /*******************************
     * Events triggered from list
     *******************************/

    /**
     * To be called when item is added to corresponding DATABASE table.
     */
    public long addListItemEvent(ListObject listItem) throws EventHandlerException {
        try {
            return plannerDBMS.insertToDB(listItem);
        } catch (Exception e) {
            throw new EventHandlerException("Failed to insert listItem to DB", e);
        }
    }
    public long addScheduleItemEvent(ScheduleObject scheduleItem) throws EventHandlerException {
        try {
            return plannerDBMS.insertToDB(scheduleItem);
        } catch (Exception e) {
            throw new EventHandlerException("Failed to insert listItem to DB", e);
        }
    }

    /**
     * To be called when item is removed from corresponding DATABASE table.
     */
    public void removeListItemEvent(ListObject listItem) throws EventHandlerException {
        try {
            long id = listItem.getID();
            plannerDBMS.deleteListObjectFromDB(id);
        } catch (Exception e) {
            throw new EventHandlerException("Failed to delete listItem from DB", e);
        }
    }
    public void removeScheduleItemEvent(ScheduleObject scheduleItem) throws EventHandlerException {
        try {
            long id = scheduleItem.getID();
            plannerDBMS.deleteScheduleObjectFromDB(id);
        } catch (Exception e) {
            throw new EventHandlerException("Failed to delete listItem from DB", e);
        }
    }

    /**
     * To be called when entire table is removed.
     */
    public void removeListEvent() throws EventHandlerException {
        try {
            plannerDBMS.deleteListTable();
        } catch (Exception e) {
            throw new EventHandlerException("Failed to delete list from DB", e);
        }
    }
    public void removeScheduleTableEvent() throws EventHandlerException {
        try {
            plannerDBMS.deleteScheduleTable();
        } catch (Exception e) {
            throw new EventHandlerException("Failed to delete list from DB", e);
        }
    }

    /**
     * To be called when list item in todolist is modified
     */
    public void modifyListItemEvent(ListObject listItem) throws EventHandlerException {
        try {
            plannerDBMS.updateDB(listItem);
        } catch (Exception e) {
            throw new EventHandlerException("Failed to modify the listItem in the DB", e);
        }
    }
    public void modifyScheduleItemEvent(ScheduleObject listItem) throws EventHandlerException {
        try {
            plannerDBMS.updateDB(listItem);
        } catch (Exception e) {
            throw new EventHandlerException("Failed to modify the listItem in the DB", e);
        }
    }

    /**
     * To be called when a new planning suggestion has been produced
     */
    public void refreshPlanningEvent(ArrayList<ScheduleObject> scheduleObjects) throws Exception {
        //TODO implement refreshPlanningEvent(), see note below
        /**
         * current issue seems to be that long is written as int in database,
         * the value returned from start and end fields appear to be ints. Cause unknown...
         */
//        try {
//            plannerDBMS.deleteScheduleTable();
//            for(ScheduleObject scheduleObject : scheduleObjects){
//                plannerDBMS.insertToDB(scheduleObject);
//            }
//        } catch (Exception e) {
//            throw new EventHandlerException("Failed to refresh database table", e);
//
//        }
    }

    /**
     * Returns all the list objects from the DB.
     */
    private void populateList() throws EventHandlerException {
        try {
            this.listObjects = plannerDBMS.fetchListTable();
        } catch (Exception e) {
            throw new EventHandlerException("Failed to fetch all listItems", e);

        }
    }
    private void populateSchedule() throws EventHandlerException {
        try {
            this.scheduleObjects = plannerDBMS.fetchScheduleTable();
        } catch (Exception e) {
            throw new EventHandlerException("Failed to fetch all listItems", e);
        }
    }
}


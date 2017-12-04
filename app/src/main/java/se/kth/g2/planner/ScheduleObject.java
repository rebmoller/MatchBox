package se.kth.g2.planner;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Elias on 2017-05-02.
 * Modified by Anna on 2017-05-05.
 * Modified by David J on 2017-05-08.
 * Modified by Love on 2017-05-08
 * Modified by Love on 2017-05-09
 */

public class ScheduleObject {


    public ListObject listObject;
    /** TODO replace calendar object with array? */
    public Calendar scheduledStartTime;
    public Calendar scheduledEndTime;
    public long _id;
    public int size;

    /**
     * Empty constructor
     */
    public ScheduleObject(){
        initCalendar();
        this.listObject = new ListObject();
        this._id = 0;
        this.size = 0;
    }

    /**
     * Overloaded constructors to handle planned events, to be able
     * to pass them directly from planning/scheduling method
     */
    public ScheduleObject(ListObject plannedObject) {
        scheduledStartTime = Calendar.getInstance();
        scheduledStartTime.setTimeInMillis(plannedObject.getStartMs());
        scheduledEndTime = Calendar.getInstance();
        scheduledEndTime.setTimeInMillis(plannedObject.getEndMs());
        this.listObject = plannedObject;
        size = plannedObject.getEstimate();
    }

    public ScheduleObject(ListObject newListObject, TimePool.TimeBlock timeBlock) {
        scheduledStartTime = Calendar.getInstance();
        scheduledEndTime = Calendar.getInstance();
        /** TODO this only sets objects at start of timeblock, might be expanded on later */
        scheduledStartTime.setTimeInMillis(timeBlock.startTime());
        this.listObject = newListObject;
        size = listObject.getEstimate();
        scheduledEndTime.setTimeInMillis(scheduledStartTime.getTimeInMillis() + (size * 60000));
        _id = newListObject.getID();
    }

    public void initCalendar(){
        this.scheduledStartTime = Calendar.getInstance();
        this.scheduledEndTime = Calendar.getInstance();
    }

    public void setSize(int size){ this.size = size; }
    public void setID(long _id){ this._id = _id; }
    public void setScheduledStartTime(Calendar cal){ this.scheduledStartTime = cal; }
    public void setScheduledEndTime(Calendar cal){ this.scheduledEndTime = cal; }
    public void setListObject(ListObject object){ this.listObject = object; }
    public void setComparableStartTime(long millis){ this.scheduledStartTime.setTimeInMillis(millis); }
    public void setComparableEndTime(long millis){
        this.scheduledEndTime.setTimeInMillis(millis);
    }

    public int getSize() { return this.size; }
    public long getID(){ return this._id; }
    public ListObject getListObject(){ return this.listObject; }
    public Calendar getScheduledStartTime() { return this.scheduledStartTime; }
    public Calendar getScheduledEndTime() { return this.scheduledEndTime; }
    public int getStartYear() {
        return scheduledStartTime.get(Calendar.YEAR);
    }
    public int getStartMonth() {
        return scheduledStartTime.get(Calendar.MONTH);
    }
    public int getWeekOfYear(){ return scheduledStartTime.get(Calendar.WEEK_OF_YEAR);}
    public int getStartDay() {
        return scheduledStartTime.get(Calendar.DAY_OF_WEEK);
    }
    public int getStartHour() { return scheduledStartTime.get(Calendar.HOUR_OF_DAY);}
    public int getStartMinute() { return scheduledStartTime.get(Calendar.MINUTE);}
    public long getComparableStartTime() { return scheduledStartTime.getTimeInMillis(); }

    public long getComparableEndTime() {
        return scheduledEndTime.getTimeInMillis();
    }
    public int getEndYear(){ return  scheduledEndTime.get(Calendar.YEAR);}
    public int getEndMonth(){ return  scheduledEndTime.get(Calendar.MONTH);}
    public int getEndDay(){ return  scheduledEndTime.get(Calendar.DAY_OF_WEEK);}
    public int getEndHour(){ return scheduledEndTime.get(Calendar.HOUR_OF_DAY);}
    public int getEndMinute() { return scheduledEndTime.get(Calendar.MINUTE);}

}
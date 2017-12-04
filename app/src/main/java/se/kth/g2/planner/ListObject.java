package se.kth.g2.planner;
/**
 * Created by Elias Sundberg
 * Modfified by David Jacobsson
 * Modified by Love Stark
 * Modified by Jens Egeland
 */

import java.io.Serializable;
import java.util.Calendar;

// TODO always set estimate, planned items have estimate plannedEnd() - plannedStart()
public class ListObject implements Serializable {

    /**
     * object properties
     */
    private long _id;                        // unique object identifier
    private String objectTitle;              // object name
    private int estimate;                    // estimated time in minutes
    private int importanceLevel;             // importance level for prioritized events (0-3)
    private boolean isImportant;             // flag for importance
    private boolean isImported;              // flag for generated list objects from imported calendar events
    private boolean isPlanned;               // flag for planned events
    private boolean hasDeadline;             // flag for deadline events
    private Calendar listObjectStart;        // start date and time for planned event
    private Calendar listObjectEnd;          // end date and time for planned event
    private Calendar listObjectDeadline;     // date for planned hasDeadline event

    /**
     * constructors
     */
    public ListObject() {
    }

    /**
     * basic constructor with only object title
     */
    public ListObject(String title) {
        this.objectTitle = title;
        this.hasDeadline = false;
        this.isPlanned = false;
    }

    /**
     * constructor with object title and estimated effort time
     */
    public ListObject(String title, int estimate) {
        this.objectTitle = title;
        this.estimate = estimate;
        this.hasDeadline = false;
    }

    /**
     * Constructor for imported calendar events should go here, but
     * written when more info on imported date formats are available.
     * public ListObject( ... ) { ... }
     */

    /**
     * constructor with object title and type of even (planned/deadline)
     */
    public ListObject(String title, String typeOfEvent) {
        this.objectTitle = title;
        switch (typeOfEvent) {
            case "deadline":
                this.hasDeadline = true;
                listObjectDeadline = Calendar.getInstance();
                listObjectDeadline.set(Calendar.SECOND, 0);
                break;
            case "planned":
                this.isPlanned = true;
                listObjectStart = Calendar.getInstance();
                listObjectStart.set(Calendar.SECOND, 0);
                listObjectEnd = Calendar.getInstance();
                listObjectEnd.set(Calendar.SECOND, 0);
                break;
            default:
                this.hasDeadline = false;
                this.isPlanned = false;
                break;
        }
    }

    /**
     * constructor with object title, estimate effort time, and type of event.
     * deadline- and planned-dates to be set separately with methods below.
     */
    public ListObject(String title, int estimate, String typeOfEvent) {
        this.objectTitle = title;
        this.estimate = estimate;
        switch (typeOfEvent) {
            case "deadline":
                this.hasDeadline = true;
                listObjectDeadline = Calendar.getInstance();
                break;
            case "planned":
                this.isPlanned = true;
                listObjectStart = Calendar.getInstance();
                listObjectEnd = Calendar.getInstance();
                break;
            default:
                this.hasDeadline = true;
                this.isPlanned = true;
                break;
        }
    }

    /**
     * Methods for initializing instance of Calendar in object
     */
    public void initPlannedCal() {
        this.listObjectStart = Calendar.getInstance();
        this.listObjectEnd = Calendar.getInstance();
    }

    public void initDeadlineCal() {
        this.listObjectDeadline = Calendar.getInstance();
    }

    /**
     * Setting methods
     */
    public void setID(long newID) {
        this._id = newID;
    }

    public void setTitle(String newTitle) {
        this.objectTitle = newTitle;
    }

    public void setEstimate(int newEstimate) {
        this.estimate = newEstimate;
    }


    public void setImportanceLevel(int importance) {
        if (importance == 0) this.isImportant = false;
        else this.isImportant = true;
        this.importanceLevel = importance;
    }

    public void setImportance(boolean importance) {
        this.isImported = importance;
    }

    public void setImported(boolean imported) {
        this.isImported = imported;
    }

    public void setPlanned(boolean planned) {
        this.isPlanned = planned;
    }

    public void setDeadline(boolean deadline) {
        this.hasDeadline = deadline;
    }

    public void setStartMs(long millis) {
        this.listObjectStart.setTimeInMillis(millis);
    }

    public void setEndMs(long millis) {
        this.listObjectEnd.setTimeInMillis(millis);
    }

    public void setDeadlineMs(long millis) {
        this.listObjectDeadline.setTimeInMillis(millis);
    }

    public void setListObjectStart(Calendar start) {
        this.listObjectStart = start;
    }

    public void setListObjectEnd(Calendar end) {
        this.listObjectEnd = end;
    }

    public void setListObjectDeadline(Calendar deadline) {
        this.listObjectDeadline = deadline;
    }

    public void setStartDate(int year, int month, int day, int hour, int minute) {
        listObjectStart.set(year, month, day, hour, minute);
    }

    public void setEndDate(int year, int month, int day, int hour, int minute) {
        listObjectEnd.set(year, month, day, hour, minute);
    }

    public void setDeadlineDate(int year, int month, int day, int hour, int minute) {
        listObjectDeadline.set(year, month, day, hour, minute);
    }

    /**
     * Getting methods
     */
    public long getID() {
        return this._id;
    }

    public String getTitle() {
        return this.objectTitle;
    }

    public int getEstimate() {
        return this.estimate;
    }

    public boolean getIsImportant()    { return this.isImportant; }

    public boolean getIsImported() {
        return this.isImported;
    }

    public boolean getIsPlanned() {
        return this.isPlanned;
    }

    public boolean getHasDeadline() {
        return this.hasDeadline;
    }

    public Calendar getListObjectStart() {
        return this.listObjectStart;
    }

    public Calendar getListObjectEnd() {
        return this.listObjectEnd;
    }

    public Calendar getListObjectDeadline() {
        return this.listObjectDeadline;
    }

    public int getStartYear() {
        return listObjectStart.get(Calendar.YEAR);
    }

    public int getStartMonth() {
        return listObjectStart.get(Calendar.MONTH);
    }

    public int getStartDay() {
        return listObjectStart.get(Calendar.DAY_OF_MONTH);
    }

    public int getStartHour() {
        return listObjectStart.get(Calendar.HOUR_OF_DAY);
    }

    public int getStartMinute() {
        return listObjectStart.get(Calendar.MINUTE);
    }

    public int getEndYear() {
        return listObjectEnd.get(Calendar.YEAR);
    }

    public int getEndMonth() {
        return listObjectEnd.get(Calendar.MONTH);
    }

    public int getEndDay() {
        return listObjectEnd.get(Calendar.DAY_OF_MONTH);
    }

    public int getEndHour() {
        return listObjectEnd.get(Calendar.HOUR_OF_DAY);
    }

    public int getEndMinute() {
        return listObjectEnd.get(Calendar.MINUTE);
    }

    public int getDeadlineYear() {
        return listObjectDeadline.get(Calendar.YEAR);
    }

    public int getDeadlineMonth() {
        return listObjectDeadline.get(Calendar.MONTH);
    }

    public int getDeadlineDay() {
        return listObjectDeadline.get(Calendar.DAY_OF_MONTH);
    }

    public int getDeadlineHour() {
        return listObjectDeadline.get(Calendar.HOUR_OF_DAY);
    }

    public int getDeadlineMinute() {
        return listObjectDeadline.get(Calendar.MINUTE);
    }

    public long getStartMs() {
        return listObjectStart.getTimeInMillis();
    }

    public long getEndMs() {
        return listObjectEnd.getTimeInMillis();
    }

    public long getDeadlineMs() {
        return listObjectDeadline.getTimeInMillis();
    }

    public int getImportanceLevel() { return this.importanceLevel; }
}
package se.kth.g2.planner;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Love in 2017-04-28.
 * Edited by David H on 2017-04-28.
 * Edited by Love in 2017-04-28.
 */

public class PlannerClass {
    public ArrayList<ScheduleObject> generateSchedule(ArrayList<ListObject> listOfEvents) {
        TimePool timePool = new TimePool();
        ArrayList<ScheduleObject> scheduleList = new ArrayList<>();
        ArrayList<ListObject> leftUnplanned = new ArrayList<>();
        ArrayList<ScheduleObject> panicBlocks = new ArrayList<>();


        // Clone the eventlist, just to make sure nothing happens to the original
        // in terms of order, additions etc.
        ArrayList<ListObject> clonedListOfEvents = (ArrayList<ListObject>) listOfEvents.clone();
        ArrayList<ListObject> plannedEvents = this.getPlannedEvents(clonedListOfEvents);
        ArrayList<ListObject> deadlineEvents = this.getDeadlineEvents(clonedListOfEvents);

        // Planned events
        for (ListObject plannedEvent : plannedEvents) {
            Calendar planned = plannedEvent.getListObjectStart();
            if (planned.get(Calendar.WEEK_OF_YEAR) != timePool.current.get(Calendar.WEEK_OF_YEAR)) {
                leftUnplanned.add(plannedEvent);
                continue;
            } else {
                timePool.trimToFit(plannedEvent);
                ScheduleObject scheduleThis = new ScheduleObject(plannedEvent);
                Schedule.addEvent(scheduleThis, scheduleList);
            }
        }

        // Deadlines
        // Sort deadlines by most pressing deadline
        PlannerUtil.sortListObjByDeadline(deadlineEvents);
        for (int i = 0; i < deadlineEvents.size(); i++) {
            boolean matchFound = false;
            if (deadlineEvents.get(i).getDeadlineMs() < timePool.current.getTimeInMillis()) {
                Calendar deadLinePassed = Calendar.getInstance(timePool.current.getTimeZone());
                deadLinePassed.set(Calendar.HOUR_OF_DAY, timePool.restrictions.start);
                TimePool.TimeBlock tB = timePool.generateTimeBlock(deadLinePassed.getTimeInMillis());
                ScheduleObject newPanicObject = new ScheduleObject(deadlineEvents.get(i), tB);
                panicBlocks.add(newPanicObject);
                matchFound = true;
            }
            for (TimePool.TimeBlock timeBlock : timePool.timeBlocks) {
                if (deadlineEvents.get(i).getEstimate() <= timeBlock.size()) {
                    if (timeBlock.deadLineMatches(deadlineEvents.get(i))) {
                        timeBlock.shrinkSize(deadlineEvents.get(i).getEstimate());
                        ScheduleObject scheduleThis = new ScheduleObject(deadlineEvents.get(i), timeBlock);
                        Schedule.addEvent(scheduleThis, scheduleList);
                        matchFound = true;
                        break;
                    }
                }
            }
            if (!matchFound) {
                if (deadlineEvents.get(i).getDeadlineMs() > timePool.startOfNext.getTimeInMillis()) {
                    timePool.addTime();
                    handlePlannedEvents(leftUnplanned, scheduleList, timePool);
                    i--; // Redo the iteration
                } else {
                    Calendar panicDate = Calendar.getInstance(timePool.current.getTimeZone());
                    panicDate.setTimeInMillis(deadlineEvents.get(i).getDeadlineMs());
                    panicDate.add(Calendar.DATE, -1);
                    panicDate.set(Calendar.HOUR_OF_DAY, timePool.restrictions.end);
                    TimePool.TimeBlock panicBlock = timePool.generateTimeBlock(panicDate.getTimeInMillis());
                    ScheduleObject panicObject = new ScheduleObject(deadlineEvents.get(i), panicBlock);
                    panicBlocks.add(panicObject);
                }
            }
        }

        // Remaining list objects
        for (int i = 0; i < clonedListOfEvents.size(); i++) {
            boolean matchFound = false;
            for (TimePool.TimeBlock timeBlock : timePool.timeBlocks) {
                if (clonedListOfEvents.get(i).getEstimate() <= timeBlock.size()) {
                    // Add event to list
                    ScheduleObject scheduleThis = new ScheduleObject(clonedListOfEvents.get(i), timeBlock);
                    Schedule.addEvent(scheduleThis, scheduleList);

                    // Shift timeblock to end of event
                    long tBEnd = timeBlock.endTime();
                    timeBlock.shiftStartTimeByMinutes(clonedListOfEvents.get(i).getEstimate());
                    timeBlock.setEnd(tBEnd);

                    matchFound = true;
                    break;
                }
            }
            // TODO test that this works by inserting really big event blocks, and see that they get scheduled properly next week
            if (!matchFound) {
                timePool.addTime();
                leftUnplanned = handlePlannedEvents(leftUnplanned, scheduleList, timePool);
                i--; // Redo the iteration
            }
        }

        if (leftUnplanned.size() > 0) {
            forcePlanned(leftUnplanned, scheduleList);
        }

        if (panicBlocks.size() > 0) {
            for (ScheduleObject sO : panicBlocks) {
                scheduleList.add(0, sO);
            }
        }

        return scheduleList;

    }

    /**
     * Force deadline that has passed, or deadlines events that do not fit into schedule,
     * to be placed somewhere in schedule.
     * TODO Come up with a better place to put deadlines than just start of day
     */

    private void forcePlanned(ArrayList<ListObject> leftUnplanned, ArrayList<ScheduleObject> scheduleList) {
        for (ListObject plannedEvent : leftUnplanned) {
            ScheduleObject scheduleThis = new ScheduleObject(plannedEvent);
            scheduleList.add(scheduleThis);
        }
    }

    public ArrayList<ListObject> handlePlannedEvents(ArrayList<ListObject> plannedEvents, ArrayList<ScheduleObject> scheduleList, TimePool timePool) {
        ArrayList<ListObject> leftUnplanned = new ArrayList<>();
        for (ListObject plannedEvent : plannedEvents) {
            Calendar planned = plannedEvent.getListObjectStart();
            if (planned.get(Calendar.WEEK_OF_YEAR) != timePool.current.get(Calendar.WEEK_OF_YEAR)) {
                leftUnplanned.add(plannedEvent);
                continue;
            } else {
                timePool.trimToFit(plannedEvent);
                //ScheduleObject scheduleThis = new ScheduleObject(plannedEvent);
                //Schedule.addEvent(scheduleThis, scheduleList);
            }
        }
        return leftUnplanned;
    }

    public ArrayList<ListObject> getPlannedEvents(ArrayList<ListObject> inputList) {

        ArrayList<ListObject> filteredList = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i++) {
            if (inputList.get(i).getIsPlanned()) {
                filteredList.add(inputList.get(i));
                inputList.remove(i);
                i--;
            }
        }

        return filteredList;
    }

    public ArrayList<ListObject> getDeadlineEvents(ArrayList<ListObject> inputList) {

        ArrayList<ListObject> filteredList = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i++) {
            if (inputList.get(i).getHasDeadline()) {
                filteredList.add(inputList.get(i));
                inputList.remove(i);
                i--;
            }
        }
        return filteredList;
    }
}

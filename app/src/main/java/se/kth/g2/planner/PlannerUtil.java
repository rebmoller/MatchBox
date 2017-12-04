package se.kth.g2.planner;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sieken on 2017-05-02.
 */

class PlannerUtil {

    public static void sortByBlockSize(ArrayList<TimePool.TimeBlock> timeBlocks) {
        Collections.sort(timeBlocks, new Comparator<TimePool.TimeBlock>() {
            @Override
            public int compare(TimePool.TimeBlock tb1, TimePool.TimeBlock tb2) {
                Integer comparable1 = new Integer(tb1.size());
                Integer comparable2 = new Integer(tb2.size());
                return comparable1.compareTo(comparable2);
            }
        });
    }

    public static void chronoSort(ArrayList<TimePool.TimeBlock> timeBlocks) {
        Collections.sort(timeBlocks, new Comparator<TimePool.TimeBlock>() {
            @Override
            public int compare(TimePool.TimeBlock tb1, TimePool.TimeBlock tb2) {
                Long comparable1 = new Long(tb1.startTime());
                Long comparable2 = new Long(tb2.startTime());
                return comparable1.compareTo(comparable2);
            }
        });
    }

    public static void scheduleSortByMillis(ArrayList<ScheduleObject> scheduleObjects) {
        Collections.sort(scheduleObjects, new Comparator<ScheduleObject>() {
            @Override
            public int compare(ScheduleObject o1, ScheduleObject o2) {
                Long comparable1 = new Long(o1.getComparableStartTime());
                Long comparable2 = new Long(o2.getComparableStartTime());
                return comparable1.compareTo(comparable2);
            }
        });
    }

    public static void sortListObjByDeadline(ArrayList<ListObject> deadlineObjects) {
        Collections.sort(deadlineObjects, new Comparator<ListObject>() {
            @Override
            public int compare(ListObject o1, ListObject o2) {
                Long comparable1 = new Long(o1.getDeadlineMs());
                Long comparable2 = new Long(o2.getDeadlineMs());
                return comparable1.compareTo(comparable2);
            }
        });
    }

    public static int objectIdBinarySearch(ArrayList<ListObject> array, long id){
        int bottom = 0;
        int top = array.size() - 1;
        return objectIdBinarySearch(array, id, bottom, top);
    }

    private static int objectIdBinarySearch(ArrayList<ListObject> array, long id, int bottom, int top) {
        ListObject object;
        if (top == bottom) {
            object = array.get(top);
            if (object.getID() == id) {
                return top;
            } else {
                return -1;
            }
        }

        int middle = ((top - bottom) / 2) + bottom;
        object = array.get(middle);
        if (object.getID() == id) {
            return middle;
        } else if (object.getID() < id) {
            return objectIdBinarySearch(array, id, middle + 1, top);
        } else {
            return objectIdBinarySearch(array, id, bottom, middle - 1);
        }
    }
}

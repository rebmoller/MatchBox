package se.kth.g2.planner;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DATE;

/**
 * Created by sieken on 2017-05-02.
 */

public class TimePool {

    boolean initialized = false;
    public Calendar current;
    public Calendar startOfNext;
    public ArrayList<TimeBlock> timeBlocks;
    public TimeRestrictions restrictions;

    public TimePool() {
        current = Calendar.getInstance();
        current.set(Calendar.SECOND, 0);
        startOfNext = Calendar.getInstance(current.getTimeZone());
        startOfNext.setTime(current.getTime());

        timeBlocks = new ArrayList<>();
        restrictions = new TimeRestrictions();

        // Set startOfNext to be the next even 15 minutes
        while (startOfNext.get(Calendar.MINUTE) % 15 != 0) {
            startOfNext.add(Calendar.MINUTE, 1);
        }
        startOfNext.set(Calendar.SECOND, 0);

        addTime();
    }

    // TODO properly implement this, make it work with Starks methods
    public TimePool(long timeInMillis) {
        current = Calendar.getInstance();
        current.setTimeInMillis(timeInMillis);
        current.set(Calendar.SECOND, 0);
        // Set current to be the next even 15 minutes
        while (current.get(Calendar.MINUTE) % 15 != 0) {
            current.add(Calendar.MINUTE, 1);
        }

        startOfNext = Calendar.getInstance(current.getTimeZone());
        startOfNext.setTime(current.getTime());
        timeBlocks = new ArrayList<>();
        restrictions = new TimeRestrictions();
        addTime();
    }

    public void trimToFit(ListObject plannedEvent) {
        PlannerUtil.chronoSort(timeBlocks);
        TimeBlock fitThis = new TimeBlock(plannedEvent.getStartMs());
        fitThis.setEnd(plannedEvent.getEndMs());
        TimeBlock template;

        ArrayList<TimeBlock> additions = new ArrayList<>();

        // TODO make checks rule out dates faster maybe
        for (TimeBlock t : timeBlocks) {
            // End if done
            if (fitThis.size() == 0) {
                break;
            }
            // Check if planned event
            // 1) Is completely encapsulated by time block,
            // 2) Only has its start time in time block, or
            // 3) Only has end time in time block
            if (fitThis.startTime() >= t.startTime() && fitThis.endTime() <= t.endTime()) {
                TimeBlock newTimeBlock = new TimeBlock(fitThis.endTime());
                newTimeBlock.setEnd(t.endTime());
                t.setEnd(fitThis.startTime());
                fitThis.setEnd(fitThis.startTime());
                additions.add(newTimeBlock);
                break;
            } else if (t.startTime() <= fitThis.startTime() && fitThis.endTime() > t.endTime()
                    && fitThis.startTime() < t.endTime()) {
                template = new TimeBlock(fitThis.startTime());
                template.setEnd(t.endTime());
                t.shrinkSize(template.size());
                fitThis.shrinkSize(template.size());
                fitThis.shiftStartTimeByMinutes(-template.size());
                break;
            } else if (t.startTime() > fitThis.startTime() && fitThis.endTime() > t.startTime()
                    && fitThis.endTime() <= t.endTime()) {
                template = new TimeBlock(t.startTime());
                template.setEnd(fitThis.endTime());
                fitThis.shrinkSize(template.size());
                t.shrinkSize(template.size());
                t.shiftStartTimeByMinutes(template.size());
                break;
            } else {
                // Probably date not matching
            }
        }
        for (TimeBlock addition : additions) {
            timeBlocks.add(addition);
        }
        PlannerUtil.sortByBlockSize(timeBlocks);
    }

    public class TimeRestrictions {
        int start;
        int end;
        int lunchStart;
        int lunchLengthInHours;
        int lunchEnd;

        public TimeRestrictions() {
            start = 8;
            lunchStart = 12;
            lunchLengthInHours = 1;
            lunchEnd = lunchStart + lunchLengthInHours;
            end = 17;
        }

        public long getEndOfCurrent(Calendar startOfNext) {
            Calendar endOfCurrent = Calendar.getInstance(startOfNext.getTimeZone());
            endOfCurrent.setTime(startOfNext.getTime());
            int currentHour = endOfCurrent.get(Calendar.HOUR_OF_DAY);

            // Is time in
            // first work-block, or
            // second work-block?
            // TODO extend with free time hours maybe
            if (currentHour >= restrictions.start && currentHour < restrictions.lunchEnd) {
                endOfCurrent.set(Calendar.HOUR_OF_DAY, lunchStart);
                endOfCurrent.set(Calendar.MINUTE, 0);
                endOfCurrent.set(Calendar.SECOND, 0);
            } else if (currentHour >= restrictions.lunchEnd && currentHour <= restrictions.end) {
                endOfCurrent.set(Calendar.HOUR_OF_DAY, end);
                endOfCurrent.set(Calendar.MINUTE, 0);
                endOfCurrent.set(Calendar.SECOND, 0);
            } else {
                // Probably in lunch hours, fix this
                System.out.println("Probably in lunch hours or after end of day");
            }

            return endOfCurrent.getTimeInMillis();
        }

        public long getStartOfNext(Calendar tempStartOfNext) {
            Calendar startOfNextRestriction = Calendar.getInstance(tempStartOfNext.getTimeZone());
            startOfNextRestriction.setTime(tempStartOfNext.getTime());
            int currentHour = startOfNextRestriction.get(Calendar.HOUR_OF_DAY);

            // Is time in
            // first work-block, or
            // second work-block?
            if (currentHour >= restrictions.start && currentHour < restrictions.lunchEnd) {
                startOfNextRestriction.set(Calendar.HOUR_OF_DAY, restrictions.lunchEnd);
                startOfNextRestriction.set(Calendar.MINUTE, 0);
                startOfNextRestriction.set(Calendar.SECOND, 0);
            } else if (currentHour >= restrictions.lunchEnd) { // TODO configure this for free time hours
                startOfNextRestriction.add(DATE, 1);
                startOfNextRestriction.set(Calendar.HOUR_OF_DAY, restrictions.start);
                startOfNextRestriction.set(Calendar.MINUTE, 0);
                startOfNextRestriction.set(Calendar.SECOND, 0);
            } else {
                // Probably in lunch hours, fix this
                System.out.println("Probably in lunch hours or after end of day");
            }

            return startOfNextRestriction.getTimeInMillis();
        }
    }

    class TimeBlock {
        private int size;
        private Calendar timeStamp;

        public TimeBlock(int sizeInMinutes, long timeInMillis) {
            size = sizeInMinutes;
            timeStamp = Calendar.getInstance();
            timeStamp.setTimeInMillis(timeInMillis);
        }

        public TimeBlock(long timeInMillis) {
            size = 0;
            timeStamp = Calendar.getInstance();
            timeStamp.setTimeInMillis(timeInMillis);
        }

        public int size() {
            return this.size;
        }

        public long sizeInMillis() {
            return this.size*60000;
        }

        public long startTime() {
            return this.timeStamp.getTimeInMillis();
        }

        public long endTime() {
            return this.startTime() + this.sizeInMillis();
        }

        public void shrinkSize(int shrinkBy) {
            if (shrinkBy > this.size) {
                this.size = 0;
            } else {
                this.size -= shrinkBy;
            }
        }

        public void setEnd(long timeInMillis) {
            this.size = (int) ((timeInMillis - this.startTime())/60000);
        }

        public void shiftStartTimeByMinutes(int shiftByMinutes) {
            long startTime = this.startTime() + shiftByMinutes*60000;
            this.timeStamp.setTimeInMillis(startTime);
        }

        public boolean deadLineMatches(ListObject listObject) {
            // Strictly less than for some margin
            return (this.endTime() < listObject.getDeadlineMs());
        }

        public TimeBlock split(long timeInMillis) {
            Calendar checker = Calendar.getInstance();
            checker.setTimeInMillis(timeInMillis);

            int diff = (int) (this.endTime() - timeInMillis)/60000;
            this.shrinkSize(diff);
            TimeBlock remaining = new TimeBlock(diff, timeInMillis);
            return remaining;
        }
    }

    /*
    public void addTime(Calendar cal){

        for(int i = cal.get(Calendar.DAY_OF_WEEK); i <= workingDays; i++){
            TimeBlock block = new TimeBlock();
            TimeBlock block2 = new TimeBlock();
            cal.set(HOUR_OF_DAY, startOfDay);
            block.setStartTime(cal);
            block.setBlockSize(firstBlockSize);
            cal.set(HOUR_OF_DAY, endOfLunch);
            block2.setStartTime(cal);
            block2.setBlockSize(secondBlockSize);
            timeBlock.add(block);
            timeBlock.add(block2);
            cal.set(cal.DAY_OF_WEEK, cal.DAY_OF_WEEK + 1);

        }
    }
    */

    // TODO add method that takes a cal for when to start planning (Starkium version, commented out above)
    // TODO check that this sets blocks to sane dates
    // TODO check that this sets this.startOfNext to start of next monday
    public void addTime() {

        // Set nextMonday to point at start of day next monday
        Calendar nextMonday;
        nextMonday = Calendar.getInstance(startOfNext.getTimeZone());
        nextMonday.setTime(startOfNext.getTime());
        // If already on a monday, shift one week ahead
        if (nextMonday.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            nextMonday.add(Calendar.WEEK_OF_YEAR, 1);
        } else {
            while (nextMonday.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                nextMonday.add(DATE, 1);
            }
        }
        // TODO possibly not needed, just here to be safe for now
        nextMonday.set(Calendar.MINUTE, 0);
        nextMonday.set(Calendar.HOUR_OF_DAY, 0);
        nextMonday.set(Calendar.SECOND, 0);

        /*
        /**
         * head should begin at startOfNext, and indicate when we have
         * passed end of week
        Calendar head = Calendar.getInstance(startOfNext.getTimeZone());
        head.setTimeInMillis(startOfNext.getTimeInMillis());
        head.set(Calendar.MINUTE, 0);
        head.set(Calendar.SECOND, 0);
        */

        // TODO make sure that startOfNext doesn't start in lunch hours
        TimeBlock newBlock;
        while (startOfNext.getTimeInMillis() < nextMonday.getTimeInMillis()) {
            newBlock = new TimeBlock(startOfNext.getTimeInMillis());
            newBlock.setEnd(restrictions.getEndOfCurrent(startOfNext));
            timeBlocks.add(newBlock);
            long sON = restrictions.getStartOfNext(startOfNext);
            startOfNext.setTimeInMillis(sON);
        }

            startOfNext = nextMonday;
    }

    public TimeBlock generateTimeBlock(int size, long startTime) {
        return new TimeBlock(size, startTime);
    }

    public TimeBlock generateTimeBlock(long startTime) {
        return new TimeBlock(startTime);
    }

    public TimeBlock makeTimeBlock(){
        TimeBlock timeBlock = new TimeBlock(60, System.currentTimeMillis());
        return timeBlock;
    }
}

package se.kth.g2.planner;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Anna on 2017-05-05.
 */
public class ScheduleObjectTest {
    @Test
    public void setSize() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        s1.setSize(100);
        s2.setSize(120);

        System.out.println("SIZE OF BLOCK NR 1:" + " " + s1.getSize());
        System.out.println("SIZE OF BLOCK NR 2" + " " + s2.getSize());

    }

    @Test
    public void setID() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        e1.setID(1);
        e2.setID(2);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        s1.setID(e1.getID());
        s2.setID(e2.getID());

        System.out.println("ID 1:" + " " + s1.getID());
        System.out.println("ID 2:" + " " + s2.getID());

    }

    @Test
    public void setComparableStartTime() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        s1.setComparableStartTime(14939);
        s2.setComparableStartTime(1493991109);

        System.out.println("START:" + " " + s1.getComparableStartTime());
        System.out.println("START:" + " " + s2.getComparableStartTime());

    }

    @Test
    public void setComparableEndTime() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        s1.setComparableEndTime(14939);
        s2.setComparableEndTime(1494231533378L);

        System.out.println("END:" + " " + s1.getComparableEndTime());
        System.out.println("END:" + " " + s2.getComparableEndTime());

    }

    @Test
    public void getSize() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("SIZE 1:" + " " + s1.getSize());
        System.out.println("SIZE 2:" + " " + s2.getSize());

    }

    @Test
    public void getID() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);
        e1.setID(1);
        e2.setID(2);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("ID 1:" + " " + s1.getID());
        System.out.println("ID 2:" + " " + s2.getID());

    }

    @Test
    public void getStartYear() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("START YEAR OBJ 1:" + " " + s1.getStartYear());
        System.out.println("START YEAR OBJ 2:" + " " + s2.getStartYear());

    }

    @Test
    public void getStartMonth() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("START MONTH OBJ 1:" + " " + s1.getStartMonth()+1);
        System.out.println("START MONTH OBJ 2:" + " " + s2.getStartMonth()+1);


    }

    @Test
    public void getStartDay() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("START DAY OBJ 1:" + " " + s1.getStartDay());
        System.out.println("START DAY OBJ 2:" + " " + s2.getStartDay());


    }

    @Test
    public void getStartHour() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("START HOUR OBJ 1:" + " " + s1.getStartHour());
        System.out.println("START HOUR OBJ 2:" + " " + s2.getStartHour());

    }

    @Test
    public void getStartMinute() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("START MINUTE OBJ 1:" + " " + s1.getStartMinute());
        System.out.println("START MINUTE OBJ 2:" + " " + s2.getStartMinute());

    }

    @Test
    public void getComparableStartTime() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("START TIME OBJ 1:" + " " + s1.getComparableStartTime());
        System.out.println("START TIME OBJ 2:" + " " + s2.getComparableStartTime());

    }

    @Test
    public void getComparableEndTime() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("END TIME OBJ 1:" + " " + s1.getComparableEndTime());
        System.out.println("END TIME OBJ 2:" + " " + s2.getComparableEndTime());

    }

    @Test
    public void getEndYear() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("END YEAR OBJ 1:" + " " + s1.getEndYear());
        System.out.println("END YEAR OBJ 2:" + " " + s2.getEndYear());

    }

    @Test
    public void getEndMonth() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("END MONTH OBJ 1:" + " " + s1.getEndMonth()+1);
        System.out.println("END MONTH OBJ 2:" + " " + s2.getEndMonth()+1);

    }

    @Test
    public void getEndDay() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("END DAY OBJ 1:" + " " + s1.getEndDay());
        System.out.println("END DAY OBJ 2:" + " " + s2.getEndDay());

    }

    @Test
    public void getEndHour() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("END HOUR OBJ 1:" + " " + s1.getEndHour());
        System.out.println("END HOUR OBJ 2:" + " " + s2.getEndHour());

    }

    @Test
    public void getEndMinute() throws Exception {
        ListObject e1 = new ListObject("Sleep", 60);
        ListObject e2 = new ListObject("JENS SUGER", 120);

        TimePool tp = new TimePool();
        TimePool.TimeBlock tb = tp.makeTimeBlock();

        ScheduleObject s1 = new ScheduleObject(e1, tb);
        ScheduleObject s2 = new ScheduleObject(e2, tb);

        System.out.println("END MINUTE OBJ 1:" + " " + s1.getEndMinute());
        System.out.println("END MINUTE OBJ 2:" + " " + s2.getEndMinute());

    }

}
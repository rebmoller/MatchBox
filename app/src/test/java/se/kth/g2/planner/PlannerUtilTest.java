package se.kth.g2.planner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

import se.kth.g2.planner.DatabaseContract;
/**
 * Created by J2DX on 2017-05-09.
 */
public class PlannerUtilTest {
    ArrayList<ListObject> listObjects = new ArrayList<>();
    ListObject listObject;

    @Before
    public void setUp() throws Exception {
        listObject = DatabaseContract.mockListObject1();
        listObject.setID(1);
        listObjects.add(listObject);

        listObject = DatabaseContract.mockListObject1();
        listObject.setID(2);
        listObjects.add(listObject);

        listObject = DatabaseContract.mockListObject1();
        listObject.setID(3);
        listObjects.add(listObject);

        listObject = DatabaseContract.mockListObject1();
        listObject.setID(4);
        listObjects.add(listObject);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void objectIdBinarySearch() throws Exception {
        int resultIndex = PlannerUtil.objectIdBinarySearch(listObjects, 1);
        assertEquals(0, resultIndex);
        resultIndex = PlannerUtil.objectIdBinarySearch(listObjects, 2);
        assertEquals(1, resultIndex);
        resultIndex = PlannerUtil.objectIdBinarySearch(listObjects, 3);
        assertEquals(2, resultIndex);
        resultIndex = PlannerUtil.objectIdBinarySearch(listObjects, 4);
        assertEquals(3, resultIndex);
        resultIndex = PlannerUtil.objectIdBinarySearch(listObjects, 5);
        assertEquals(-1, resultIndex);


        listObjects.get(0).setID(3);
        listObjects.get(1).setID(5);
        listObjects.get(2).setID(8);
        listObjects.get(3).setID(25);
        resultIndex = PlannerUtil.objectIdBinarySearch(listObjects, 8);
        assertEquals(2, resultIndex);
    }

}
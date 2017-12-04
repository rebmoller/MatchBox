package se.kth.g2.planner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Jens on 2017-04-28.
 */
@RunWith(MockitoJUnitRunner.class)
public class TodoListViewTest {
    ListObject item1;
    ListObject item2;

    ArrayList<ListObject> list;

    @Before
    public void testEventOne(){
        item1 = new ListObject();

        item1.setPlanned(true);
        item1.setDeadline(false);
        item1.setTitle("Jens testar");
        item1.setEstimate(100);
        item1.setID(1337);

        list = new ArrayList<ListObject>();
        list.add(item1);

    }
    @Before
    public void testChanges(){
        Long ID = item1.getID();
        list.remove(0);

        item1.setPlanned(false);
        item1.setDeadline(true);
        item1.setTitle("Jens testar");
        item1.setEstimate(100);
        item1.setID(ID);

        list.add(item1);
    }

    @Test
    public void test1(){
        //assertEquals(item1.getID(),1234);
        assertEquals(item1.getID(), 1337);
    }
}

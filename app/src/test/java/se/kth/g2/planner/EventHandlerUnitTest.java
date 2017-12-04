package se.kth.g2.planner;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by sieken on 2017-04-20.
 */

public class EventHandlerUnitTest {
    EventHandler eh = EventHandler.getInstance();

    @Test
    public void getInstanceTest() throws Exception {
        EventHandler ehTest = EventHandler.getInstance();
        assertEquals(eh, ehTest);
    }
}

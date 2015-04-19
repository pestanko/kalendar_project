
package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.EventAlreadyExists;

import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class EventManagerImplTest extends TestCase {
    EventManagerImpl eventManager;


    @Before
    public void setUp() throws Exception {
        eventManager = new EventManagerImpl();
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"), Event.dateFormat.parse("14-03-2015 03:25:00"));
        Event fridayEvent2 = new Event("FridayEvent", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        Event fridayEvent3 = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"), Event.dateFormat.parse("14-03-2015 03:25:00"));
        eventManager.addEvent(fridayEvent);
        eventManager.addEvent(fridayEvent2);
        eventManager.addEvent(fridayEvent3);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddEvent() throws Exception {


        assertThat(3L, is(equalTo(eventManager.size())));

        try {
            Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                    Event.dateFormat.parse("13-03-2015 18:20:56"), Event.dateFormat.parse("14-03-2015 03:25:00"));
            fridayEvent.setId(1);
            eventManager.addEvent(fridayEvent);
            Assert.fail("Adding event with existing ID.");
        } catch (EventAlreadyExists ex) {

        }


    }


    @Test
    public void testRemoveEvent() throws Exception {

        Event event2 = eventManager.getEvent(2);
        eventManager.removeEvent(event2.getId());
        Event event = eventManager.getEvent(2);
        assertThat(event, is(sameInstance(null)));


    }

    @Test
    public void testGetEvent() throws Exception {
        Event fridayEvent = eventManager.getEvent(1);
        Event event = eventManager.getEvent(fridayEvent.getId());
        assertThat(fridayEvent, is(equalTo(event)));

        event = eventManager.getEvent(-1);
        if (event == null) return;
        Assert.fail("Find non existing user");

    }

    @Test
    public void testFindCurrentEvents() throws Exception{
        if(eventManager.getEvent(1).isNowActive()) Assert.fail("This event is not currently running");
        if(!eventManager.getEvent(2).isNowActive()) Assert.fail("This event is currently running");
        if(eventManager.getEvent(3).isNowActive()) Assert.fail("This event is not currently running");
    }

    @Test
    public void testGetAllEvents() throws Exception {
        List<Event> events = eventManager.getAllEvents();
        if (!events.contains(eventManager.getEvent(2)))
        Assert.fail("Container of events doesn't contain event with ID 2 but it should");
    }

    @Test
    public void testEditEvent() throws Exception {
        Event event = eventManager.getEvent(2);
        event.setName("Sunday Event");
        event.setDescription("Meeting at local church");
        assertThat(event, is(sameInstance(eventManager.getEvent(2))));
        if (!event.getName().equals("Sunday Event")) Assert.fail("Event doesn't update correctly");
    }

    @Test
    public void testFindEventInTimePeriod() throws Exception {
        Date dateBegin = EventManagerImpl.dateFormat.parse("13-03-2015 17:20:56");
        Date dateEnd = EventManagerImpl.dateFormat.parse("14-03-2015 04:25:00");
        Event event = eventManager.getEvent(1);
        if (event.getDateBegin() == null || event.getDateEnd() == null) Assert.fail("Begin or end date of event is null");
        if(!event.getDateBegin().after(dateBegin) && !event.getDateEnd().before(dateEnd)) Assert.fail("Event's begin and end time should satisfies");
        event = eventManager.getEvent(2);
        if (event.getDateBegin() == null || event.getDateEnd() == null) Assert.fail("Begin or end date of event is null");
        if(!event.getDateBegin().after(dateBegin) && !event.getDateEnd().before(dateEnd)) Assert.fail("Event's begin and end time should NOT satisfies");

    }

    @Test
    public void testSize() throws Exception {
        assertThat(((long) eventManager.getAllEvents().size()), is(equalTo(eventManager.size())));
        assertThat(3L, is(equalTo(eventManager.size())));
    }

}
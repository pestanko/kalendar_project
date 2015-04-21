package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import javax.sql.DataSource;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

public class EventManagerDBTest {

    private EventManager eventManager;
    private DataSource dataSource = null;

    @Before
    public void setUp() throws Exception {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:derby:memory:EventManagerTest;create=true");
        dataSource = bds;

        try (Connection connection = bds.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(Event.class, "CREATE")).executeUpdate();
        }
        eventManager = new EventManagerDB(bds);

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
    public void destroy_db() throws Exception
    {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(Event.class, "DROP")).executeUpdate();
        }

    }

    @Test
    public void testAddEvent() throws Exception {
        assertThat(2L, is(equalTo(eventManager.getEvent(2).getId())));
        assertThat(3L, is(equalTo(eventManager.size())));
    }

    @Test
    public void testRemoveEvent() throws Exception {
        eventManager.removeEvent(eventManager.getEvent(2).getId());
        Event event = eventManager.getEvent(2);
        assertThat(event, is(sameInstance(null)));

    }

    @Test
    public void testGetEvent() throws Exception {

        assertThat(eventManager.getEvent(2), is(equalTo(eventManager.getEvent(eventManager.getEvent(2).getId()))));
        assertThat(eventManager.getEvent(1).getName(), is(equalTo(eventManager.getEvent(eventManager.getEvent(1).getId()).getName())));
        if (eventManager.getEvent(-1) == null) return;
        Assert.fail("Find non existing user");

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
        eventManager.updateEvent(event);
        assertThat(event, is(equalTo(eventManager.getEvent(2))));
        if (!event.getName().equals("Sunday Event")) Assert.fail("Event doesn't update correctly");
    }

    @Test
    public void testFindCurrentEvents() throws Exception {
        if(eventManager.getEvent(1).isNowActive()) Assert.fail("This event is not currently running");
        if(!eventManager.getEvent(2).isNowActive()) Assert.fail("This event is currently running");
        if(eventManager.getEvent(3).isNowActive()) Assert.fail("This event is not currently running");
    }


    @Test
    public void testSize() throws Exception {
        Assert.assertEquals("Size is not 3", 3, eventManager.size());
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
}
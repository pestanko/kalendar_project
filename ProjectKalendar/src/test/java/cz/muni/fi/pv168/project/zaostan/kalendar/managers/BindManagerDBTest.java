package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class BindManagerDBTest {
    private BindManager bindManager;
    private UserManager userManager;
    private EventManager eventManager;

    private DataSource dataSource = null;

    @Before
    public void setUp() throws Exception {


        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:derby:memory:UserManagerTest;create=true");

        dataSource = bds;

        //creating new empty table
        try (Connection connection = bds.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(User.class, "CREATE")).executeUpdate();
            connection.prepareStatement(FileUtils.readSqlFile(Event.class, "CREATE")).executeUpdate();
            connection.prepareStatement(FileUtils.readSqlFile(Bind.class, "CREATE")).executeUpdate();

        }

        bindManager = new BindManagerDB(dataSource);
        eventManager = new EventManagerDB(dataSource);
        userManager = new UserManagerDB(dataSource);
    }

    @After
    public void tearDown() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(User.class, "DROP")).executeUpdate();
            connection.prepareStatement(FileUtils.readSqlFile(Event.class, "DROP")).executeUpdate();
            connection.prepareStatement(FileUtils.readSqlFile(Bind.class, "DROP")).executeUpdate();

        }
    }

    @Test
    public void testAddBinding() throws Exception
    {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        User fero = new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost");
        userManager.addUser(fero);
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                EventManagerImpl.dateFormat.parse("13-03-2015 18:20:56"),
                EventManagerImpl.dateFormat.parse("14-03-2015 03:25:00"));
        eventManager.addEvent(fridayEvent);

        Bind binding = new Bind(user, fridayEvent);
        Bind binding_fero = new Bind(fero, fridayEvent);
        bindManager.addBinding(binding);
        bindManager.addBinding(binding_fero);
        Assert.assertTrue("Binding has not set id = 1", binding.getId() == 1);


    }

    @Test
    public void testGetBinding() throws Exception
    {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                EventManagerImpl.dateFormat.parse("13-03-2015 18:20:56"),
                EventManagerImpl.dateFormat.parse("14-03-2015 03:25:00"));
        eventManager.addEvent(fridayEvent);

        Bind binding = new Bind(user, fridayEvent);
        bindManager.addBinding(binding);

        User fero = new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost");
        userManager.addUser(fero);
        Bind binding_fero = new Bind(fero, fridayEvent);
        bindManager.addBinding(binding_fero);

        Bind result = bindManager.getBinding(1);
        Assert.assertEquals("Bindings are not equals on users", result.getUser(), binding.getUser());
        Assert.assertEquals("Bindings are not equals on events", result.getEvent(), binding.getEvent());
        Bind res2 = bindManager.getBinding(2);
        Assert.assertEquals("Binding for user Fero is not equals.", fero, res2.getUser());
    }

    @Test
    public void testRemoveBinding() throws Exception
    {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                EventManagerImpl.dateFormat.parse("13-03-2015 18:20:56"),
                EventManagerImpl.dateFormat.parse("14-03-2015 03:25:00"));
        eventManager.addEvent(fridayEvent);

        Bind binding = new Bind(user, fridayEvent);
        bindManager.addBinding(binding);

        bindManager.removeBinding(binding.getId());
        Bind res = bindManager.getBinding(binding.getId());
        Assert.assertNull("Remove binding was not successful.", res);
    }

    @Test
    public void testUpdateBinding() throws Exception
    {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"),
                Event.dateFormat.parse("14-03-2015 03:25:00"));
        eventManager.addEvent(fridayEvent);
        Bind binding = new Bind(user, fridayEvent);
        bindManager.addBinding(binding);

        binding.setType(Bind.BindType.OWNER);
        bindManager.updateBinding(binding);

        Bind res = bindManager.getBinding(binding.getId());
        Assert.assertEquals("After update bindings are not equals.", binding, res);

    }


    @Test
    public void testFindEventsWhereIsUser() throws Exception {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        User user2 = new User("Jano", "Kral", "xkral", "xkral@nolhy.local");
        userManager.addUser(user2);
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"),
                Event.dateFormat.parse("14-03-2015 03:25:00"));

        Event fridayEvent2 = new Event("FridayEventCurrent", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        Event fridayEvent3 = new Event("FridayEventSuperJano", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        eventManager.addEvent(fridayEvent);
        eventManager.addEvent(fridayEvent2);
        eventManager.addEvent(fridayEvent3);
        Bind binding = new Bind(user, fridayEvent);
        Bind b2 = new Bind(user, fridayEvent2);
        Bind bjano = new Bind(user2, fridayEvent3);
        Bind bjano2 = new Bind(user2, fridayEvent);
        bindManager.addBinding(binding);
        bindManager.addBinding(b2);
        bindManager.addBinding(bjano);
        bindManager.addBinding(bjano2);

        List<Event> res = bindManager.findEventsWhereIsUser(user);
        Assert.assertTrue("Not same size.", res.size() == 2);
        Assert.assertTrue("Not containining Current.", res.contains(fridayEvent2));
        Assert.assertTrue("Not containining not current", res.contains(fridayEvent));


    }

    @Test
    public void testFindEventsWhereIsUser1() throws Exception {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        User user2 = new User("Jano", "Kral", "xkral", "xkral@nolhy.local");
        userManager.addUser(user2);
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"),
                Event.dateFormat.parse("14-03-2015 03:25:00"));

        Event fridayEvent2 = new Event("FridayEventCurrent", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        Event fridayEvent3 = new Event("FridayEventSuperJano", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        eventManager.addEvent(fridayEvent);
        eventManager.addEvent(fridayEvent2);
        eventManager.addEvent(fridayEvent3);
        Bind binding = new Bind(user, fridayEvent);
        binding.setType(Bind.BindType.OWNER);
        Bind b2 = new Bind(user, fridayEvent2);
        Bind bjano = new Bind(user2, fridayEvent3);
        Bind bjano2 = new Bind(user2, fridayEvent);
        bindManager.addBinding(binding);
        bindManager.addBinding(b2);
        bindManager.addBinding(bjano);
        bindManager.addBinding(bjano2);

        List<Event> res = bindManager.findEventsWhereIsUser(user, Bind.BindType.OWNER);
        Assert.assertNotNull("Result is empty", res);
        Assert.assertTrue("Not same size.", res.size() == 1);
        Assert.assertTrue("Containining Current.", !res.contains(fridayEvent2));
        Assert.assertTrue("Not containining not current", res.contains(fridayEvent));
    }

    @Test
    public void testFindUpcommingEvents() throws Exception {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        User user2 = new User("Jano", "Kral", "xkral", "xkral@nolhy.local");
        userManager.addUser(user2);
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"),
                Event.dateFormat.parse("14-03-2015 03:25:00"));

        Event fridayEvent2 = new Event("FridayEventCurrent", "Event at Irish pub",
                new DateTime().plusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        Event fridayEvent3 = new Event("FridayEventSuperJano", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        eventManager.addEvent(fridayEvent);
        eventManager.addEvent(fridayEvent2);
        eventManager.addEvent(fridayEvent3);
        Bind binding = new Bind(user, fridayEvent);
        binding.setType(Bind.BindType.OWNER);
        Bind b2 = new Bind(user, fridayEvent2);
        Bind bjano = new Bind(user2, fridayEvent3);
        Bind bjano2 = new Bind(user2, fridayEvent);
        bindManager.addBinding(binding);
        bindManager.addBinding(b2);
        bindManager.addBinding(bjano);
        bindManager.addBinding(bjano2);

        List<Event> res = bindManager.findUpcommingEvents(user);
        Assert.assertNotNull("Result is empty", res);
        Assert.assertTrue("Not same size.", res.size() == 1);
        Assert.assertTrue("Not Containining Current.", res.contains(fridayEvent2));
        Assert.assertTrue("Containining not current.", !res.contains(fridayEvent));
    }

    @Test
    public void testFindCurrentEvents() throws Exception {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        User user2 = new User("Jano", "Kral", "xkral", "xkral@nolhy.local");
        userManager.addUser(user2);
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"),
                Event.dateFormat.parse("14-03-2015 03:25:00"));

        Event fridayEvent2 = new Event("FridayEventCurrent", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        Event fridayEvent3 = new Event("FridayEventSuperJano", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        eventManager.addEvent(fridayEvent);
        eventManager.addEvent(fridayEvent2);
        eventManager.addEvent(fridayEvent3);
        Bind binding = new Bind(user, fridayEvent);
        binding.setType(Bind.BindType.OWNER);
        Bind b2 = new Bind(user, fridayEvent2);
        Bind bjano = new Bind(user2, fridayEvent3);
        Bind bjano2 = new Bind(user2, fridayEvent);
        bindManager.addBinding(binding);
        bindManager.addBinding(b2);
        bindManager.addBinding(bjano);
        bindManager.addBinding(bjano2);

        List<Event> res = bindManager.findCurrentEvents(user);
        Assert.assertNotNull("Result is empty", res);
        Assert.assertTrue("Not same size.", res.size() == 1);
        Assert.assertTrue("Not Containining Current.", res.contains(fridayEvent2));
        Assert.assertTrue("Containining not current.", !res.contains(fridayEvent));

    }

    @Test
    public void testFindUsersInEvent() throws Exception
    {
        User user = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        User user2 = new User("Jano", "Kral", "xkral", "xkral@nolhy.local");
        userManager.addUser(user2);
        userManager.addUser(user);
        Event fridayEvent = new Event("FridayEvent", "Event at Irish pub",
                Event.dateFormat.parse("13-03-2015 18:20:56"),
                Event.dateFormat.parse("14-03-2015 03:25:00"));

        Event fridayEvent2 = new Event("FridayEventCurrent", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        Event fridayEvent3 = new Event("FridayEventSuperJano", "Event at Irish pub",
                new DateTime().minusHours(1).toDate(), new DateTime().plusDays(1).toDate());
        eventManager.addEvent(fridayEvent);
        eventManager.addEvent(fridayEvent2);
        eventManager.addEvent(fridayEvent3);
        Bind binding = new Bind(user, fridayEvent);
        binding.setType(Bind.BindType.OWNER);
        Bind b2 = new Bind(user, fridayEvent2);
        Bind bjano = new Bind(user2, fridayEvent3);
        Bind bjano2 = new Bind(user2, fridayEvent);
        bindManager.addBinding(binding);
        bindManager.addBinding(b2);
        bindManager.addBinding(bjano);
        bindManager.addBinding(bjano2);

        List<User> users = bindManager.findUsersInEvent(fridayEvent);
        Assert.assertTrue("Size is not same.", users.size() == 2);
        Assert.assertTrue("Not contains Peter.", users.contains(user));
        Assert.assertTrue("Not contains Jano.", users.contains(user2));

    }
}
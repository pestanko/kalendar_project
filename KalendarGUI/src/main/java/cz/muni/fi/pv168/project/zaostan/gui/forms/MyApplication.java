package cz.muni.fi.pv168.project.zaostan.gui.forms;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.*;
import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by wermington on 3.5.2015.
 */
public class MyApplication {
    private static UserManager userManager;
    private static EventManager eventManager;
    private static BindManager bindManager;
    final static Logger logger = LoggerFactory.getLogger(MyApplication.class);


    public static void init() throws Exception {
        BasicDataSource bds = new BasicDataSource();
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bds.setUrl("jdbc:derby:memory:Kalendar;create=true");
        initUsers(bds);
        initEvents(bds);
        initBinds(bds);
    }

    private static void initBinds(BasicDataSource bds) throws Exception {
        FileUtils.createBindsTable(bds);
        bindManager = new BindManagerDB(bds);
    }

    private static void initEvents(BasicDataSource bds) throws Exception {
        FileUtils.createEventsTable(bds);
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


    private static void initUsers(DataSource bds) throws Exception {
        FileUtils.createUsersTable(bds);
        userManager = new UserManagerDB(bds);
        logger.debug("Creating users");
        userManager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        User miso = new User("Michal", "Joodla", "xmichal", "michal.joodla@localhost.edu");
        userManager.addUser(miso);
        userManager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        userManager.addUser(new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost"));
    }


    public static UserManager getUserManager() {
        return userManager;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static BindManager getBindManager() {
        return bindManager;
    }
}

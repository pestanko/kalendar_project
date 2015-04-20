package cz.muni.fi.pv168.project.zaostan.kalendar.server;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.EventExceptionDB;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.*;
import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Peter Stanko
 * @version 17.4.2015
 */
@WebListener
public class StartListener implements ServletContextListener {
    final static Logger logger = LoggerFactory.getLogger(StartListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent ev) {
        ServletContext servletContext = ev.getServletContext();

        BasicDataSource bds = new BasicDataSource();
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bds.setUrl("jdbc:derby:memory:Kalendar;create=true");

        UserManager userManager = new UserManagerDB(bds);
        EventManager eventManager = new EventManagerDB(bds);
        BindManager bindManager = new BindManagerDB(bds);
        servletContext.setAttribute(UsersServlet.USER_MANAGER, userManager);
        servletContext.setAttribute("EventManager", eventManager);
        servletContext.setAttribute("BindManager", bindManager);

        try {
            createDatabases(bds);
            createUsers(userManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }


    private void createDatabases(BasicDataSource bds) throws Exception {
        FileUtils.createUsersTable(bds);
        FileUtils.createBindsTable(bds);
        FileUtils.createEventsTable(bds);

    }

    private void createUsers(UserManager userManager) throws UserException
    {
        logger.debug("Creating users");
        userManager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        User miso = new User("Michal", "Joodla", "xmichal", "michal.joodla@localhost.edu");
        userManager.addUser(miso);
        userManager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        userManager.addUser(new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost"));
    }



    private void createEvents(EventManager eventManager) throws Exception
    {
        throw new NotImplementedException();
    }


    private void createBinds(BindManager bindManager, UserManager userManager, EventManager eventManager)
    {
        logger.debug("Creating Binds.");

    }
}

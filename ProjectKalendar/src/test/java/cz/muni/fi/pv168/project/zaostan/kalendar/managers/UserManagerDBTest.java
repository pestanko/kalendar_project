package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;


import javax.sql.DataSource;

import java.sql.Connection;
import java.util.List;

public class UserManagerDBTest {

    private UserManager userManager;

    private DataSource dataSource = null;

    @Before
    public void setUp() throws Exception {


        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:derby:memory:UserManagerTest;create=true");

        dataSource = bds;

        //creating new empty table
        try (Connection connection = bds.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(User.class, "CREATE")).executeUpdate();
        }

        userManager = new UserManagerDB(bds);
    }

    @After
    public void destroy_db() throws Exception
    {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(User.class, "DROP")).executeUpdate();
        }

    }

    @Test
    public void testAddUser() throws Exception {
        userManager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        User miso = new User("Michal", "Joodla", "xmichal", "michal.joodla@localhost.edu");
        userManager.addUser(miso);
        userManager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        userManager.addUser(new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost"));



        long res_id = miso.getId();
        Assert.assertTrue("Id of object and id in database are not the same.", res_id == 2);

        try
        {
            userManager.addUser(new User("Samuel", "Localhost", "xmichal", "xlokal@foo.com"));
            Assert.fail("Adding user with existing username.");
        }catch (UserException ex)
        {
        }
    }

    @Test
    public void testGetUser() throws Exception
    {
        final User orig = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(orig);

        User user = userManager.getUser(1);
        Assert.assertNotNull("User with id 1 is null -> not exitst -> that is wrong.");
        Assert.assertEquals("Users are not same.", orig, user );

        user = userManager.getUser(-1);
        Assert.assertNull("Received user which not exits", user);

        user = userManager.getUser(1000);
        Assert.assertNull("Received user which not exits", user);


    }

    @Test
    public void testRemoveUser() throws Exception
    {
        userManager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        userManager.addUser(new User("Jano", "Obsival", "xobsival", "obsival@localhost"));
        userManager.addUser(new User("Johan", "Joodla", "xjoodla", "jan.joodla@localhost.edu"));
        userManager.removeUser(1);

        try {
            User user = userManager.getUser(1);
            Assert.assertNull("User with id=1 still exists in database. ", user);
        }catch (UserException ex)
        {
            ex.printStackTrace();
        }
    }

    @Test
    public void testUpdateUser() throws Exception
    {
        User orig = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(orig);


        User user = userManager.getUser(orig.getId());
        user.setAddress("somewhere");
        user.setLastName("Stanko2");
        userManager.updateUser(user);

        User res = userManager.getUser(user.getId());
        Assert.assertEquals(user, res);

        if(res.getLastName().equals("Stanko2"));

    }

    @Test
    public void testFindByName() throws Exception
    {
        User orig = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(orig);
        userManager.addUser(new User("Jano", "Obsival", "xobsival", "obsival@localhost"));
        userManager.addUser(new User("Johan", "Joodla", "xjoodla", "jan.joodla@localhost.edu"));
        userManager.addUser(new User("Peter", "Stefanovsky", "xstefan", "stefapet@localhost"));
        List<User> users = userManager.findByFirstName("Peter");
        Assert.assertTrue("Wrong number of users", users.size() == 2);

        try
        {
            users = userManager.findByFirstName("Xena");
            Assert.assertNull("Found not existing user with name Xena.", users);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testFindByEmail() throws Exception
    {
        User orig = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(orig);
        userManager.addUser(new User("Jano", "Obsival", "xobsival", "obsival@localhost"));
        userManager.addUser(new User("Johan", "Joodla", "xjoodla", "jan.joodla@localhost.edu"));

        List<User> users = userManager.findByEmail("stanko@localhost");
        Assert.assertTrue("Wrong number of users. ", users.size() == 1);

        try
        {
            users = userManager.findByEmail("xstanko2@localhost");
            Assert.assertNull("Found not existing user with email xstanko2@localhost", users);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void testFindByUserName() throws Exception {

        User orig = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(orig);
        userManager.addUser(new User("Jano", "Obsival", "xobsival", "obsival@localhost"));
        userManager.addUser(new User("Johan", "Joodla", "xjoodla", "jan.joodla@localhost.edu"));

        User users = userManager.findByUserName("xstanko2");
        Assert.assertTrue("User is not the same.", users.getId() == 1);

        users = userManager.findByUserName("xsupermario");
        Assert.assertNull("Found not existing user with user name xsupermario.", users);

    }

    @Test
    public void testFindBySurName() throws Exception {

        User orig = new User("Peter", "Stanko", "xstanko2", "stanko@localhost");
        userManager.addUser(orig);
        userManager.addUser(new User("Jano", "Obsival", "xobsival", "obsival@localhost"));
        userManager.addUser(new User("Johan", "Joodla", "xjoodla", "jan.joodla@localhost.edu"));

        List<User> users = userManager.findByLastName("Joodla");
        Assert.assertTrue("Wrong number of users. ", users.size() == 1);

        try
        {
            users = userManager.findByFirstName("Lolofon");
            Assert.assertNull("Found not existing user with sur name Lolofon", users);
        }catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Test
    public void testGetAllUsers() throws Exception {
        userManager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        User miso = new User("Michal", "Joodla", "xmichal", "michal.joodla@localhost.edu");
        userManager.addUser(miso);
        userManager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        userManager.addUser(new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost"));

        List<User> users = userManager.getAllUsers();

        Assert.assertTrue("All users not contains michal ", users.contains(miso));


    }

    @Test
    public void testSize() throws Exception {
        userManager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        User miso = new User("Michal", "Joodla", "xmichal", "michal.joodla@localhost.edu");
        userManager.addUser(miso);
        userManager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        userManager.addUser(new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost"));

        List<User> users = userManager.getAllUsers();
        Assert.assertEquals("The size of set returned by getAllUsers is not the same as Database count", users.size(), userManager.size());
        Assert.assertTrue("Size is not same.", userManager.size() == 4);

    }
}
package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;



public class UserManagerImplTest {

    private UserManager manager = new UserManagerImpl();

    @Before
    public void setUp() throws Exception {
        manager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        manager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        manager.addUser(new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost"));
        manager.addUser(new User("Jano", "Obsival", "xobsival", "obsival@localhost"));
        manager.addUser(new User("Johan", "Joodla", "xjoodla", "jan.joodla@localhost.edu"));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddUser() throws Exception {

        // Add unsuccesful
        try
        {
            manager.addUser(new User("Samuel", "Localhost", "xstanko2", "xlokal@foo.com"));
            Assert.fail("Adding user with existing username.");
        }catch (UserException ex)
        {
            // Do nothing
        }
    }

    @Test
    public void testGetUser() throws Exception {
        // CORRECT
        User user = manager.getUser(2);
        Assert.assertNotEquals("Cannot find user by id 2", null, user);

        // Not existing id
        user = manager.getUser(-1);
        Assert.assertNull("Found user with non existing id.", user);

    }


    @Test
    public void testRemoveUser() throws Exception {
        // 10
        List<User> users = manager.findByEmail("obsival@localhost");
        long id = users.get(0).getId();
        manager.removeUser(id);
        User user2 = manager.getUser(id);
        Assert.assertTrue("User with id 10 still exists.", user2 == null);

        // 100
        try{

            User user = manager.getUser(-1);
            if(user == null) return;
            Assert.assertNull("Removing with user id 100 is throwing exception", user);
        }catch (UserException exception) {}


    }

    @Test
    public void testFindByName() throws Exception {
        List<User> users = manager.findByFirstName("Peter");
        Assert.assertTrue("Wrong number of users", users.size() == 2);

        try
        {
            users = manager.findByFirstName("Xena");
            Assert.assertNull("Found not existing user with name Xena.", users);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testFindByEmail() throws Exception {
        List<User> users = manager.findByEmail("stanko@localhost");
        Assert.assertTrue("Wrong number of users. ", users.size() == 1);

        try
        {
            users = manager.findByEmail("xstanko2@localhost");
            Assert.assertNull("Found not existing user with email xstanko2@localhost", users);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void testFindByUserName() throws Exception {
        User users = manager.findByUserName("xstanko2");
        Assert.assertTrue("User is not the same.", users.getId() == 1);

        users = manager.findByUserName("xsupermario");
        Assert.assertNull("Found not existing user with user name xsupermario.", users);




    }

    @Test
    public void testFindBySurName() throws Exception {
        List<User> users = manager.findByLastName("Zaoral");
        Assert.assertTrue("Wrong number of users. ", users.size() == 1);

        try
        {
            users = manager.findByFirstName("Lolofon");
            Assert.assertNull("Found not existing user with sur name Lolofon", users);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
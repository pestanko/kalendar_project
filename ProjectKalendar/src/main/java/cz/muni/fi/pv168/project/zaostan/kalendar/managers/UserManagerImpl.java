package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserAlreadyExitsException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Stanko
 * @version 2015-02-24
 */
public class UserManagerImpl implements UserManager
{
    private Set<User> usersCollection = new HashSet<User>();
    private long index = 1;

    private static Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);


    @Override
    public void addUser(User user) throws UserException
    {
        if(user.getId() == 0)
        {
            if(findByUserName(user.getUserName()) != null)
            {
                logger.warn("(addUser) User with username \""+user.getUserName() +"\" already exists.");
                throw new UserAlreadyExitsException("Username: " + user.getUserName() + " already exists.");
            }
            user.setId(index++);
        }

        User exist = getUser(user.getId());
        if(exist != null)
        {
            logger.warn("(addUser) User with id \"" + user.getId() + "\" already exists.");
            throw new UserAlreadyExitsException("User with id \"" + user.getId() + "\" already exists." );
        }
        usersCollection.add(user);
        logger.debug("(addUser) Added " + user);
    }

    @Override
    public User getUser(long id) throws UserException
    {
            List<User> userStream = usersCollection.stream().filter((User user) -> user.getId() == id).collect(Collectors.toList());
            if(userStream.size() != 0) {
                return userStream.get(0);
            }
            //throw new UserNotExistException("User with id " + id + " not exists.", ex);
            logger.info("(getUser) User with id " + id + " not exitsts.");
            return null;
    }


    @Override
    public void removeUser(long id) throws UserException {
        User user = getUser(id);
        if (user == null)
        {
            logger.warn("(removeUser) User with id " + id + " not exitsts.");
            throw new UserNotExistException("User with id " + id  + " not exists.");
        }


        usersCollection.remove(user);
        logger.info("(removeUser) User with id " + id + " was successfuly removed.");
    }

    @Override
    public void updateUser(User user) throws UserException {
        throw new UnsupportedOperationException("Cannot update user in current state");
    }


    @Override
    public List<User> findByFirstName(String name) throws UserException
    {
        List<User> users =   usersCollection.stream().filter((User user) -> user.getFirstName().equals(name) ).collect(Collectors.toList());
        if (users.size() == 0) {
            logger.info("(findByFirstName) No user with name \"" + name + "\" not exists.");
            return null;
        }
        else return users;
    }

    @Override
    public List<User> findByEmail(String email) throws UserException
    {
        List<User> users = usersCollection.stream().filter((User user) -> user.getEmail().equals(email)).collect(Collectors.toList());
        if (users.size() == 0) {
            logger.info("(findByEmail) No user with email \"" + email + "\" not exists.");
            return null;
        }
        else return users;
    }

    @Override
    public User findByUserName(String name) throws UserException{

        List<User> userStream = usersCollection.stream().filter((User user) -> user.getUserName().equals(name)).collect(Collectors.toList());
        if(userStream.size() != 0)
        {
            return userStream.get(0);
        }
        logger.info("(findUserName) User with username: \"" + name + "\" not exists.");
        return null;
        //throw new UserNotExistException("User with username \""+ name +" \" not exists.");

    }

    @Override
    public List<User> findByLastName(String name) throws UserException{
        List<User> users = usersCollection.stream().filter((User user) -> user.getLastName().equals(name)).collect(Collectors.toList());
        if (users.size() == 0) {

            logger.info("(findBySurname) No user with surname \"" + name + "\" not exists.");
            return null;
        }
        else return users;
    }

    @Override
    public List<User> getAllUsers() throws UserException{
        List<User> users = new ArrayList<User>();
        users.addAll(this.usersCollection);
        return users;
    }


    @Override
    public long size()throws UserException{
        return usersCollection.size();
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        usersCollection.forEach((User user) -> builder.append(user).append(System.lineSeparator()));
        return builder.toString();
    }
}

package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import java.util.List;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;

/**
 * @author Peter Stanko
 * @version 2015-02-25
 */
public interface UserManager
{
    void addUser(User user) throws UserException;

    User getUser(long id) throws UserException;

    void removeUser(long id) throws UserException;

    void updateUser(User user) throws  UserException;

    List<User> findByFirstName(String name) throws UserException;

    List<User> findByEmail(String email) throws UserException;

    User findByUserName(String name) throws UserException;
    List<User> findByLastName(String name) throws UserException;

    List<User> getAllUsers() throws UserException;

    long size() throws  UserException;

    @Override
    String toString();
}

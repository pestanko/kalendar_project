package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.db.ServiceFailureException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserCannotBeCreated;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Stanko
 * @version  18.3.2015
 */
public class UserManagerDB implements UserManager {
    final static Logger logger = LoggerFactory.getLogger(UserManagerDB.class);

    private DataSource source = null;

    public UserManagerDB(DataSource source)
    {
        this.source = source;
    }

    @Override
    public void addUser(User user) throws UserException
    {
        if(user == null)
        {
            throw new NullPointerException("User is null.");
        }

        if(user.getId() != 0)
        {
            throw new IllegalArgumentException("User id is already set..");
        }


        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {

            st = connection.prepareStatement(
                    FileUtils.readSqlFile(User.class, "insert"),
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, user.getUserName());
            st.setString(2, user.getFirstName());
            st.setString(3, user.getLastName());
            st.setString(4, user.getMobileNumber());
            st.setString(5, user.getEmail());
            st.setString(6, user.getAddress());

            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new UserCannotBeCreated("Internal Error: More rows "
                        + "inserted when trying to insert user " + user);
            }

            ResultSet keyRS = st.getGeneratedKeys();

            try {
                user.setId(getKey(keyRS, user));
            } catch (ServiceFailureException ex) {
                throw new UserException("Detected problem with receiving user id.", ex);
            }


        } catch (SQLException ex) {
            throw new UserCannotBeCreated("Cannot create user with username: " + user.getUserName(), ex);
        } catch (IOException e) {
            throw new UserException("Cannot load sql file...", e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot close database.", ex);
                }
            }
        }
    }

    private static Long getKey(ResultSet keyRS, User user) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + " retriving failed when trying to insert user " + user
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + " retriving failed when trying to insert user " + user
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + " retriving failed when trying to insert user " + user
                    + " - no key found");
        }
    }

    @Override
    public User getUser(long id) throws UserException {
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(User.class, "SELECT_BY_ID")
                    );
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                User user = resultSetToUser(rs);

                if (rs.next()) {
                    throw new UserException(
                            "Internal error: More entities with the same id found "
                                    + "(source id: " + id + ", found " + user + " and " + resultSetToUser(rs));
                }

                return user;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new UserException(
                    "Error when retrieving user with id " + id, ex);
        } catch (IOException e) {
            throw new UserException("Cannot find sql file for get by id.", e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot clone statement. ", ex);
                }
            }
        }
    }

    public static User resultSetToUser(ResultSet rs) throws SQLException {

        return resultSetToUser(rs, "");
    }

    public static User resultSetToUser(ResultSet rs, String prefix) throws SQLException {
        if(prefix == null || prefix.equals(""))
        {
            prefix="";
        }
        User user = new User();
        user.setId(rs.getLong(prefix+"ID"));
        user.setFirstName(rs.getString(prefix+"FIRST_NAME"));
        user.setLastName(rs.getString(prefix+"LAST_NAME"));
        user.setUserName(rs.getString(prefix + "USER_NAME"));
        user.setEmail(rs.getString(prefix+"EMAIL"));
        user.setMobileNumber(rs.getString(prefix+"MOBILE_NUMBER"));
        user.setAddress(rs.getString(prefix+"ADDRESS"));
        return user;
    }

    @Override
    public void removeUser(long id) throws UserException
    {
        if(id <= 0)
            throw new IllegalArgumentException("Id is less than one.");
        PreparedStatement st = null;

        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    "DELETE FROM USERS WHERE id=?");
            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new UserException(
                    "Error when deleting user with id = " + id, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot clone statement. ", ex);
                }
            }
        }


    }

    @Override
    public void updateUser(User user) throws UserException
    {
        if(user == null)
        {
            throw new NullPointerException("User is null.");
        }

        long id = user.getId();
        if(id == 0)
        {
            throw new IllegalArgumentException("User's id is 0. User is not in db. You have to update him.");
        }

        //UPDATE USERS SET user_name=1, name=2, surname=3, email=4, mobile_number=5, ADDRESS=6 WHERE id=7
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {

            st = connection.prepareStatement(
                    FileUtils.readSqlFile(User.class, "update")
            );

            st.setString(1, user.getUserName());
            st.setString(2, user.getFirstName());
            st.setString(3, user.getLastName());
            st.setString(4, user.getEmail());
            st.setString(5, user.getMobileNumber());
            st.setString(6, user.getAddress());
            st.setLong  (7, user.getId());

            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new UserCannotBeCreated("Internal Error: More rows "
                        + "inserted when trying to insert user " + user);
            }

        } catch (SQLException ex) {
            throw new UserCannotBeCreated("Cannot create user with username: " + user.getUserName(), ex);
        } catch (IOException e) {
            throw new UserException("Cannot load sql file for update.", e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot close database.", ex);
                }
            }
        }

    }

    private List<User> findByAttribute(String attr, String name) throws UserException
    {
        if(name == null)
        {
            throw new NullPointerException("Name is null");
        }

        PreparedStatement st = null;
        try (Connection connection = source.getConnection())  {
            st = connection.prepareStatement(
                    "SELECT id,USER_NAME,FIRST_NAME,LAST_NAME,MOBILE_NUMBER,EMAIL,ADDRESS FROM USERS WHERE "+ attr +" LIKE ?");
            st.setString(1, name);
            ResultSet rs = st.executeQuery();

            List<User> result = new ArrayList<User>();
            while (rs.next()) {
                result.add(resultSetToUser(rs));
            }
            if(result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {
            throw new UserException(
                    "Error when retrieving all users", ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot clone statement. ", ex);
                }
            }
        }
    }

    @Override
    public List<User> findByFirstName(String name) throws UserException
    {
        if(name == null)
        {
            throw new NullPointerException("Name is null");
        }

        return findByAttribute("FIRST_NAME", name);

    }

    @Override
    public List<User> findByEmail(String email)throws UserException{
        if(email == null)
        {
            throw new NullPointerException("Name is null");
        }

        return findByAttribute("EMAIL", email);

    }

    @Override
    public User findByUserName(String username) throws UserException {
        if(username == null)
        {
            throw new NullPointerException("Name is null");
        }

        List<User> result = findByAttribute("user_name", username);

        return (result == null) ? null : result.get(0);
    }




    @Override
    public List<User> findByLastName(String surname) throws UserException{
        if(surname == null)
        {
            throw new NullPointerException("Name is null");
        }

        return findByAttribute("LAST_NAME", surname);
    }

    @Override
    public List<User> getAllUsers() throws UserException{

        PreparedStatement st = null;
        try  (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(User.class ,"ALL"));
            ResultSet rs = st.executeQuery();

            List<User> result = new ArrayList<User>();
            while (rs.next()) {
                result.add(resultSetToUser(rs));
            }
            if(result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {
            throw new UserException(
                    "Error when retrieving all graves", ex);
        } catch (IOException e) {
            throw new UserException("Cannot load sql file for ALL ROWS.", e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot clone statement. ", ex);
                }
            }
        }
    }

    @Override
    public long size() throws UserException {
        PreparedStatement st = null;
        try (Connection connection = source.getConnection())  {
            st = connection.prepareStatement(
                    FileUtils.readFile("src/sql/users/USERS_TABLE_SIZE.sql"));
            ResultSet rs = st.executeQuery();
            long result = 0;
            while(rs.next()) {
                result = rs.getLong("NUMBER_OF_ELEMENTS");
            }
            return result;
        } catch (SQLException ex) {
            throw new UserException("Error when retrieving all users", ex);
        } catch (IOException e) {
            throw new UserException("Cannot load sql file for size.", e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot clone statement. ", ex);
                }
            }
        }
    }
}

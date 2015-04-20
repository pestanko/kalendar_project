package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind.BindType;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.binding.BindingException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.db.ServiceFailureException;
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
 * @version 24.3.2015
 */
public class BindManagerDB implements BindManager {
    final static Logger logger = LoggerFactory.getLogger(BindManagerDB.class);

    private DataSource source = null;

    public BindManagerDB(DataSource dataSource) {
        source = dataSource;
    }

    private Long getKey(ResultSet keyRS, Bind binding) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + " retriving failed when trying to insert binding " + binding
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + " retriving failed when trying to insert binding " + binding
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + " retriving failed when trying to insert binding " + binding
                    + " - no key found");
        }
    }

    @Override
    public void addBinding(Bind binding) throws BindingException {
        if (binding == null) {
            throw new NullPointerException("Binding is null.");
        }

        if (binding.getId() != 0) {
            throw new IllegalArgumentException("Binding id is already set..");
        }


        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {

            // INSERT INTO binds (user_id, event_id, type) VALUES (?,?,?)
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Bind.class, "insert"),
                    Statement.RETURN_GENERATED_KEYS);

            st.setLong(1, binding.getUser().getId());
            st.setLong(2, binding.getEvent().getId());
            st.setInt(3, BindType.getBindId(binding.getType()));


            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new BindingException("Internal Error: More rows "
                        + "inserted when trying to insert binding " + binding);
            }

            ResultSet keyRS = st.getGeneratedKeys();


            try {
                binding.setId(getKey(keyRS, binding));
            } catch (ServiceFailureException ex) {
                throw new BindingException("Detected problem with receiving binding id.", ex);
            }

            logger.debug("Added binding to database: " + binding);
        } catch (SQLException ex) {
            throw new BindingException("Cannot create binding with id: " + binding.getId(), ex);
        } catch (IOException e) {
            throw new BindingException("Cannot load sql file...", e);
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


    @Override
    public Bind getBinding(long id) throws BindingException {
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Bind.class, "GET_BY_ID")
            );
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Bind binding = resultSetToBinding(rs);

                if (rs.next()) {
                    throw new BindingException(
                            "Internal error: More entities with the same id found "
                                    + "(source id: " + id + ", found " + binding + " and " + resultSetToBinding(rs));
                }
                logger.debug("Got binding from database: " + binding);
                return binding;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new BindingException(
                    "Error when retrieving binding with id " + id, ex);
        } catch (IOException e) {
            throw new BindingException("Cannot find sql file for get by id.", e);
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
    public List<Bind> getAllBindings() throws BindingException {
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Bind.class, "GET_BY_ID")
            );
            ResultSet rs = st.executeQuery();
            List<Bind> bindings = new ArrayList<>();

            while (rs.next()) {
                Bind binding = resultSetToBinding(rs);
                bindings.add(binding);
                logger.debug("Got binding from database: " + binding);
            }
            if(bindings.size() == 0) return null;
            return bindings;
        } catch (SQLException ex) {
            throw new BindingException(
                    "Error when retrieving all graves", ex);
        } catch (IOException e) {
            throw new BindingException("Cannot load sql file for ALL ROWS.", e);
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

    private Bind resultSetToBinding(ResultSet rs) throws SQLException, BindingException {
        Bind binding = new Bind();
        binding.setUser(UserManagerDB.resultSetToUser(rs, "u_"));
        binding.setEvent(EventManagerDB.resultSetToEvent(rs, "e_"));
        binding.setType(BindType.getBindType(rs.getInt("b_type")));

        return binding;
    }

    @Override
    public void removeBinding(long id) throws BindingException {
        if (id <= 0)
            throw new IllegalArgumentException("Id is less than one.");
        PreparedStatement st = null;

        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    "DELETE FROM BINDS WHERE id=?");
            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new BindingException(
                    "Error when deleting binding with id = " + id, ex);
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
    public void updateBinding(Bind binding) throws BindingException {

        if (binding == null) {
            throw new NullPointerException("Binding is null.");
        }

        long id = binding.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Binding's id is 0. Binding is not in db. You have to update it.");
        }

        //UPDATE USERS SET user_name=1, name=2, surname=3, email=4, mobile_number=5, ADDRESS=6 WHERE id=7
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {

            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Bind.class, "update")
            );

            st.setLong(1, binding.getUser().getId());
            st.setLong(2, binding.getEvent().getId());
            st.setInt(3, BindType.getBindId(binding.getType()));
            st.setLong(4, binding.getId());

            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new BindingException("Internal Error: More rows "
                        + "inserted when trying to insert binding " + binding);
            }

        } catch (SQLException ex) {
            throw new BindingException("Cannot create binding with id: " + binding.getId(), ex);
        } catch (IOException e) {
            throw new BindingException("Cannot load sql file for update.", e);
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

    public List<Event> getEventsByCondition(User user, String sql, BindType type) throws BindingException {
        if (user == null || sql == null) {
            throw new NullPointerException("User or SQL is NULL.");
        }


        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    sql);
            st.setLong(1, user.getId());
            if (type != null && type != BindType.NONE) {
                st.setInt(2, BindType.getBindId(type));
            }
            ResultSet rs = st.executeQuery();

            List<Event> result = new ArrayList<>();
            while (rs.next()) {
                result.add(EventManagerDB.resultSetToEvent(rs));
            }
            if (result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {

            throw new BindingException(
                    "Error when retrieving all bindings for user: " + user, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Cannot close connection. ", ex);
                }
            }
        }
    }


    @Override
    public List<Event> findEventsWhereIsUser(User user) throws BindingException {
        try {
            return getEventsByCondition(user, FileUtils.readSqlFile(Bind.class, "GET_EVENTS"), null);
        } catch (IOException e) {
            throw new BindingException("Cannot load GET_EVENTS", e);
        }
    }

    @Override
    public List<Event> findEventsWhereIsUser(User user, BindType type) throws BindingException {
        if (user == null) {
            throw new NullPointerException("Name is null");
        }
        try {
            return getEventsByCondition(user, FileUtils.readSqlFile(Bind.class, "GET_EVENTS_TYPE"), type);
        } catch (IOException e) {
            throw new BindingException("Cannot load sql file.", e);
        }

    }

    @Override
    public List<Event> findUpcommingEvents(User user) throws BindingException {
        if (user == null) {
            throw new NullPointerException("User is null");
        }
        try {
            return getEventsByCondition(user, FileUtils.readSqlFile(Bind.class, "GET_EVENTS_UPCOMMING"), null);
        } catch (IOException e) {
            throw new BindingException("Cannot load sql file", e);
        }
    }

    @Override
    public List<Event> findCurrentEvents(User user) throws BindingException {
        if (user == null) {
            throw new NullPointerException("User is null");
        }
        try {
            return getEventsByCondition(user, FileUtils.readSqlFile(Bind.class, "GET_EVENTS_CURRENT"), null);
        } catch (IOException e) {
            throw new BindingException("Cannot load sql file", e);
        }
    }

    @Override
    public List<User> findUsersInEvent(Event event) throws BindingException {
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Bind.class, "GET_USERS"));
            st.setLong(1, event.getId());
            ResultSet rs = st.executeQuery();

            List<User> result = new ArrayList<User>();
            while (rs.next()) {
                result.add(UserManagerDB.resultSetToUser(rs));
            }
            if (result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {
            throw new BindingException(
                    "Error when retrieving all users for event " + event, ex);
        } catch (IOException e) {
            throw new BindingException("Cannot load sql file for Bindings GET USERS IN EVENT.", e);
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

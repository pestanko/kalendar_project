package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.db.ServiceFailureException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.EventExceptionDB;
import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Peter Zaoral
 * @version 2015-3-31
 */
public class EventManagerDB implements EventManager {

    final static Logger logger = LoggerFactory.getLogger(EventManagerDB.class);
    private DataSource source = null;

    public EventManagerDB(DataSource source) {
        this.source = source;
    }


    @Override
    public void addEvent(Event event) throws EventExceptionDB {
        if (event == null) {
            throw new NullPointerException("User is null.");
        }

        if (event.getId() != 0) {
            throw new IllegalArgumentException("User id is already set..");
        }


        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Event.class,"INSERT"),
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, event.getName());
            st.setString(2, event.getDescription());
            st.setTimestamp(3, new Timestamp(event.getDateBegin().getTime()));
            st.setTimestamp(4, new Timestamp(event.getDateEnd().getTime()));
            st.setString(5, event.getAddress());

            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new EventExceptionDB("Internal Error: More rows "
                        + "inserted when trying to insert event " + event);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            try {
                event.setId(getKey(keyRS, event));
            } catch (ServiceFailureException ex) {
                throw new EventExceptionDB("Detected problem with receiving event id.", ex);
            }


        } catch (SQLException ex) {
            throw new EventExceptionDB("Cannot create event with name: " + event.getName(), ex);
        } catch (IOException ex) {
            throw new EventExceptionDB("Can't read file.", ex);
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

    private static Long getKey(ResultSet keyRS, Event event) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + " retriving failed when trying to insert event " + event
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + " retriving failed when trying to insert event " + event
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + " retriving failed when trying to insert event " + event
                    + " - no key found");
        }
    }


    @Override
    public void removeEvent(long id)  throws EventExceptionDB
    {
        {
            if(id <= 0)
                throw new IllegalArgumentException("Id is less than one.");
            PreparedStatement st = null;

            try (Connection connection = source.getConnection()) {
                st = connection.prepareStatement(
                        FileUtils.readSqlFile(Event.class,"DELETE"));
                st.setLong(1, id);
                st.executeUpdate();
            } catch (SQLException ex) {
                throw new EventExceptionDB(
                        "Error when deleting event with id = " + id, ex);
            } catch (IOException ex) {
                throw new EventExceptionDB("Can't read file.", ex);
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

    static Event resultSetToEvent(ResultSet rs, String prefix) throws SQLException {
        if(prefix == null)
        {
            prefix = "";
        }

        Event event = new Event();
        event.setId(rs.getLong(prefix+ "ID"));
        event.setName(rs.getString(prefix+ "EVENT_NAME"));
        event.setDescription(rs.getString(prefix+ "DESCRIPTION"));
        event.setDateBegin(rs.getTimestamp(prefix+ "DATE_BEGIN"));
        event.setDateEnd(rs.getTimestamp(prefix+ "DATE_END"));
        event.setAddress(rs.getString(prefix+ "ADDRESS"));
        return event;
    }

    static Event resultSetToEvent(ResultSet rs) throws SQLException {
        return resultSetToEvent(rs, "");

    }

    @Override
    public Event getEvent(long id) throws EventExceptionDB {
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Event.class,"SELECT_BY_ID"));
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Event event = resultSetToEvent(rs);

                if (rs.next()) {
                    throw new EventExceptionDB(
                            "Internal error: More entities with the same id found "
                                    + "(source id: " + id + ", found " + " and " + resultSetToEvent(rs));
                }
                return event;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new EventExceptionDB(
                    "Error when retrieving event with id " + id, ex);
        } catch (IOException ex) {
            throw new EventExceptionDB("Can't read file.", ex);
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
    public List<Event> getEvent(String name) throws EventExceptionDB {
        if (name == null) {
            throw new NullPointerException("Name is null");
        }

        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Event.class, "SELECT_BY_NAME"));
            st.setString(1, name);
            ResultSet rs = st.executeQuery();

            List<Event> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToEvent(rs));
            }
            if (result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {
            throw new EventExceptionDB(
                    "Error when retrieving all events.", ex);
        } catch (IOException ex) {
            throw new EventExceptionDB("Can't read sql file",ex);
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
    public List<Event> getAllEvents() throws EventExceptionDB {

        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Event.class,"SELECT_ALL"));
            ResultSet rs = st.executeQuery();

            List<Event> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToEvent(rs));
            }
            if (result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {
            throw new EventExceptionDB(
                    "Error when retrieving all events", ex);
        } catch (IOException ex) {
            throw new EventExceptionDB("Can't read sql file",ex);
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
    public void editEvent(Event event) throws EventExceptionDB {
        if(event == null)
        {
            throw new NullPointerException("Event is null.");
        }

        long id = event.getId();
        if(id == 0)
        {
            throw new IllegalArgumentException("Event ID is 0, means that it is NOT in container, you have to use add() method");
        }
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {

            st = connection.prepareStatement(
                    //UPDATE EVENTS SET EVENT_NAME=?, DESCRIPTION=?, DATE_BEGIN=?, DATE_END=?, ADDRESS=? WHERE ID=?
                    FileUtils.readSqlFile(Event.class, "UPDATE"));

            st.setString(1, event.getName());
            st.setString(2, event.getDescription());
            st.setTimestamp(3, new Timestamp(event.getDateBegin().getTime()));
            st.setTimestamp(4, new Timestamp(event.getDateEnd().getTime()));
            st.setString(5, event.getAddress());
            st.setLong(6,event.getId());

            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new EventExceptionDB("Internal Error: More rows "
                        + "inserted when trying to insert user " + event);
            }

        } catch (SQLException ex) {
            throw new EventExceptionDB("Cannot create event named: " + event.getName(), ex);
        } catch (IOException ex) {
            throw new EventExceptionDB("Can't read sql file", ex);
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
    public List<Event> findCurrentEvents() throws EventExceptionDB{

        //Timestamp now = new Timestamp(new Date().getTime());
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Event.class,"SELECT_CURRENT"));
            ResultSet rs = st.executeQuery();
            List<Event> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToEvent(rs));
            }
            if (result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {
            throw new EventExceptionDB(
                    "Error when retrieving all events", ex);
        } catch (IOException ex) {
            throw new EventExceptionDB("Can't read sql file",ex);
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
    public List<Event> findEventInTimePeriod(Date start, Date end) throws EventExceptionDB {

        if(start == null || end == null){
            throw new NullPointerException("Start or end date of event is null");
        }
        //zhovievavost
        EventManagerImpl.dateFormat.setLenient(false);

        try {
            EventManagerImpl.dateFormat.parse(start.toString());
            EventManagerImpl.dateFormat.parse(end.toString());
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException("Start or end time has wrong format", ex);
        }
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {


            st = connection.prepareStatement(FileUtils.readSqlFile(Event.class, "SELECT_BETWEEN"));
            st.setTimestamp(1, new Timestamp(start.getTime()));
            st.setTimestamp(2, new Timestamp(end.getTime()));
            ResultSet rs = st.executeQuery();
            List<Event> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToEvent(rs));
            }
            if (result.size() == 0) return null;
            return result;

        } catch (SQLException ex) {
            throw new EventExceptionDB(
                    "Error when retrieving all events.", ex);
        } catch (IOException ex) {
            throw new EventExceptionDB("Can't read sql file",ex);
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
    public long size() throws EventExceptionDB {
        PreparedStatement st = null;
        try (Connection connection = source.getConnection()) {
            st = connection.prepareStatement(
                    FileUtils.readSqlFile(Event.class, "SIZE"));
            ResultSet rs = st.executeQuery();
            long result = 0;
            while (rs.next()) {
                result = rs.getLong("rows_count");
            }
            return result;
        } catch (SQLException ex) {
            throw new EventExceptionDB("Error when retrieving all events", ex);
        } catch (IOException e) {
            throw new EventExceptionDB("Can't read sql file");
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

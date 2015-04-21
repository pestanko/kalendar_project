package cz.muni.fi.pv168.project.zaostan.kalendar.server;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Peter Zaoral
 * @date 21.4.2015
 */
@WebServlet(urlPatterns = EventServlet.URL_MAPPING + "/*")
public class EventServlet extends HttpServlet {
    public static final String URL_MAPPING = "/events";
    public static final String EVENTS_JSP = "/events.jsp";
    public static final String EVENT_MANAGER = "EventManager";
    public static final String UPDATE_FLAG = "update_flag";

    final static Logger logger = LoggerFactory.getLogger(EventServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("Incomming get request");
        try {
            logger.info("Showing events...");
            showEvents(request, response);
        } catch (CalendarEventException ex) {
            logger.error("Problem with showing events.", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private long parseID(String sid)
    {
        if(sid == null) return -1;
        try{
            return Long.parseLong(sid);
        }catch (NumberFormatException ex)
        {
            logger.error("(ParseID) - Cannot parse sid " + sid, ex);
        }
        return -1;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventManager eventManager = getEventManager(request);
        request.setCharacterEncoding("utf-8");
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/add":
                    doAddEvent(request, response, eventManager, false);
                    break;

                case "/delete":
                    doDeleteEvent(request, response, eventManager);
                    break;
                case "/update":
                    long eid = parseID(request.getParameter("eid"));
                    if(eid != -1)
                    {
                        Event event = eventManager.getEvent(eid);
                        request.setAttribute("update_sel_event", event);
                    }
                    request.setAttribute("update_event_id", eid);
                    showEvents(request, response, true);

                    break;

            }
        } catch (CalendarEventException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        //response.sendRedirect(request.getContextPath() + URL_MAPPING);

    }

    private void doDeleteEvent(HttpServletRequest request, HttpServletResponse response, EventManager eventManager) throws IOException, ServletException {
        long id = -1L;

        try
        {

            id = Long.parseLong(request.getParameter("eid"));
            logger.debug("Deleting event with eid == " + id);
            eventManager.removeEvent(id);
            showEvents(request, response);
        } catch (CalendarEventException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        } catch (NumberFormatException ex)
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Removing event with id: " + id + ex.getMessage());
        }
    }

    private EventManager getEventManager(HttpServletRequest request) {
        return (EventManager) getServletContext().getAttribute(EVENT_MANAGER);
    }

    private void showEvents(HttpServletRequest request, HttpServletResponse response, boolean update) throws ServletException, IOException, CalendarEventException {
        EventManager eventManager = getEventManager(request);
        if (eventManager == null) {
            logger.error("Event manager is NULL ----------------------");
        }
        List<Event> allEvents = eventManager.getAllEvents();
        if (allEvents == null) {
            logger.error("All events are null -------------------------");
        }

        request.setAttribute("Events", allEvents);
        if(update)
            request.setAttribute(UPDATE_FLAG, true);
        request.getRequestDispatcher(EVENTS_JSP).forward(request, response);
    }

    private void showEvents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CalendarEventException {
        showEvents(request, response, false);
    }

    private boolean containsOnly(String str, String pattern) {
        if(str == null) return false;
        if(pattern == null) return true;
        Pattern reg = Pattern.compile(pattern);
        return reg.matcher(str).matches();
    }


    private String checkField(String field, String name, String pattern, boolean empty) {
        if (name == null) {
            return field + " is null. ";
        }
        if (!empty && name.length() == 0) {
            return field + " length is 0.";
        }
        // "^[a-zA-Z]*$"
        if (!empty && !containsOnly(name, pattern)) {
            return field + " must match pattern " + pattern + ".";
        }

        return null;
    }

    private Date checkDate(String field, String name) throws ParseException {
        //String dateRegex = "^[0-9.-\\s/]+$";
        String result = checkField(field, name, null, false);
        if(result != null)
        {
            throw new ParseException(result, 0);
        }
        return Event.dateFormat.parse(name);
    }

    private String checkName(String field, String name) {
        return checkField(field, name, "^[a-zA-Z\\s-_]*$", false);
    }

    private void doCheckValidResult(StringBuilder message, String result) {
        if (result != null) {
            message.append("\n<li>\n");
            logger.debug("Error: " + result);
            message.append(result);
            message.append("\n</li>\n");
        }
    }


    private boolean doAddEvent(HttpServletRequest request, HttpServletResponse response, EventManager eventManager, boolean update) throws IOException, ServletException, CalendarEventException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String dateBegin = request.getParameter("dateBegin");
        String dateEnd = request.getParameter("dateEnd");
        String address = request.getParameter("address");

        StringBuilder message = new StringBuilder();
        String result;

        result = checkName("Name", name);
        doCheckValidResult(message, result);
        result = checkField("Description", description, null, true);
        doCheckValidResult(message, result);
        Date start = new Date();
        Date end = new Date();
        try {
             start= checkDate("Date of begin", dateBegin);
        } catch (ParseException e) {
            doCheckValidResult(message,e.getMessage());
        }
        doCheckValidResult(message, result);
        try {
             end= checkDate("Date of end", dateEnd);
        } catch (ParseException e) {
            doCheckValidResult(message,e.getMessage());
        }
        doCheckValidResult(message, result);

        result = checkField("Address", address, null, true);
        doCheckValidResult(message, result);


        if (message.length() != 0) {
            logger.error("Sending error message");
            String message_out = "<ul class='err_messages'>" + message.toString() + "</ul>";
            request.setAttribute("Error", message_out);
            logger.debug("Redirecting");
            showEvents(request, response);
            return false;
        }

        String update_flag =  request.getParameter(UPDATE_FLAG);
        if(update_flag == null || update_flag.length() == 0){
            update = false;
        }
        else{
            try{
                update = Boolean.parseBoolean(update_flag);
            }catch (NumberFormatException ex)
            {
                logger.error("Cannot convert: " + update_flag, ex);
                update = false;
            }
        }

        if (update) {
            long id;
            try {
                id = Long.parseLong(request.getParameter("eid"));
                Event event = eventManager.getEvent(id);
                if (event == null) {
                    throw new CalendarEventException("Event with eid " + id + " not found.");
                }

                event.setName(name);
                event.setDescription(description);
                event.setDateBegin(start);
                event.setDateEnd(end);
                event.setAddress(address);

                eventManager.updateEvent(event);
            } catch (CalendarEventException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return false;
            } catch (NumberFormatException ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Wrong id " + ex.getMessage());
                return false;
            }
        } else {
            Event event = new Event();

            event.setName(name);
            event.setDescription(description);
            event.setDateBegin(start);
            event.setDateEnd(end);
            event.setAddress(address);
            try {
                logger.debug("Adding event: " + event);
                eventManager.addEvent(event);

            } catch (CalendarEventException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return false;
            }
        }
        showEvents(request, response);


        return true;
    }
}

package cz.muni.fi.pv168.project.zaostan.kalendar.server;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * @author Peter Stanko
 * @date 19.4.2015
 */
@WebServlet(urlPatterns = EventServlet.URL_MAPPING + "/*")
public class EventServlet extends HttpServlet {
    public static final String URL_MAPPING = "/events";
    public static final String EVENTS_JSP = "/events.jsp";
}

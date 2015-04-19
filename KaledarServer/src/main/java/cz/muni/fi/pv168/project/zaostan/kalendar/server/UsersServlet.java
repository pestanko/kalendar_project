package cz.muni.fi.pv168.project.zaostan.kalendar.server;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Peter Stanko
 * @version 17.4.2015
 */

@WebServlet(UsersServlet.URL_MAPPING + "/*")
public class UsersServlet extends HttpServlet {


    public static final String URL_MAPPING = "/users";
    public static final String USERS_JSP = "/users.jsp";
    public static final String USER_MANAGER = "UserManager";
    final static Logger logger = LoggerFactory.getLogger(UsersServlet.class);
    public static final String UPDATE_FLAG = "update_flag";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("Incomming get request");
        try {
            logger.info("Showing users...");
            showUsers(request, response);
        } catch (UserException ex) {
            logger.error("Problem with showing users.", ex);
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
        UserManager userManager = getUserManager(request);
        request.setCharacterEncoding("utf-8");
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/add":
                    doAddUser(request, response, userManager, false);
                    break;

                case "/delete":
                    doDeleteUser(request, response, userManager);
                    break;
                case "/update":
                    //doAddUser(request, response, userManager, true);
                    long uid = parseID(request.getParameter("uid"));
                    if(uid != -1)
                    {
                        User user = userManager.getUser(uid);
                        request.setAttribute("update_sel_user", user);
                    }
                    request.setAttribute("update_user_id", uid);
                    showUsers(request, response, true);

                    break;

            }
        } catch (UserException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        //response.sendRedirect(request.getContextPath() + URL_MAPPING);

    }

    private void doDeleteUser(HttpServletRequest request, HttpServletResponse response, UserManager userManager) throws IOException, ServletException {
        long id = -1L;

        try
        {

            id = Long.parseLong(request.getParameter("uid"));
            logger.debug("Deleting user with uid == " + id);
            userManager.removeUser(id);
            showUsers(request, response);
        } catch (UserException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        } catch (NumberFormatException ex)
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Removing user with id: " + id + ex.getMessage());
        }
    }

    private UserManager getUserManager(HttpServletRequest request) {
        return (UserManager) getServletContext().getAttribute(USER_MANAGER);
    }

    private void showUsers(HttpServletRequest request, HttpServletResponse response, boolean update) throws ServletException, IOException, UserException {
        UserManager userManager = getUserManager(request);
        if (userManager == null) {
            logger.error("User manager is NULL ----------------------");
        }
        List<User> allUsers = userManager.getAllUsers();
        if (allUsers == null) {
            logger.error("All users are null -------------------------");
        }

        request.setAttribute("Users", allUsers);
        if(update)
            request.setAttribute(UPDATE_FLAG, true);
        request.getRequestDispatcher(USERS_JSP).forward(request, response);
    }

    private void showUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UserException {
        showUsers(request, response,  false);
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

    private String checkName(String field, String name) {
        return checkField(field, name, "^[a-zA-Z]*$", false);
    }

    private void doCheckValidResult(StringBuilder message, String result) {
        if (result != null) {
            message.append("\n<li>\n");
            logger.debug("Error: " + result);
            message.append(result);
            message.append("\n</li>\n");
        }
    }


    private boolean doAddUser(HttpServletRequest request, HttpServletResponse response, UserManager userManager, boolean update) throws IOException, ServletException, UserException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobileNumber");
        String address = request.getParameter("address");

        StringBuilder message = new StringBuilder();
        String result;

        // Check FirstName
        result = checkName("First name", firstName);
        doCheckValidResult(message, result);
        result = checkName("Last name", lastName);
        doCheckValidResult(message, result);
        result = checkField("User name", userName, "^[\\w.]*$", false);
        doCheckValidResult(message, result);

        String mailregex = "^[a-zA-Z.0-9_]+@[a-z-A-Z.0-9-]+$";
        result = checkField("Email", email, mailregex, false);
        doCheckValidResult(message, result);

        String mobileRegex = "^[+]?[0-9\\s-]+$";
        result = checkField("Mobile number", mobile, mobileRegex, true);
        doCheckValidResult(message, result);

        result = checkField("Address", address, null, true);
        doCheckValidResult(message, result);


        if (message.length() != 0) {
            logger.error("Sending error message");
            String message_out = "<ul class='err_messages'>" + message.toString() + "</ul>";
            request.setAttribute("Error", message_out);
            logger.debug("Redirecting");
            showUsers(request, response);
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
                id = Long.parseLong(request.getParameter("uid"));
                User user = userManager.getUser(id);
                if (user == null) {
                    throw new UserException("User with uid " + id + " not found.");
                }

                user.setUserName(userName);
                user.setAddress(address);
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setMobileNumber(mobile);

                userManager.updateUser(user);
            } catch (UserException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return false;
            } catch (NumberFormatException ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Wrong id " + ex.getMessage());
                return false;
            }
        } else {
            User user = new User();

            user.setUserName(userName);
            user.setAddress(address);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMobileNumber(mobile);
            try {
                logger.debug("Adding user: " + user);
                userManager.addUser(user);

            } catch (UserException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return false;
            }
        }
        showUsers(request, response);


        return true;
    }
}

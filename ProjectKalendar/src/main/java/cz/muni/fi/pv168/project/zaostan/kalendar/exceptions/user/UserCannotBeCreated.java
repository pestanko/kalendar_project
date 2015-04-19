package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user;

import java.sql.SQLException;

/**
 * @author Peter Stanko
 * @date 21.3.2015
 */
public class UserCannotBeCreated extends UserException {
    public UserCannotBeCreated(String s, Throwable ex) {
        super(s, ex);
    }

    public UserCannotBeCreated(String message)
    {
        this(message, null);
    }
}

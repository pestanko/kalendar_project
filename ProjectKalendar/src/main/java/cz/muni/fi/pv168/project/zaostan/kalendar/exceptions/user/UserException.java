package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user;

import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.db.ServiceFailureException;

/**
 * @author Peter Stanko
 * @date 21.3.2015
 */
public class UserException extends ServiceFailureException {
    public UserException(String message) { this(message, null);}
    public UserException(String message, Throwable ex) {super(message, ex);}

    public UserException() { this(null, null);}

}

package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user;


/**
 * @author Peter Stanko
 * @date 4.3.2015.
 */
public class UserNotExistException extends UserException {
    public UserNotExistException(String message, Throwable ex)
    {
        super(message, ex);
    }

    public UserNotExistException(String message)
    {
        super(message);
    }
}

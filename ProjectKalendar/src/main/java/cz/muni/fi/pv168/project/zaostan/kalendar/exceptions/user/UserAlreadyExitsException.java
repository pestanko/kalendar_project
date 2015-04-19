package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user;


/**
 * @author Peter Stanko
 * @date 4.3.2015.
 */
public class UserAlreadyExitsException extends UserException {
    public UserAlreadyExitsException(String message) {
        super(message);
    }

    public UserAlreadyExitsException(String message, Throwable ex)
    {
        super(message, ex);
    }

}

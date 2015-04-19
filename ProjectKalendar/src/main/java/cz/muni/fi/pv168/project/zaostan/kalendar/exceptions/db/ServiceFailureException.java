package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.db;

/**
 * @author Peter Stanko
 * @date 21.3.2015
 */
public class ServiceFailureException extends Exception {

    public ServiceFailureException(Throwable cause)
    {
        super(cause);
    }
    public ServiceFailureException(String message) { this(message, null);}
    public ServiceFailureException(String message, Throwable ex) {super(message, ex);}

    public ServiceFailureException() { this(null, null);}
}

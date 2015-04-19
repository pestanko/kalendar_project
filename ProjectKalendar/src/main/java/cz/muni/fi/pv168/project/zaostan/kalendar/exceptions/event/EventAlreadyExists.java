package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event;

/**
 * Created by Peter Zaoral on 10.3.2015.
 */
public class EventAlreadyExists extends IllegalArgumentException {
    public EventAlreadyExists() {
    }

    public EventAlreadyExists(String s) {
        super(s);
    }

    public EventAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public EventAlreadyExists(Throwable cause) {
        super(cause);
    }
}

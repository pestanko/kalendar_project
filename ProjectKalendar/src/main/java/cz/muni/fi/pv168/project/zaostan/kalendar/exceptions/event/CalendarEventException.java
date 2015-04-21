package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event;

import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.db.ServiceFailureException;

/**
 * Created by Peter Zaoral on 22.3.2015.
 */
public class CalendarEventException extends ServiceFailureException {
    public CalendarEventException(String message) {
        super(message);
    }

    public CalendarEventException(String message, Throwable ex) {
        super(message, ex);
    }

    public CalendarEventException() {
    }
}

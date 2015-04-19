package cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.binding;

import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.db.ServiceFailureException;

/**
 * @author Peter Stanko
 * @date 24.3.2015
 */
public class BindingException extends ServiceFailureException {
    public BindingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BindingException(String message)
    {
        this(message, null);
    }
    public BindingException(Throwable cause)
    {
        super(cause);
    }

    public BindingException()
    {
        super();
    }

}

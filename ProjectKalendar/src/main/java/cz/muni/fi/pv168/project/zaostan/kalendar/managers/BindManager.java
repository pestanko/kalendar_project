package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import java.util.List;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.binding.BindingException;

/**
 * @author Peter Stanko
 * @version 2015-02-25
 */
public interface BindManager
{
    void addBinding(Bind binding) throws BindingException;
    Bind getBinding(long id) throws BindingException;
    void removeBinding(long id) throws BindingException;
    void updateBinding(Bind binding) throws BindingException;

    List<Event> findEventsWhereIsUser(User user) throws BindingException;
    List<Event> findEventsWhereIsUser(User user, Bind.BindType type) throws BindingException;
    List<Event> findUpcommingEvents(User user) throws BindingException;
    List<Event> findCurrentEvents(User user) throws BindingException;
    List<User> findUsersInEvent(Event event) throws BindingException;

}

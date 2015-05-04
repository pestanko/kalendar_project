package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import java.util.Date;
import java.util.List;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;

/**
 * @author Peter Stanko
 * @version 2015-02-25
 */
public interface EventManager
{

    void addEvent(Event event) throws CalendarEventException;

    void removeEvent(long id) throws CalendarEventException;


    Event getEvent(long id) throws CalendarEventException;


    List<Event> getEvent(String name) throws CalendarEventException;

    List<Event> getAllEvents() throws CalendarEventException;

    void updateEvent(Event event) throws CalendarEventException;

    List<Event> findCurrentEvents() throws CalendarEventException;


    List<Event> findEventInTimePeriod(Date start, Date end) throws CalendarEventException;

    long size() throws CalendarEventException;


}

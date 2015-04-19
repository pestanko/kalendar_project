package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import java.util.Date;
import java.util.List;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.EventExceptionDB;

/**
 * @author Peter Stanko
 * @version 2015-02-25
 */
public interface EventManager
{

    void addEvent(Event event) throws EventExceptionDB;

    void removeEvent(long id) throws EventExceptionDB;


    Event getEvent(long id) throws EventExceptionDB;


    List<Event> getEvent(String name) throws EventExceptionDB;

    List<Event> getAllEvents() throws EventExceptionDB;

    void editEvent(Event event) throws EventExceptionDB;

    List<Event> findCurrentEvents() throws EventExceptionDB;


    List<Event> findEventInTimePeriod(Date start, Date end) throws EventExceptionDB;

    long size() throws EventExceptionDB;
}

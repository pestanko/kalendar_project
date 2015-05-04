package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.EventAlreadyExists;

import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Stanko
 * @version 2015-02-24
 */
public class EventManagerImpl implements EventManager
{
    private Set<Event> events = new HashSet<Event>();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
    private int index = 0;
    @SuppressWarnings("unused")
    final static Logger logger = LoggerFactory.getLogger(EventManagerImpl.class);
    // TODO
    @Override
    public void addEvent(Event event)
    {
        if(event.getId() != 0) throw new EventAlreadyExists();
        event.setId(++index);
        events.add(event);
    }

    // TODO
    @Override
    public void removeEvent(long id)
    {

        List<Event> eventStream = events.stream().filter((Event event) -> event.getId() == id).collect(Collectors.toList());
        if(eventStream.size() == 0) return;
        Event result = eventStream.get(0);
        events.remove(result);
    }

    // TODO
    @Override
    public Event getEvent(long id)
    {

        List<Event> eventStream = events.stream().filter((Event event) -> event.getId() == id).collect(Collectors.toList());
        if(eventStream.size() == 0) return null;
        return eventStream.get(0);
    }

    @Override
    public List<Event> getEvent(String name) {
        List<Event> eventStream = events.stream().filter((Event event) -> event.getName().equals(name)).collect(Collectors.toList());
        if(eventStream.size() == 0) return null;
        return eventStream;

    }

    @Override
    public List<Event> getAllEvents() throws CalendarEventException {
        return new ArrayList<>(events);
    }

    @Override
    public void updateEvent(Event event) throws CalendarEventException {
        removeEvent(event.getId());
        addEvent(event);
    }

    // TODO
    @Override
    public List<Event> findCurrentEvents()
    {
            List<Event> results = events.stream().filter(Event::isNowActive).collect(Collectors.toList());
            if(results.size() == 0) return null;
            return results;
    }


    @Override
    public List<Event> findEventInTimePeriod(Date start, Date end) {
        return null;
    }

    @Override
    public long size() {
        return events.size();
    }

    public static EventManager asEventManager(List<Event> events)
    {
        EventManagerImpl eventManager = new EventManagerImpl();
        eventManager.events.addAll(events);
        return eventManager;
    }

}

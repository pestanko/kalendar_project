package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.gui.forms.UserKalendarForm;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event.EventType;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.BindManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event.EventType.CURRENT;
import static cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event.EventType.UPCOMMING;

/**
 * Created by wermington on 5/4/15.
 */
public class EventTableModel extends AbstractTableModel {

    private EventManager eventManager = MyApplication.getEventManager();
    private BindManager bindManager = MyApplication.getBindManager();
    final static Logger logger = LoggerFactory.getLogger(EventTableModel.class);

    private static ResourceBundle texts = ResourceBundle.getBundle("forms");


    List<Event> events;

    public EventTableModel()
    {
        try {
            events = eventManager.getAllEvents();
        } catch (CalendarEventException e) {
            logger.error("Get all events exception", e);
        }
    }

    @Override
    public int getRowCount() {
        return events.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Event event = events.get(rowIndex);

        switch (columnIndex)
        {
            case 0:
                return event.getName();

            case 1:
                return event.getDescription();
            case 2:
                return event.getDateBegin();
            case 3:
                return  event.getDateEnd();
            case 4:
                return event.getAddress();
        }
        return null;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return texts.getString("name");
            case 1:
                return texts.getString("description");
            case 2:
                return texts.getString("date_begin");
            case 3:
                return texts.getString("date_end");
            case 4:
                return texts.getString("address");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }


    public void updateAllEvents(UserKalendarForm.KalendarStats stats) throws CalendarEventException {
        if(stats==null)
        {
            events = eventManager.getAllEvents();
        }
        new SwingWorker<Void,Void>()
        {

            @Override
            protected Void doInBackground() throws Exception {

                EventManager tmp = eventManager;
                User user = stats.getUser();
                Bind.BindType bindType = stats.getBindType();
                EventType eventType = stats.getEventType();

                if(user == null)
                    events = tmp.getAllEvents();

                if(bindType != Bind.BindType.NONE)
                {
                    events = bindManager.findEventsWhereIsUser(user, bindType);
                }

                events = events.stream().filter((event) -> {
                    switch (eventType){
                        case CURRENT:
                            return event.isNowActive();
                        case UPCOMMING:
                            return event.isUpcoming();
                        default:
                            return true;
                    }

                }).collect(Collectors.toList());
                return null;

            }
        };
    }
}

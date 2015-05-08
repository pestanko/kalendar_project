package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Peter Stanko on 8.5.2015.
 *
 * @author Peter Stanko
 */
public class EventsAdminModel extends AbstractTableModel {

    private EventManager eventManager;
    private List<Event> events;

    public final static int COL_ID = 5;
    public final static int COL_NAME = 0;
    public final static int COL_DESC = 1;
    public final static int COL_BEGIN = 2;
    public final static int COL_END = 3;
    public final static int COL_ADDR =4;

    private static ResourceBundle texts = ResourceBundle.getBundle("forms");


    final static Logger logger = LoggerFactory.getLogger(EventsAdminModel.class);

    public EventsAdminModel()
    {
        eventManager = MyApplication.getEventManager();
        updateEvents();
    }


    public void updateEvents() {


        try {
            events = eventManager.getAllEvents();
            fireTableDataChanged();
        } catch (CalendarEventException e) {
            // logger
            logger.error("Cannot update evetns.", e);
        }
    }


    @Override
    public int getRowCount() {
        return events.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Event event = events.get(rowIndex);

        switch (columnIndex)
        {
            case COL_NAME:
                return event.getName();
            case COL_DESC:
                return event.getDescription();
            case COL_BEGIN:
                return event.getDateBegin();
            case COL_END:
                return event.getDateEnd();
            case COL_ADDR:
                return event.getAddress();
            case COL_ID:
                return ""+event.getId();
            default:
                return null;
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case COL_NAME:
                return texts.getString("name");
            case COL_DESC:
                return texts.getString("description");
            case COL_BEGIN:
                return texts.getString("date_begin");
            case COL_END:
                return texts.getString("date_end");
            case COL_ID:
                return "Id";
            case COL_ADDR:
                return texts.getString("address");
            default:
                logger.error("Column index exception thrown");
                throw new IllegalArgumentException("columnIndex");

        }
    }



    public void updateEvent(Event event)
    {
        try {
            eventManager.updateEvent(event);
            updateEvents();
        } catch (CalendarEventException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(Event event)
    {
        try {
            eventManager.removeEvent(event.getId());
            updateEvents();
        } catch (CalendarEventException e) {
            e.printStackTrace();
        }
    }

}

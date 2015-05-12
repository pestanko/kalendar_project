package cz.muni.fi.pv168.project.zaostan.gui.forms.events;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.gui.forms.components.JXDateTimePicker;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.EventsAdminModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by wermington on 4/27/15.
 */
public class EventsForm extends JPanel {
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnEdit;
    private JLabel labelFrom;
    private JPanel listAllEvents;
    private JLabel labelTo;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTable tableEvents;
    private JXDateTimePicker inputDateFrom;
    private JXDateTimePicker inputDateTo;
    private JButton btnFilter;

    final static Logger logger = LoggerFactory.getLogger(EventsForm.class);

    private final EventsAdminModel model = new EventsAdminModel();

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }


    public void initAllComponents()
    {
        tableEvents.setModel(model);


        inputDateFrom.setFormats(Event.dateFormat);
        inputDateTo.setFormats(Event.dateFormat);

        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.filterEvents(inputDateFrom.getDate(), inputDateTo.getDate());
            }
        });


        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventsEditFrom form = new EventsEditFrom(model);
                form.showDialog();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Event event = getSelectedEvent();
                if(event == null) return;
                model.deleteEvent(event);
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Event event = getSelectedEvent();
                if(event == null) return;
                EventsEditFrom form = new EventsEditFrom(model, event);
                form.showDialog();
            }
        });
    }


    private Event getSelectedEvent()
    {
        int row = tableEvents.getSelectedRow();
        if(row == -1) return null;

        String ename = (String) tableEvents.getValueAt(row, EventsAdminModel.COL_ID);
        if(ename == null) return null;

        EventManager eventManager = MyApplication.getEventManager();



        try {
            Event event = eventManager.getEvent(Long.parseLong(ename));
            return event;
        } catch (CalendarEventException e) {
            logger.error("Cannot find user: ", e);
            e.printStackTrace();
        }
        return null;
    }


    public void showDialog()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("EventsForm");
                final EventsForm eventsForm = new EventsForm();
                frame.setContentPane(eventsForm.mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                eventsForm.initAllComponents();
                frame.pack();
                frame.setVisible(true);
            }
        });
    }


    public static void main(String[] args) throws Exception
    {
        MyApplication.init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("EventsForm");
                final EventsForm eventsForm = new EventsForm();
                frame.setContentPane(eventsForm.mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                eventsForm.initAllComponents();
                frame.pack();
                frame.setVisible(true);
            }
        });

    }
}

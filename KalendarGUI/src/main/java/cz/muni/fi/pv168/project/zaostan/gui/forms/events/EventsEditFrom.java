package cz.muni.fi.pv168.project.zaostan.gui.forms.events;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.gui.forms.components.JXDateTimePicker;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.EventsAdminModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.*;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManager;
import javafx.event.EventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by wermington on 4/27/15.
 */
public class EventsEditFrom {
    private final EventsAdminModel model;
    private final Event activeEvent;
    private JLabel labelName;
    private JLabel labelDateBegin;
    private JLabel labelDateEnd;
    private JLabel labelAddress;
    private JLabel labelDescription;
    private JTextField textName;
    private JTextArea textDescription;
    private JTextArea textAddress;
    private JButton btnSave;
    private JButton btnReset;
    private JButton btnCancel;
    private JXDateTimePicker inputDateEnd;
    private JXDateTimePicker inputDateBegin;
    private JPanel mainFramePanel;
    private JPanel mainFrameContextPanel;
    private JTable table1;
    private JComboBox inputAddUser;
    private JButton btnAddUser;
    private JComboBox inputUserType;
    private JFrame frame;

    final static Logger logger = LoggerFactory.getLogger(EventsEditFrom.class);


    public EventsEditFrom(EventsAdminModel model)
    {
        this.model = model;
        activeEvent = null;
    }

    public EventsEditFrom(EventsAdminModel model, Event event) {
        this.model = model;
        this.activeEvent = event;
    }

    public void initAllComponents()
    {

        inputAddUser.setModel(new DefaultComboBoxModel<>(Bind.BindType.values()));


        if(activeEvent != null)
        {
            textAddress.setText(activeEvent.getAddress());
            textDescription.setText(activeEvent.getDescription());
            textName.setText(activeEvent.getName());

            inputDateBegin.setDate(activeEvent.getDateBegin());
            inputDateEnd.setDate(activeEvent.getDateEnd());
        }



        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



        Date date = new Date();

        //inputDateEnd.setFormats(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));
        //inputDateEnd.setTimeFormat(DateFormat.getTimeInstance(DateFormat.MEDIUM));

        inputDateEnd.setFormats(Event.dateFormat);

        inputDateEnd.setDate(date);

        //inputDateBegin.setFormats(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));
        //inputDateBegin.setTimeFormat(DateFormat.getTimeInstance(DateFormat.MEDIUM));

        inputDateBegin.setFormats(Event.dateFormat);

        inputDateBegin.setDate(date);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frame.setVisible(false);
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Event event = new Event();
                event.setName(textName.getText());
                event.setDescription(textDescription.getText());

                event.setAddress(textDescription.getText());

                Date begin = inputDateBegin.getDate();
                Date end = inputDateEnd.getDate();

                event.setDateBegin(begin);
                event.setDateEnd(end);

                if(activeEvent == null) {
                    EventManager eventManager = MyApplication.getEventManager();
                    try {
                        eventManager.addEvent(event);
                    } catch (CalendarEventException e1) {
                        logger.error("Error adding user.", e1);
                    }

                }else{
                    event.setId(event.getId());
                    model.updateEvent(event);
                }


                model.updateEvents();
                frame.dispose();
                frame.setVisible(false);
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(activeEvent == null)
                {
                    textName.setText("");
                    textAddress.setText("");
                    textDescription.setText("");
                    inputDateBegin.setDate(new Date());
                    inputDateEnd.setDate(new Date());
                    return;
                }
                textName.setText(activeEvent.getName());
                textAddress.setText(activeEvent.getAddress());
                textDescription.setText(activeEvent.getDescription());

                inputDateBegin.setDate(activeEvent.getDateBegin());
                inputDateEnd.setDate(activeEvent.getDateEnd());
            }
        });


    }



    public void showDialog()
    {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("EventsEditFrom");
                frame.setContentPane(mainFramePanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                initAllComponents();
                frame.pack();
                frame.setVisible(true);
            }
        });


    }

    public static void main(String[] args) {


        EventQueue.invokeLater(new Runnable() {
            public void run()
            {
                JFrame frame = new JFrame("EventsEditFrom");
                EventsEditFrom eventsEditFrom = new EventsEditFrom(null);
                eventsEditFrom.initAllComponents();
                frame.setContentPane(eventsEditFrom.mainFramePanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

    }
}

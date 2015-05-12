package cz.muni.fi.pv168.project.zaostan.gui.forms.events;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.gui.forms.components.JXDateTimePicker;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.EventAddUsersModel;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.EventsAdminModel;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UserComboModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.*;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.binding.BindingException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.event.CalendarEventException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.BindManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    private JTable tableAddedUsers;
    private JComboBox inputAddUser;
    private JButton btnAddUser;
    private JComboBox inputUserType;
    private JButton btnDeleteUser;
    private JFrame frame;
    private UserComboModel userModel;

    private EventAddUsersModel addUserModel;

    final static Logger logger = LoggerFactory.getLogger(EventsEditFrom.class);


    public EventsEditFrom(EventsAdminModel model) {
        this.model = model;
        activeEvent = null;
    }

    public EventsEditFrom(EventsAdminModel model, Event event) {
        this.model = model;
        this.activeEvent = event;
    }

    public void initAllComponents() {

        inputUserType.setModel(new DefaultComboBoxModel<>(Bind.BindType.values()));
        userModel = new UserComboModel(MyApplication.getUserManager());
        inputAddUser.setModel(userModel);

        addUserModel = new EventAddUsersModel();
        tableAddedUsers.setModel(addUserModel);


        if (activeEvent != null) {
            textAddress.setText(activeEvent.getAddress());
            textDescription.setText(activeEvent.getDescription());
            textName.setText(activeEvent.getName());

            inputDateBegin.setDate(activeEvent.getDateBegin());
            inputDateEnd.setDate(activeEvent.getDateEnd());

            BindManager bm = MyApplication.getBindManager();
            try {
                java.util.List<Bind> allBindings = bm.getAllBindings();
                if (allBindings != null) {
                    allBindings.forEach(bind -> {
                        if (bind.getEvent().getId() == activeEvent.getId()) {
                            addUserModel.addBind(bind.getUser(), bind.getType());
                        }
                    });
                }
            } catch (BindingException e) {
                e.printStackTrace();
                logger.error("Error in EventsEditForm.java in initAllComponents method", e);
            }
        }


        btnAddUser.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> process = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Bind.BindType type = (Bind.BindType) inputUserType.getSelectedItem();
                        if (type == null) return null;

                        String username = (String) inputAddUser.getSelectedItem();

                        try {
                            User user = MyApplication.getUserManager().findByUserName(username);
                            addUserModel.addBind(user, type);
                        } catch (UserException e1) {
                            logger.error("Cannot find user", e1);
                        }

                        return null;
                    }
                };

                process.execute();

            }
        });


        Date date = new Date();

        inputDateEnd.setFormats(Event.dateFormat);
        inputDateEnd.setDate(date);


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

                SwingWorker<Void, Void> process = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Event event = new Event();
                        event.setName(textName.getText());
                        event.setDescription(textDescription.getText());

                        event.setAddress(textDescription.getText());

                        Date begin = inputDateBegin.getDate();
                        Date end = inputDateEnd.getDate();

                        event.setDateBegin(begin);
                        event.setDateEnd(end);

                        if (activeEvent == null) {
                            EventManager eventManager = MyApplication.getEventManager();
                            try {
                                eventManager.addEvent(event);
                            } catch (CalendarEventException e1) {
                                logger.error("Error adding user.", e1);
                            }

                        } else {
                            event.setId(activeEvent.getId());
                            model.updateEvent(event);
                        }


                        BindManager bindManager = MyApplication.getBindManager();

                        for (int i = 0; i < addUserModel.getUsers().size(); i++) {
                            User user = addUserModel.getUsers().get(i);
                            Bind.BindType type = addUserModel.getBinds().get(i);

                            try {
                                logger.debug("Id of event is: " + event.getId());
                                bindManager.addBinding(new Bind(event, user, type));
                            } catch (BindingException e1) {
                                logger.error("Cannot add new Binding.", e1);
                            }
                        }

                        model.updateEvents();
                        return null;
                    }
                };


                process.execute();

                frame.dispose();
                frame.setVisible(false);
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeEvent == null) {
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

        btnDeleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SwingWorker<Void, Void> process = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {

                        int selectedRow = tableAddedUsers.getSelectedRow();
                        if (selectedRow < 0) return null;
                        String username = (String) addUserModel.getValueAt(selectedRow, 0);

                        UserManager userManager = MyApplication.getUserManager();
                        try {
                            User user = userManager.findByUserName(username);
                            addUserModel.deleteUser(user, activeEvent);
                        } catch (UserException e1) {
                            logger.error("Cannot find user with user name " + username, e1);
                        } catch (BindingException e1) {
                            logger.error("Cannot remove user from event.", e1);
                        }

                        return null;
                    }
                };

                process.execute();

            }
        });


    }


    public void showDialog() {

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
            public void run() {
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

package cz.muni.fi.pv168.project.zaostan.gui.forms;

import cz.muni.fi.pv168.project.zaostan.gui.forms.events.EventsEditFrom;
import cz.muni.fi.pv168.project.zaostan.gui.forms.events.EventsForm;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.EventTableModel;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.EventsAdminModel;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UserComboModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.binding.BindingException;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.BindManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by wermington on 4/28/15.
 */
public class UserKalendarForm {

    public static class KalendarStats
    {
        private Event.EventType eventType = Event.EventType.ALL;
        private User user = null;
        private Bind.BindType bindType = Bind.BindType.NONE;

        public Event.EventType getEventType() {
            return eventType;
        }

        public void setEventType(Event.EventType eventType) {
            this.eventType = eventType;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Bind.BindType getBindType() {
            return bindType;
        }

        public void setBindType(Bind.BindType bindType) {
            this.bindType = bindType;
        }
    }


    private JLabel labelSelectUser;
    private JComboBox inputSelectedUser;
    private JLabel labelUserType;
    private JComboBox inputUserType;
    private JTable tableEvents;
    private JLabel labelEventType;
    private JComboBox inputEventType;
    private JButton btnAddEvent;
    private JButton btnDelete;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JButton btnFind;

    private EventTableModel eventsModel;

    final static Logger logger = LoggerFactory.getLogger(UserKalendarForm.class);

    
    
    public void initAllComponents()
    {
        inputSelectedUser.setModel(new UserComboModel(MyApplication.getUserManager()));
        Event.EventType[] values = Event.EventType.values();
        inputEventType.setModel(new DefaultComboBoxModel<>(values));
        inputUserType.setModel(new DefaultComboBoxModel<>(Bind.BindType.values()));
        eventsModel = new EventTableModel();
        tableEvents.setModel(eventsModel);


        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = (String) inputSelectedUser.getSelectedItem();
                UserManager userManager = MyApplication.getUserManager();

                Bind.BindType bindType = (Bind.BindType) inputUserType.getSelectedItem();
                Event.EventType eventType = (Event.EventType) inputEventType.getSelectedItem();

                try {
                    User user = userManager.findByUserName(username);
                    if (user != null) {
                        try {
                            eventsModel.getEventsForUser(user, bindType, eventType);
                        } catch (BindingException e1) {
                            logger.error("Binding exception", e1);
                        }
                    }

                } catch (UserException e1) {
                    logger.error("Cannot find user with user name " + username, e1);
                }

            }
        });


        btnAddEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventsForm form = new EventsForm();
                form.showDialog();
            }
        });


        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // UZ SA MI NECHCE !
            }
        });

    }
    

    public static void main(String[] args)
    {
        try {
            MyApplication.init();
        } catch (Exception e) {
            logger.error("Application init: ", e);
        }
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("UserKalendarForm");
            UserKalendarForm userKalendarForm = new UserKalendarForm();
            frame.setContentPane(userKalendarForm.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            userKalendarForm.initAllComponents();
            frame.setVisible(true);
        });
    }
}

package cz.muni.fi.pv168.project.zaostan.gui.forms;

import cz.muni.fi.pv168.project.zaostan.gui.forms.models.EventTableModel;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UserComboModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

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
    private JButton addEventButton;
    private JButton deleteButton;
    private JPanel mainPanel;
    private JPanel contentPanel;

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

package cz.muni.fi.pv168.project.zaostan.gui.forms;

import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UserComboModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;

import javax.swing.*;
import java.awt.*;

/**
 * Created by wermington on 4/28/15.
 */
public class UserKalendarForm {
    private JLabel labelSelectUser;
    private JComboBox inputSelectedUser;
    private JLabel labelUserType;
    private JComboBox inputUserType;
    private JTable table1;
    private JLabel labelEventType;
    private JComboBox inputEventType;
    private JButton addEventButton;
    private JButton deleteButton;
    private JPanel mainPanel;
    private JPanel contentPanel;
    
    
    public void initComponents()
    {
        inputSelectedUser.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                User employee = (User)value;
                value = employee.getUserName();
                return super.getListCellRendererComponent(list, value,
                        index, isSelected, cellHasFocus);
            }
        });

        inputSelectedUser.setModel(new  UserComboModel(MyApplication.getUserManager()));

    }
    

    public static void main(String[] args)
    {

        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("UserKalendarForm");
            UserKalendarForm userKalendarForm = new UserKalendarForm();
            frame.setContentPane(userKalendarForm.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            userKalendarForm.initComponents();
            frame.setVisible(true);
        });
    }
}

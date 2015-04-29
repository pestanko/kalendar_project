package cz.muni.fi.pv168.project.zaostan.gui.forms.users;

import javax.swing.*;

/**
 * Created by Pepo on 27.4.2015.
 */
public class UsersEditForm {
    private JLabel labelFirstName;
    private JLabel labelLastName;
    private JTextField textFirstName;
    private JTextField textLastName;
    private JLabel labelUserName;
    private JLabel labelEmail;
    private JLabel labelMobileNumber;
    private JLabel labelAddress;
    private JPanel mainPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UsersEditForm");
        frame.setContentPane(new UsersEditForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

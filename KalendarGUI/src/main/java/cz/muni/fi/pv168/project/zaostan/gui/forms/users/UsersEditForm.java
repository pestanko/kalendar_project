package cz.muni.fi.pv168.project.zaostan.gui.forms.users;

import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UsersTableModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JTextField textUserName;
    private JTextField textEmail;
    private JTextField textMobileNumber;
    private JTextArea textAddress;
    private JButton btnSave;
    private JButton btnReset;
    private JButton btnClose;


    final static Logger logger = LoggerFactory.getLogger(UsersEditForm.class);

    public UsersEditForm() {
    }

    public UsersEditForm(UsersTableModel model) {

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.addUser(new User(textFirstName.getText(), textLastName.getText(), textUserName.getText(), textEmail.getText()));
                } catch (UserException e1) {
                    e1.printStackTrace();
                    logger.error("Error during adding user in UsersEditFom");
                }

            }
        });
    }

    public void showDialog() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("UsersEditForm");
                frame.setContentPane(new UsersEditForm().mainPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });


    }


}

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

    private UsersTableModel model;

    private JFrame frame;


    final static Logger logger = LoggerFactory.getLogger(UsersEditForm.class);

    public UsersEditForm() {

    }

    public UsersEditForm(UsersTableModel model) {
        this.model = model;
        logger.debug("Model was set. " + model);


    }

    public void showDialog() {

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame();
            frame.setContentPane(this.mainPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            initAllComponents();
            frame.pack();
            frame.setVisible(true);
        });


    }

    private void initAllComponents() {
        logger.debug("Initialize all components");

        btnClose.addActionListener(e -> {
            logger.debug("BtnClose was clicked. ");
            frame.dispose();
            frame.setVisible(false);
        });


        btnSave.addActionListener(e -> {
            try {
                logger.debug("Save was clicked.");
                model.addUser(new User(textFirstName.getText(), textLastName.getText(), textUserName.getText(), textEmail.getText()));
                frame.dispose();
                frame.setVisible(false);


            } catch (UserException e1) {
                e1.printStackTrace();
                logger.error("Error during adding user in UsersEditFom");
            }

        });


    }


}

package cz.muni.fi.pv168.project.zaostan.gui.forms.users;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UsersTableModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by wermington on 4/27/15.
 */
public class UsersForm extends JPanel {
    private JButton btnAdd;
    private JLabel labelUsers;
    private JButton btnDelete;
    private JButton btnEdit;
    private JPanel mainPanel;
    private JTable tableUsers;
    private JScrollPane jScrollPaneUsers;
    private JButton btnSave;
    final static Logger logger = LoggerFactory.getLogger(UsersForm.class);
    private UsersTableModel model;
    private JFrame frame;

    public UsersForm(){}
    public UsersForm(UsersTableModel model) {
        logger.debug("Model was set" + model);
        this.model = model;


    }
    private void initAllComponents()
    {
        btnEdit.setEnabled(false);

        tableUsers.setModel(new UsersTableModel());
        model = (UsersTableModel) tableUsers.getModel();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsersEditForm usersEditForm = new UsersEditForm(model);
                usersEditForm.showDialog();
            }
        });

        btnEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                User user = getSelectedUser();

                UsersEditForm uedit = new UsersEditForm(model, user);
                uedit.showDialog();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frame.setVisible(false);
            }
        });


        btnDelete.addActionListener(new ActionListener() {



            @Override
            public void actionPerformed(ActionEvent e) {


                logger.debug("Btn delete was clicked.");
                int row = tableUsers.getSelectedRow();
                String uname = (String) tableUsers.getValueAt(row, 0);
                UserManager userManager = MyApplication.getUserManager();
                if(uname == null) return ;
                try {
                    User active = userManager.findByUserName(uname);
                    if(active == null)
                    {
                        return ;
                    }

                    model.removeUser(row, active);

                } catch (UserException e1) {
                    logger.error("Error while deleting user in UseForm",e);
                    e1.printStackTrace();
                }

            }
        });

        tableUsers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                btnEdit.setEnabled(true);
            }
        });
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

    private User getSelectedUser()
    {
        int row = tableUsers.getSelectedRow();
        if(row == -1) return null;

        String uname = (String) tableUsers.getValueAt(row, 0);
        if(uname == null) return null;

        UserManager userManager = MyApplication.getUserManager();



        try {
            User user = userManager.findByUserName(uname);
            return user;
        } catch (UserException e) {
            logger.error("Cannot find user: ", e);
            e.printStackTrace();
        }
        return null;
    }


}

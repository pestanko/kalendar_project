package cz.muni.fi.pv168.project.zaostan.gui.forms.users;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UsersTableModel;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
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
    private JButton btnFind;
    private JPanel mainPanel;
    private JTable tableUsers;
    private JScrollPane jScrollPaneUsers;
    final static Logger logger = LoggerFactory.getLogger(UsersForm.class);

    public UsersForm() {
        btnEdit.setEnabled(false);

        tableUsers.setModel(new UsersTableModel());
        UsersTableModel model = (UsersTableModel) tableUsers.getModel();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsersEditForm usersEditForm = new UsersEditForm(model);
                usersEditForm.showDialog();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            int row = tableUsers.getSelectedRow();

            @Override
            public void actionPerformed(ActionEvent e) {
                UsersEditForm usersEditForm = new UsersEditForm(model, new User(tableUsers.getValueAt(row,2).toString(),tableUsers.getValueAt(row,0).toString(),
                        tableUsers.getValueAt(row,1).toString(),tableUsers.getValueAt(row,3).toString()));
                usersEditForm.showDialog();
            }
        });

        tableUsers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
               btnEdit.setEnabled(true);
            }
        });

    }

    public static void main(String[] args) {
        try {
            MyApplication.init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in initialization in UsersForm",e);
        }

        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("UsersForm");
            frame.setContentPane(new UsersForm().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

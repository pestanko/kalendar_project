package cz.muni.fi.pv168.project.zaostan.gui.forms.users;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.gui.forms.models.UsersTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

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

        tableUsers.setModel(new UsersTableModel());
        UsersTableModel model = (UsersTableModel) tableUsers.getModel();
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

package cz.muni.fi.pv168.project.zaostan.gui.forms.users;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;

import javax.swing.*;

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

    public UsersForm() {

        tableUsers.setModel(new UsersTableModel());
        UsersTableModel model = (UsersTableModel) tableUsers.getModel();



    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("UsersForm");
        frame.setContentPane(new UsersForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

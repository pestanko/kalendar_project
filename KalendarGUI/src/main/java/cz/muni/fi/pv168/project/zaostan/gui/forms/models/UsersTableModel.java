package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;


/**
 * Created by Peter Zaoral on 30.4.2015.
 */
public class UsersTableModel extends AbstractTableModel {
    private UserManager userManager;
    final static Logger logger = LoggerFactory.getLogger(UsersTableModel.class);

    public UsersTableModel() {
        userManager = MyApplication.getUserManager();
    }

    public void addUser(User user) throws UserException {
        userManager.addUser(user);
    }

    public int getRowCount() {

        try {

            return (int) userManager.size();
        } catch (UserException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getColumnCount() {
        return 6;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        try {
            User user;
            user = userManager.getAllUsers().get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return user.getUserName();
                case 1:
                    return user.getFirstName();
                case 2:
                    return user.getLastName();
                case 3:
                    return user.getMobileNumber();
                case 4:
                    return user.getEmail();
                case 5:
                    return user.getAddress();
                default:
                    throw new IllegalArgumentException("columnIndex");
            }
        } catch (UserException e) {
            e.printStackTrace();
        }
        return null;
    }

}

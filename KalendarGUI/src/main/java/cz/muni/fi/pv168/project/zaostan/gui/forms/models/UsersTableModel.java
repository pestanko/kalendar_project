package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Created by Peter Zaoral on 30.4.2015.
 */
public class UsersTableModel extends AbstractTableModel {
    private UserManager userManager = MyApplication.getUserManager();;
    final static Logger logger = LoggerFactory.getLogger(UsersTableModel.class);
    private static ResourceBundle texts = ResourceBundle.getBundle("forms");

    List<User> users = new ArrayList<>();
    public UsersTableModel() {
        try {
            users = userManager.getAllUsers();
        } catch (UserException e) {
            logger.error("Get all users exception", e);
        }
    }

    public void addUser(User user) throws UserException {
        userManager.addUser(user);
    }
    @Override
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
    @Override
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

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return texts.getString("user_name");
            case 1:
                return texts.getString("first_name");
            case 2:
                return texts.getString("last_name");
            case 3:
                return texts.getString("mobile_number");
            case 4:
                return texts.getString("email");
            case 5:
                return texts.getString("address");
            default:
                logger.error("Column index exception thrown");
                throw new IllegalArgumentException("columnIndex");

        }
    }

}

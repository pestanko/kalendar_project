package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.BindManager;
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
    private UserManager userManager = MyApplication.getUserManager();
    private BindManager bindManager = MyApplication.getBindManager();
    final static Logger logger = LoggerFactory.getLogger(UsersTableModel.class);
    private static ResourceBundle texts = ResourceBundle.getBundle("forms");

    List<User> users;


    private final int COL_FIRST = 1;
    private final int COL_LAST = 2;
    private final int COL_USER = 0;
    private final int COL_EMAIL = 4;
    private final int COL_ADDRESS = 5;
    private final int COL_MOBILE = 3;


    public UsersTableModel() {
        updateUsers();
    }

    public void addUser(User user) throws UserException {
        userManager.addUser(user);
        users = userManager.getAllUsers();
        int lastRow = (int) userManager.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public User findUser(User user)
    {
        try {
            if(userManager.getAllUsers().contains(user))
                return user;
        } catch (UserException e) {
            e.printStackTrace();
            logger.error("Error in UsersTableModel findUser method ",e);
        }
        return null;
    }



    public void updateUsers()
    {
        try {
            users = userManager.getAllUsers();
            fireTableDataChanged();
        } catch (UserException e) {
            logger.error("Get all users exception", e);
        }

    }


    @Override
    public int getRowCount() {

        try {

            return (int) userManager.size();
        } catch (UserException e) {
            logger.error("Error while getting row count",e);
            e.printStackTrace();
        }
        return 0;
    }

    public int getColumnCount() {
        return 6;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


            User user;
            user = users.get(rowIndex);

            switch (columnIndex) {
                case COL_USER:
                    return user.getUserName();
                case COL_FIRST:
                    return user.getFirstName();
                case COL_LAST:
                    return user.getLastName();
                case COL_MOBILE:
                    return user.getMobileNumber();
                case COL_EMAIL:
                    return user.getEmail();
                case COL_ADDRESS:
                    return user.getAddress();
                default:
                    throw new IllegalArgumentException("columnIndex");
            }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case COL_USER:
                return texts.getString("user_name");
            case COL_FIRST:
                return texts.getString("first_name");
            case COL_LAST:
                return texts.getString("last_name");
            case COL_MOBILE:
                return texts.getString("mobile_number");
            case COL_EMAIL:
                return texts.getString("email");
            case COL_ADDRESS:
                return texts.getString("address");
            default:
                logger.error("Column index exception thrown");
                throw new IllegalArgumentException("columnIndex");

        }
    }


    public void removeUser(int row, User active) throws UserException {

        if(active == null) return;

        userManager.removeUser(active.getId());
        updateUsers();


    }

    public void updateUser(User activeUser) throws UserException {
        userManager.updateUser(activeUser);


        updateUsers();
    }
}

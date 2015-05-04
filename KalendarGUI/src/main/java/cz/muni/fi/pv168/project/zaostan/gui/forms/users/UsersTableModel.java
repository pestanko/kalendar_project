package cz.muni.fi.pv168.project.zaostan.gui.forms.users;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManagerDB;
import cz.muni.fi.pv168.project.zaostan.kalendar.tools.FileUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by Peter Zaoral on 30.4.2015.
 */
public class UsersTableModel extends AbstractTableModel {
    private UserManager userManager;
    final static Logger logger = LoggerFactory.getLogger(UsersTableModel.class);

    public UsersTableModel() {

        BasicDataSource bds = new BasicDataSource();
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bds.setUrl("jdbc:derby:memory:Kalendar;create=true");

        userManager = new UserManagerDB(bds);
        try {
            FileUtils.createUsersTable(bds);
            createUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createUsers() throws UserException
    {
        logger.debug("Creating users");
        userManager.addUser(new User("Peter", "Stanko", "xstanko2", "stanko@localhost"));
        User miso = new User("Michal", "Joodla", "xmichal", "michal.joodla@localhost.edu");
        userManager.addUser(miso);
        userManager.addUser(new User("Peter", "Zaoral", "xzaoral2", "zaoral@localhost"));
        userManager.addUser(new User("Fero", "Mrkva", "xmrkva10", "mrkva@localhost"));
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

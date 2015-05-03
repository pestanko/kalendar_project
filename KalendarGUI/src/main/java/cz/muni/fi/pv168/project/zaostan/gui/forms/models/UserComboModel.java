package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.user.UserException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManagerDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Created by wermington on 3.5.2015.
 */
public class UserComboModel  extends AbstractListModel implements ComboBoxModel  {

    UserManager userManager;
    User selection = null;
    final static Logger logger = LoggerFactory.getLogger(UserComboModel.class);

    public UserComboModel(UserManager userManager)
    {
        super();
    }

    public Object getElementAt(int index) {
        try {
            return userManager.getUser(index);
        } catch (UserException e) {
            logger.error("UserEx: ", e);
        }
        return null;
    }

    public int getSize() {
        try {
            return (int) userManager.size();
        } catch (UserException e) {
            logger.error("UserEx: ", e);
        }
        return 0;
    }

    public void setSelectedItem(Object anItem) {
        selection = (User) anItem;
    } // item from the pull-down list

    // Methods implemented from the interface ComboBoxModel
    public Object getSelectedItem() {
            return selection; // to add the selection to the combo box
    }

}


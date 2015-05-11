package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.gui.forms.MyApplication;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.binding.BindingException;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.BindManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Peter Stanko on 11.5.2015.
 *
 * @author Peter Stanko
 */
public class EventAddUsersModel extends AbstractTableModel {

    private List<User> users = new ArrayList<>();
    private List<Bind.BindType> binds = new ArrayList<>();

    final static Logger logger = LoggerFactory.getLogger(EventAddUsersModel.class);

    public List<User> getUsers() {
        return users;
    }

    public List<Bind.BindType> getBinds()
    {
        return binds;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex)
        {
            case 0:
                return users.get(rowIndex).getUserName();

            case 1:
                return binds.get(rowIndex);

            default:
                return null;
        }
    }


    public void addBind(User user, Bind.BindType type)
    {

        for(int i = 0; i < users.size(); i++)
        {
            if(user.getId() == users.get(i).getId())
            {
                binds.set(i, type);
                fireTableDataChanged();
                return;
            }
        }

        users.add(user);
        binds.add(type);

        fireTableDataChanged();
    }


    public void deleteUser(User user, Event event) throws BindingException {


        for(int i = 0 ; i < users.size(); i++)
        {
            if(users.get(i).getId() == user.getId()) {
                users.remove(i);
                binds.remove(i);
            }
        }


        if(event != null)
        {

            BindManager bindManager = MyApplication.getBindManager();
            List<Bind> allbinds = bindManager.getAllBindings();


            if (allbinds != null) {
                allbinds.forEach(bind -> {
                    if(bind.getId() < 1) return;
                    if (bind.getUser().getId() == user.getId() && bind.getEvent().getId() == event.getId()) {
                        try {
                            bindManager.removeBinding(bind.getId());
                        } catch (BindingException e) {
                            logger.error("Cannot remove binding with id: " + bind.getId());
                        }
                    }
                });
            }
        }

        fireTableDataChanged();
    }
}

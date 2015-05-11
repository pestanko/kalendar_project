package cz.muni.fi.pv168.project.zaostan.gui.forms.models;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Peter Stanko on 11.5.2015.
 *
 * @author Peter Stanko
 */
public class EventAddUsersModel extends AbstractTableModel {

    private List<User> users = new ArrayList<>();
    private List<Bind.BindType> binds = new ArrayList<>();

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
                return;
            }
        }

        users.add(user);
        binds.add(type);

        fireTableDataChanged();
    }


}

package cz.muni.fi.pv168.project.zaostan.gui.forms;

import cz.muni.fi.pv168.project.zaostan.kalendar.managers.BindManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.EventManager;
import cz.muni.fi.pv168.project.zaostan.kalendar.managers.UserManager;

/**
 * Created by wermington on 3.5.2015.
 */
public class MyApplication {
    private static UserManager userManager;
    private static EventManager eventManager;
    private static BindManager bindManager;


    public static void init()
    {

    }


    public static UserManager getUserManager() {
        return userManager;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static BindManager getBindManager() {
        return bindManager;
    }
}

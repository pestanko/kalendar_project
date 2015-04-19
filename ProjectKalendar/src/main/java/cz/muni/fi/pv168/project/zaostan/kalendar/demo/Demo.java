package cz.muni.fi.pv168.project.zaostan.kalendar.demo;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.*;

/**
 * @author Peter Stanko
 * @date 11.3.2015.
 */
public class Demo {

    public static final String MYCONFIG_PROPERTIES = "/myconfig.properties";

    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        InputStream in;
        in = Demo.class.getResourceAsStream(MYCONFIG_PROPERTIES);

        prop.load(in);

        print_properties(prop);

        System.out.println("========= SYSTEM =========");

        print_properties(System.getProperties());

        System.setProperty("user.language", "sk");


        Locale loc = Locale.getDefault();



        System.out.println("Date: " + new Date());
    }

    private static void print_properties(Properties prop) {
        Set<String> keys = new TreeSet<>( prop.stringPropertyNames());

        for(String key : keys)
        {
            String property = prop.getProperty(key);
            System.out.println( key + ": " + property);
        }
    }

}

package cz.muni.fi.pv168.project.zaostan.kalendar.tools;


import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import org.apache.commons.io.IOUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Stanko
 * @date 22.3.2015
 */
public class FileUtils {



    private static final Map<String, String> sqlRes = new HashMap<String,String>();

    public static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public static String readResFile(String path) throws  IOException
    {
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(path);
        if(is == null)
        {
            System.out.println("Cannot find resource: "+ path);
        }
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF-8");
        return writer.toString();
    }

    public static String readSqlFile(Class<?> type, String name) throws IOException {

        if(type == null || name == null){
            throw new NullPointerException("type or name is null");
        }
        String nameUp = name.toUpperCase();

        String clsName = type.getSimpleName().toLowerCase()+"s";
        String clsNameUp = clsName.toUpperCase();

        String path = "sql/";
        path += clsName + "/" + clsNameUp+ "_TABLE_" + nameUp + ".sql";

        String keyName = clsNameUp + "_" + nameUp;

        if(sqlRes.containsKey(keyName)){
            return sqlRes.get(keyName);
        }

        try {
            String query = readResFile(path);
            sqlRes.put(keyName, query);

            return query;
        }catch (IOException ex)
        {
            throw new IOException("Cannot load sql file with name: " + name + " on path: " + path ,ex);
        }
    }

    public static void sqlResourceClear()
    {
        sqlRes.clear();
    }

    public static void createUsersTable(DataSource source) throws Exception {
        try(Connection connection = source.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(User.class, "CREATE")).executeUpdate();
        }
    }

    public static void createEventsTable(DataSource source) throws Exception {
        try(Connection connection = source.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(Event.class, "CREATE")).executeUpdate();
        }
    }
    public static void createBindsTable(DataSource source) throws Exception {
        try(Connection connection = source.getConnection()) {
            connection.prepareStatement(FileUtils.readSqlFile(Bind.class, "CREATE")).executeUpdate();
        }
    }
}

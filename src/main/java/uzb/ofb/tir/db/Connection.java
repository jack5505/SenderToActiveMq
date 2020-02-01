package uzb.ofb.tir.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection {
    private static Connection instance;
    private static java.sql.Connection con;

    public static Connection getInstance(){
        if(instance == null){
            instance = new Connection();
        }
        return instance;
    }
    public java.sql.Connection getConnection(){
        Properties properties = null;
        try {
            properties = loadParams();
            Class.forName("org.postgresql.Driver");
            con = (java.sql.Connection) DriverManager.getConnection(properties.getProperty("db.url"),properties.getProperty("db.username"),properties.getProperty("db.password"));
            return con;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    };

    private  Properties loadParams() throws IOException {
        Properties properties = new Properties();
        InputStream fileInputStream = null;
        try {
            fileInputStream = getClass().getClassLoader().getResourceAsStream("db.properties");
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
        }
        return properties;
    }



}

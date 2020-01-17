package uzb.ofb.tir.utils;

import javafx.scene.control.Label;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.awt.*;

public class ActiveMqOperations {
    public ActiveMqOperations() {

    }

    public static Connection connectWithActiveMq(String address,String port, String user, String password,Label status){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://"+address+":"+port);
        factory.setUserName(user);
        factory.setPassword(password);
        try
        {
            Connection connection = factory.createConnection();
            connection.start();
            status.setText("Connected");
            System.out.println("Successed Connect");
            return connection;
        } catch (JMSException e) {
            status.setText("Error");
            e.printStackTrace();
        }
        return null;
    }
}

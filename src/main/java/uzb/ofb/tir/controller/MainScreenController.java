package uzb.ofb.tir.controller;

import com.company.Wtransfer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.activemq.ActiveMQConnection;
import uzb.ofb.tir.db.OperationsDb;
import uzb.ofb.tir.dto.ActiveMqDto;
import uzb.ofb.tir.utils.ActiveMqOperations;
import uzb.ofb.tir.utils.Utilits;
import uzb.ofb.tir.utils.Views;

import javax.jms.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainScreenController implements Initializable {

    //PrefWidth 938
    //PrefHeigh 859

    public static MainScreenController  instance;

    @FXML
    private TextArea  inputForm;

    @FXML
    private TextArea output;

    @FXML
    private TextField address;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button send;

    @FXML
    private Button settings;

    @FXML
    private Button send1;

    @FXML
    private  Label status;

    @FXML
    private TextField port;

    private static String path = "";

    private Connection connection;

    private Scanner scanner;

    @FXML
    private RadioButton request;

    @FXML
    private RadioButton requestR;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextArea logs;

    @FXML
    private Button history;

    @FXML
    private Label dbStatus;

    public static ActiveMqDto activeMqDto = new ActiveMqDto();

    @FXML
    private CheckBox save;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        dbStatus.setVisible(false);
        instance = this;
        uzb.ofb.tir.db.Connection.getInstance().getConnection();
        status.setText("");
        requestR.setSelected(true);
        clicks();
        filter();

    }




    private void clicks() {
            //To save into File settings of configuration
            address.setText(OperationsDb.getSettings().getAddress());
            port.setText(OperationsDb.getSettings().getPort()+"");
            username.setText(OperationsDb.getSettings().getUsername());
            password.setText(OperationsDb.getSettings().getPassword());
            connection = ActiveMqOperations.connectWithActiveMq(address.getText(),port.getText(),username.getText(),password.getText(),status);
            settings.setOnAction(event -> {
                activeMqDto.setPort(Integer.parseInt(port.getText()));
                activeMqDto.setUsername(username.getText());
                activeMqDto.setPassword(password.getText());
                activeMqDto.setAddress(address.getText());
                OperationsDb.ChangeSettings();
            });

            history.setOnAction(event -> {
                Wtransfer wtransfer = new Wtransfer(Views.main.listRequests);
                wtransfer.show();
            });

            send1.setOnAction(event -> {
              //  logs.setText(logs.getText()+"Request sended correqts_in\n");
                sendGetToQueue();
            });

            send.setOnAction(event -> {
                sendGetToQueue();

            });

            requestR.setOnAction(event -> {
                requestR.setSelected(true);
                request.setSelected(false);
                output.setVisible(true);
                inputForm.setPrefHeight(350);
                //Modify location place of TextFields
                AnchorPane.setBottomAnchor(inputForm,521.0);
                AnchorPane.setBottomAnchor(output,13.0);
            });

            request.setOnAction(event -> {
                request.setSelected(true);
                requestR.setSelected(false);
                output.setVisible(false);
                inputForm.setPrefHeight(858);
                //Modify location place of TextFields
                AnchorPane.setBottomAnchor(inputForm,30.0);
            });
    }

    private void sendGetToQueue() {
        //Todo test request correctly
        System.out.println("Check for validation");
        if(Utilits.checkRequest(inputForm.getText()))
        {
            dbStatus.setVisible(true);
            dbStatus.setText("Passed Validation");
            if(save.isSelected()){
                    OperationsDb.insertRequest(inputForm.getText());
                    dbStatus.setText(dbStatus.getText()+" "+"saved");
            }
        new Thread(){
            @Override
            public void run() {
                super.run();
                logs.setText(logs.getText()+"Request sended correqts_in\n");
            }
        }.run();

       Thread thread = new Thread(){
           @Override
           public void run() {
               super.run();
               try
               {
                   Thread.sleep(1000);
                   Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
                   Queue queue = session.createQueue("correqts_in");
                   MessageProducer producer = session.createProducer(queue);
                   TextMessage request = session.createTextMessage(inputForm.getText());
                   producer.send(request);
                   Queue queue1 = session.createQueue("correqts_out");
                   MessageConsumer messageConsumer = session.createConsumer(queue1);
                   TextMessage message = (TextMessage) messageConsumer.receive();
                   System.out.println("Recieve");
                   logs.setText(logs.getText()+"Response get from correqts_out\n");
                   if(requestR.isSelected()){
                       output.setText("");
                       output.setText(output.getText()+"\n"+message.getText());
                   }
               } catch (JMSException e) {
                   e.printStackTrace();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       };
       thread.start();
        }
        else
            {
                dbStatus.setVisible(true);
                dbStatus.setText("Error don't passed validation");
            }
    }


    private void filter() {
        port.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    port.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    public  void changeInputReuquest(int requestID){
        inputForm.setText(""+OperationsDb.getSelectedId(requestID));
    }


}

package uzb.ofb.tir.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import uzb.ofb.tir.utils.ActiveMqOperations;
import uzb.ofb.tir.utils.Utilits;

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
    @FXML
    private TextArea  inputForm;

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
    private TextArea logs;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        request.fire();
        URL show = MainScreenController.class.getProtectionDomain().getCodeSource().getLocation();
        path = show.getPath();
        try {
            checkFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try
        {
            readFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        clicks();
    }



    private void readFromFile() throws FileNotFoundException {
        boolean yes = true;
        scanner = new Scanner(new File(this.path));
        int i = 0;
        while (scanner.hasNextLine()){
                if(i == 0){
                    address.setText(scanner.nextLine());
                }if(i == 1){
                    port.setText(scanner.nextLine());
            }if(i == 2){
                    username.setText(scanner.nextLine());
            }if(i == 3){
                    password.setText(scanner.nextLine());
            }
                i++;
        }
        scanner.close();
        if(i >= 4){
            connection = ActiveMqOperations.connectWithActiveMq(address.getText(),port.getText(),username.getText(),password.getText(),status);
        }

    }

    private void checkFile() throws IOException {
        this.path = Utilits.cdd(this.path)+"settings.txt";
        File file = new File(this.path);
        System.out.println(file.getPath());
        if (!file.exists()){
            try
            {
                System.out.println("createdfile");
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void clicks() {
            //To save into File settings of configuration
            settings.setOnAction(event -> {
                try
                {
                    PrintWriter printWriter = new PrintWriter(new File(this.path));
                    printWriter.println(address.getText());
                    printWriter.println(username.getText());
                    printWriter.println(password.getText());
                    printWriter.close();
                    readFromFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            send.setOnAction(event -> {
                try
                {
                    Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
                    Queue queue = session.createQueue("correqts_in");
                    MessageProducer producer = session.createProducer(queue);
                    TextMessage request = session.createTextMessage(inputForm.getText());
                    producer.send(request);
                    System.out.println("Sended");
                    logs.setText(logs.getText()+"Request sended correqts_in\n");
                    Queue queue1 = session.createQueue("correqts_out");
                    MessageConsumer messageConsumer = session.createConsumer(queue1);
                    TextMessage message = (TextMessage) messageConsumer.receive();
                    System.out.println("Recieve");
                    logs.setText(logs.getText()+"Response get from correqts_out");
                    inputForm.setText(inputForm.getText()+"\n"+message.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }

            });
            requestR.setOnAction(event -> {
                requestR.setSelected(true);
                request.setSelected(false);
            });
            request.setOnAction(event -> {
                request.setSelected(true);
                requestR.setSelected(false);
            });
    }


}

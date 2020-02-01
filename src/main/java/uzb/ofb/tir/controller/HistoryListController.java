package uzb.ofb.tir.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import uzb.ofb.tir.db.OperationsDb;
import uzb.ofb.tir.dto.RequestDto;

import java.net.URL;
import java.util.ResourceBundle;

public class HistoryListController implements Initializable {

    @FXML
    private TableView<RequestDto> tableData;

    @FXML
    private TableColumn<RequestDto,Long > id;

    @FXML
    private TableColumn<RequestDto,String > head;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        id.setCellValueFactory(new PropertyValueFactory<RequestDto,Long>("id"));
        head.setCellValueFactory(new PropertyValueFactory<RequestDto,String>("header"));
        tableData.getItems().addAll(OperationsDb.listRequests());
    }
}

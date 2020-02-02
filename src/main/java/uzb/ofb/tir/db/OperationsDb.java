package uzb.ofb.tir.db;

import oracle.jdbc.proxy.annotation.Pre;
import uzb.ofb.tir.controller.MainScreenController;
import uzb.ofb.tir.dto.ActiveMqDto;
import uzb.ofb.tir.dto.RequestDto;
import uzb.ofb.tir.utils.ActiveMqOperations;
import uzb.ofb.tir.utils.Utilits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static uzb.ofb.tir.controller.MainScreenController.activeMqDto;

public class OperationsDb {

    public static ActiveMqDto getSettings(){
        ActiveMqDto activeMqDto = new ActiveMqDto();
        try {
            PreparedStatement preparedStatement = (PreparedStatement) Connection.getInstance().getConnection().prepareStatement("select * from settings_active_mq order by id desc limit 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                activeMqDto.setPort(Integer.parseInt(resultSet.getString(3)));
                activeMqDto.setAddress(resultSet.getString(2));
                activeMqDto.setUsername(resultSet.getString(4));
                activeMqDto.setPassword(resultSet.getString(5));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeMqDto;
    }
    public static void ChangeSettings(){
        try
        {
            PreparedStatement preparedStatement = Connection.getInstance().getConnection().prepareStatement("insert into settings_active_mq(address,port,username,passwords) values (?,?,?,?)");
            preparedStatement.setString(1, activeMqDto.getAddress());
            preparedStatement.setInt(2,activeMqDto.getPort());
            preparedStatement.setString(3,activeMqDto.getUsername());
            preparedStatement.setString(4,activeMqDto.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<RequestDto> listRequests(){
        List<RequestDto> list = new ArrayList<>();
        try {
            ResultSet resultSet = Connection.getInstance()
                                            .getConnection()
                                            .prepareStatement("select t.id,t.headers from requests as t ")
                                            .executeQuery();
            while (resultSet.next()){
                list.add(new RequestDto((long) resultSet.getInt(1),resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    };

    public static void  insertRequest(String request){
        try
        {
        PreparedStatement preparedStatement = Connection.getInstance().getConnection().prepareStatement("insert into requests(headers,request) values(?,?)");
        preparedStatement.setString(1, Utilits.getRequestHead(request));
        preparedStatement.setString(2,request);
        preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getSelectedId(int requestID) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            PreparedStatement preparedStatement = Connection.getInstance().getConnection().prepareStatement("select  request from requests where id = ?");
            preparedStatement.setInt(1,requestID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                stringBuilder.append(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}

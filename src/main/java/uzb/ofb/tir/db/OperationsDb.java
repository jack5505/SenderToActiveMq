package uzb.ofb.tir.db;

import uzb.ofb.tir.dto.ActiveMqDto;
import uzb.ofb.tir.utils.ActiveMqOperations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}

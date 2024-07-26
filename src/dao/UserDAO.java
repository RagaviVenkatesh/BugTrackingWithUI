package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.User;
import utility.DBConnection;

public class UserDAO {
	
	public boolean userValidate(User user) throws SQLException {
		boolean result = false;
        String query = "select * from users where name=? and password=?";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
               statement.setString(1, user.getName());
               statement.setString(2, user.getPassword());
               boolean x = statement.execute();
               if(x) {
            	   result=true;}
           }
        return result;
    }
}

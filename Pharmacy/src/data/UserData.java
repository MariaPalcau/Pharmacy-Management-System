package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Role;
import model.User;
import utils.DBUtil;

/**
 * Clasa pentru gestionarea utilizatorilor care se ocupa cu identificarea utilizatorilor in baza de date.
 */

public class UserData {
	public static User findUser(String username){
		String sql = "SELECT * FROM credentials WHERE username = ?";
	    try (
	        Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	        PreparedStatement pstmt = conn.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, username);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return new User(
	                    rs.getInt("id"),
	                    rs.getString("username"),
	                    rs.getString("password"),
	                    Role.valueOf(rs.getString("role"))
	                );
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}

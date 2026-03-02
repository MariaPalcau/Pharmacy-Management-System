package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.User;
import service.LoginService;

class TestLogin {
	
	@BeforeAll
	static void setup() {
	    System.setProperty("db.url", "jdbc:derby:memory:pharmacyDB;create=true");
	    TestDBUtil.createTableCredentials();
	}
	
	@Test
	void testLoginPass() throws SQLException {
		User user = LoginService.login("admin", "1234".toCharArray());
        assertNotNull(user);
	}
	
	 @Test
	 void testLoginFail() throws SQLException {
		 User user = LoginService.login("admin", "wrong".toCharArray());
	     assertNull(user);
	 }
	 
	 @Test
	 void testLoginUserNotFound() throws SQLException{
	        User user = LoginService.login("Maria", "1234".toCharArray());
	        assertNull(user);
	 }

	 @Test
	 void testRolePharmacist() throws SQLException {
	        User user = LoginService.login("pharmacist", "pswd".toCharArray());
	        assertNotNull(user);
	        User loggedUser = LoginService.role(user);
	        assertTrue(loggedUser instanceof model.Pharmacist);
	    }
	 
	 @Test
	 void testRoleManager() throws Exception {
	        User user = LoginService.login("admin", "1234".toCharArray());
	        assertNotNull(user);
	        User loggedUser = LoginService.role(user);
	        assertTrue(loggedUser instanceof model.Manager);
	    }
	 
	 @Test
	 void testEmpty() throws SQLException {
		 User user = LoginService.login("", new char[0]);
		 assertNull(user);
	 }
}

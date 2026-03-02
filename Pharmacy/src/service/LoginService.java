package service;

import java.sql.SQLException;

import data.UserData;
import model.Manager;
import model.Pharmacist;
import model.Role;
import model.User;

public class LoginService {
	
	/**
	 * Valideaza datele introduse de utilizator
	 * @param username username utilizator
	 * @param password parola utilizator
	 * @return true/false
	 */
	public static boolean validateInput(String username, char[] password) {
		if (username == null || username.trim().isEmpty()) {
	        return false;
	    }
	    if (password == null || new String(password).trim().isEmpty()) {
	        return false;
	    }
		return true;
	}
	
	/**
	 * Se cauta utilizatorul in baza de date
	 * @param username username utilizator
	 * @param passwordInput parola inserata
	 * @return obiectul User
	 * @throws SQLException daca apare o eroare la citire
	 */
	public static User login(String username, char[] passwordInput) throws SQLException {
		if(validateInput(username, passwordInput)) {
			User user;
			try {
				user = UserData.findUser(username);
				if (user == null) return null;
				if (user.getPassword().equals(new String(passwordInput)))
					return user;
				else
					return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		return null;
		
    }
	
	/**
	 * Creeaza un obiect specific in functie de rolul utilizatorului.
	 * @param employee utilizatorul generic obtinut din baza de date
	 * @return un obiect de tip Pharmacist sau Manager sau null daca rolul nu este recunoscut
	 */
	public static User role(User employee) {
		User loggedUser = null;
		if (employee.getRole().equals(Role.PHARMACIST)) {
			loggedUser = new Pharmacist(employee.getUserID(), employee.getUsername(), employee.getPassword());
	    } else if (employee.getRole().equals(Role.MANAGER)) {
	    	loggedUser = new Manager(employee.getUserID(), employee.getUsername(), employee.getPassword());
	    }
		return loggedUser;
	}
}

package model;

/**
 * Clasa User reprezinta un utilizator al sistemului care are ca atribute ID, username, parola si rolul (manager/farmacist)
 */
public class User {
	protected int userID;
	protected String username;
	protected String password;
	protected Role role;
	
	
	/**
	 * Constructorul pentru clasa User
	 * @param userID 
	 * @param username
	 * @param password
	 */
	public User(int id, String username, String password, Role role) {
		this.userID = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public int getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public Role getRole() {
		return role;
	}
}

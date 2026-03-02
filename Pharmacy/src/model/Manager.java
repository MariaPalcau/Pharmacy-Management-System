package model;
/**
 * Clasa Manager reprezinta un utilizator cu rol de manager al sistemului
 * Extinde clasa User.
 */
public class Manager extends User{

	/** 
	 * Constructor al clasei Manager
	 * @param userID
	 * @param username
	 * @param password
	 */
	public Manager(int userID, String username, String password) {
		super(userID, username, password, Role.MANAGER);
	}
}

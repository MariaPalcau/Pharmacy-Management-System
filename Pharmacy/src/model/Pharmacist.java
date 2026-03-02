package model;

/**
 * Clasa Pharmacist reprezinta un utilizator cu rol de farmacist al sistemului
 * Extinde clasa User.
 */
public class Pharmacist extends User{

	/**
	 * Constructor pentru clasa Pharmacist
	 * @param userID
	 * @param username
	 * @param password
	 */
	public Pharmacist(int userID, String username, String password) {
		super(userID, username, password, Role.PHARMACIST);
	}
}

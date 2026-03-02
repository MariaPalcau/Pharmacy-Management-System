import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import model.Manager;
import model.Pharmacist;
import model.User;
import ui.LoginDialog;
import ui.ManagerFrame;
import ui.PharmacistFrame;
import utils.DBUtil;

/**
 * Punct de start a aplicatiei.
 * Este initializat database-ul, se chiama dialog-ul login pentru autentificarea utilizatorului care foloseste aplicatia
 * si in baza rolului este deschis ecranul frame principal.
 */
public class Main {
	
	public static void main(String[] args) {
		try {
	        DBUtil.createDB();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		    	  
		    	  LoginDialog loginDialog = new LoginDialog();
		    	  loginDialog.setModal(true);
		    	  loginDialog.setVisible(true); 
		          User user = loginDialog.getLoggedUser();
		            
		          if (user == null) {
		        	  System.exit(0);
		          }
		            
		          JFrame frame;
		          if (user instanceof Pharmacist) {
		        	  frame = new PharmacistFrame(user);
		          } 
		          else if (user instanceof Manager) {
		        	  frame = new ManagerFrame(user);
		          } 
		          else {
		        	  throw new IllegalStateException("Unknown role");
		          }
        
		          frame.setLocationRelativeTo(null);
		          frame.setVisible(true);
		      }
		    });
	}
}

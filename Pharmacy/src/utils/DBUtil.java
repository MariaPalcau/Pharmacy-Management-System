package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clasa pentru conectarea la baza de date si initializarea acesteia.
 */
public class DBUtil {
	public static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
	public static final String DB_URL = System.getProperty("db.url", "jdbc:derby://localhost:1527/pharm;create=true");
	
	/**
     * Creeaza baza de date si tabelele aferente, daca acestea nu exista deja.
     * Tabelele create includ: credentials, products, orders, order_items, invoices si invoice_items. 
     * Se insereaza utilizatori predefiniti in tabela credentials.
     * @throws ClassNotFoundException daca driverul JDBC nu este gasit
     */
	public static void createDB() throws ClassNotFoundException {
		try(Connection connection = DriverManager.getConnection(DB_URL)){
			CreateTable.createCredentialsTable(connection);
			CreateTable.insertDefaultUsers(connection);
			CreateTable.createProductsTable(connection);
			CreateTable.createOrdersTable(connection);
			CreateTable.createOrderItemsTable(connection);
		    CreateTable.createInvoicesTable(connection);
		    CreateTable.createInvoiceItemsTable(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

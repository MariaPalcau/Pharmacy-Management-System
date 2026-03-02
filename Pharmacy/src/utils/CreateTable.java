package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clasa pentru crearea si popularea tabelelor din baza de date.
 */
public class CreateTable {
	/**
     * Creeaza tabela "credentials" daca nu exista deja.
     * Tabela contine id, username, password si rolul utilizatorului.
     * @param connection conexiunea activa la baza de date
     */
	public static void createCredentialsTable(Connection connection) {
		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			try(ResultSet resultSet = dbMetaData.getTables(null, null, "CREDENTIALS", null)) {
				if(resultSet.next())
					System.out.println("The table " + resultSet.getString(3) + " already exists!");
				else {
					String create_credentials = "CREATE TABLE credentials (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
							+ "username VARCHAR(30) UNIQUE NOT NULL,"
							+ "password VARCHAR(30) NOT NULL,"
							+ "role VARCHAR(10) NOT NULL,"
							+ "CONSTRAINT role_check CHECK (role IN ('MANAGER', 'PHARMACIST')),"
							+ "CONSTRAINT id_pk PRIMARY KEY (id))";
					try(Statement stmt = connection.createStatement()){
						stmt.execute(create_credentials);
						System.out.println("Created table credentials");
					}
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Insereaza utilizatori predefiniti in tabela "credentials".
     * Daca utilizatorii exista deja, se afiseaza un mesaj si nu se insereaza din nou.
     * @param connection conexiunea
     */
	public static void insertDefaultUsers(Connection connection) { 
		String insert_users = "INSERT INTO credentials (username, password, role) VALUES (?, ?, ?)"; 
		try(PreparedStatement pstmt = connection.prepareStatement(insert_users)) { 
			pstmt.setString(1, "DanaCampeanu"); 
			pstmt.setString(2, "password"); 
			pstmt.setString(3, "MANAGER"); 
			pstmt.executeUpdate(); 
			pstmt.setString(1, "MirelaBanu"); 
			pstmt.setString(2, "pswd123"); 
			pstmt.setString(3, "PHARMACIST"); 
			pstmt.executeUpdate(); 
			pstmt.setString(1, "DanBrad"); 
			pstmt.setString(2, "00001234"); 
			pstmt.setString(3, "PHARMACIST"); 
			pstmt.executeUpdate(); 
		} catch (SQLException e) { 
			System.out.println("Users already exist.");
		} 
	}
	
	/**
     * Creeaza tabela "products" daca nu exista deja.
     * Tabela contine id, name, type, expiration_date, quantity, supplier si price.
     * @param connection conexiunea
     */
	public static void createProductsTable(Connection connection) {
		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			try(ResultSet resultSet = dbMetaData.getTables(null, null, "PRODUCTS", null)) {
				if(resultSet.next())
					System.out.println("The table " + resultSet.getString(3) + " already exists!");
				else {
					String createProducts = "CREATE TABLE products (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
							+ "name VARCHAR(30) UNIQUE NOT NULL,"
							+ "type VARCHAR(30),"
							+ "expiration_date DATE,"
							+ "quantity INTEGER NOT NULL,"
							+ "supplier VARCHAR(30),"
							+ "price DECIMAL(5,2) NOT NULL,"
							+ "CONSTRAINT id_prod_pk PRIMARY KEY (id))";
					try(Statement stmt = connection.createStatement()){
						stmt.execute(createProducts);
						System.out.println("Created table products");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Creeaza tabela "orders" daca nu exista deja.
     * Tabela contine id, date si status.
     * @param connection conexiunea
     */
	public static void createOrdersTable(Connection connection) {
		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			try(ResultSet resultSet = dbMetaData.getTables(null, null, "ORDERS", null)) {
				if(resultSet.next())
					System.out.println("The table " + resultSet.getString(3) + " already exists!");
				else {
					String createOrders = "CREATE TABLE orders (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
							+ "date DATE,"
							+ "status VARCHAR(10),"
							+ "CONSTRAINT status_check CHECK (status IN ('PENDING', 'APPROVED', 'DENIED', 'COMPLETED')),"
							+ "CONSTRAINT id_order_pk PRIMARY KEY (id))";
					try(Statement stmt = connection.createStatement()) {
						stmt.execute(createOrders);
						System.out.println("Created table orders");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Creeaza tabela "order_items" daca nu exista deja.
     * Tabela contine product_id, order_id si quantity, cu referinte la tabelul orders si products.
     * @param connection conexiunea
     */
	public static void createOrderItemsTable(Connection connection) {
		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			try(ResultSet resultSet = dbMetaData.getTables(null, null, "ORDER_ITEMS", null)) {
				if(resultSet.next())
					System.out.println("The table " + resultSet.getString(3) + " already exists!");
				else {
					String createOrderItems = "CREATE TABLE order_items (order_id INTEGER NOT NULL, "
							+ "product_id INTEGER NOT NULL,"
							+ "quantity INTEGER NOT NULL,"
							+ "CONSTRAINT order_product_pk PRIMARY KEY (order_id, product_id),"
							+ "CONSTRAINT order_id_fk FOREIGN KEY (order_id) REFERENCES orders(id)  ON DELETE CASCADE,"
							+ "CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES products(id)  ON DELETE CASCADE)";
					try(Statement stmt = connection.createStatement()) {
						stmt.execute(createOrderItems);
						System.out.println("Created table order_items");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * Creeaza tabela "invoices" daca nu exista deja.
     * Tabela contine id, user_id, date si price, cu referinta la tabela credentials.
     * @param connection conexiunea
     */
	public static void createInvoicesTable(Connection connection) {
		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			try(ResultSet resultSet = dbMetaData.getTables(null, null, "INVOICES", null)) {
				if(resultSet.next())
					System.out.println("The table " + resultSet.getString(3) + " already exists!");
				else {
					String createInvoices = "CREATE TABLE invoices (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
							+ "user_id INTEGER NOT NULL,"
							+ "date DATE,"
							+ "price DECIMAL(5,2) NOT NULL,"
							+ "CONSTRAINT id_invoice_pk PRIMARY KEY (id),"
							+ "CONSTRAINT invoice_user_fk FOREIGN KEY (user_id) REFERENCES credentials(id) ON DELETE CASCADE)";
					try(Statement stmt = connection.createStatement()) {
						stmt.execute(createInvoices);
						System.out.println("Created table invoices");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * Creeaza tabela "invoice_items" daca nu exista deja.
     * Tabela contine invoice_id, product_id si quantity, cu referinte la tabelul invoices si products.
     * @param connection conexiunea
     */
	public static void createInvoiceItemsTable(Connection connection) {
		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			try(ResultSet resultSet = dbMetaData.getTables(null, null, "INVOICE_ITEMS", null)) {
				if(resultSet.next())
					System.out.println("The table " + resultSet.getString(3) + " already exists!");
				else {
					String createInvoiceItems = "CREATE TABLE invoice_items (invoice_id INTEGER NOT NULL, "
							+ "product_id INTEGER NOT NULL,"
							+ "quantity INTEGER NOT NULL,"
							+ "CONSTRAINT invoice_product_pk PRIMARY KEY (invoice_id, product_id),"
							+ "CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,"
							+ "CONSTRAINT prod_id_fk FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE)";
					try(Statement stmt = connection.createStatement()) {
						stmt.execute(createInvoiceItems);
						System.out.println("Created table invoice_items");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	 /**
     * Sterge o tabela specificata din baza de date.
     * @param connection conexiunea activa la baza de date
     * @param tableName numele tabelei care va fi stearsa
     */
	public static void dropTable(Connection connection, String tableName) {
	    try (Statement stmt = connection.createStatement()) {
	        stmt.execute("DROP TABLE " + tableName);
	        System.out.println("Dropped table " + tableName);
	    } catch (SQLException e) {
	        System.out.println("Could not drop table " + tableName + ": " + e.getMessage());
	    }
	}
}

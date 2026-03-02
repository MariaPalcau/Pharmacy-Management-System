package tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDBUtil {
	private static String url = System.getProperty("db.url");
	
	public static void createTableCredentials() {
		try (Connection conn = DriverManager.getConnection(url);
	             Statement stmt = conn.createStatement()) {
			String create_credentials = "CREATE TABLE credentials (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
					+ "username VARCHAR(30) UNIQUE NOT NULL,"
					+ "password VARCHAR(30) NOT NULL,"
					+ "role VARCHAR(10) NOT NULL,"
					+ "CONSTRAINT role_check CHECK (role IN ('MANAGER', 'PHARMACIST')),"
					+ "CONSTRAINT id_pk PRIMARY KEY (id))";
			stmt.execute(create_credentials);
			stmt.executeUpdate("INSERT INTO credentials (username, password, role) VALUES ('admin', '1234', 'MANAGER')");
			stmt.executeUpdate("INSERT INTO credentials (username, password, role) VALUES ('pharmacist', 'pswd', 'PHARMACIST')");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void createTableProducts() {
		try (Connection conn = DriverManager.getConnection(url);
	             Statement stmt = conn.createStatement()) {
			String create_products = "CREATE TABLE products (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
					+ "name VARCHAR(30) UNIQUE NOT NULL,"
					+ "type VARCHAR(30),"
					+ "expiration_date DATE,"
					+ "quantity INTEGER NOT NULL,"
					+ "supplier VARCHAR(30),"
					+ "price DECIMAL(5,2) NOT NULL,"
					+ "CONSTRAINT id_prod_pk PRIMARY KEY (id))";
			stmt.execute(create_products);
			stmt.executeUpdate("INSERT INTO products (name, type, expiration_date, quantity, supplier, price) VALUES ('nurofen', 'ains', '2026-03-31', 10, 'sup1', 10.50)");
			stmt.executeUpdate("INSERT INTO products (name, type, expiration_date, quantity, supplier, price) VALUES ('borenar', 'antistaminic', '2027-05-31', 3, 'sup2', 8.99)");
		} catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableOrders() {
		try (Connection conn = DriverManager.getConnection(url);
	             Statement stmt = conn.createStatement()) {
			String create_orders = "CREATE TABLE orders (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
					+ "date DATE,"
					+ "status VARCHAR(10),"
					+ "CONSTRAINT status_check CHECK (status IN ('PENDING', 'APPROVED', 'DENIED', 'SHIPPED', 'COMPLETED')),"
					+ "CONSTRAINT id_order_pk PRIMARY KEY (id))";
			stmt.execute(create_orders);
			stmt.executeUpdate("INSERT INTO orders (date, status) VALUES ('2026-01-16', 'PENDING')");
			stmt.executeUpdate("INSERT INTO orders (date, status) VALUES ('2026-01-17', 'PENDING')");
			stmt.executeUpdate("INSERT INTO orders (date, status) VALUES ('2026-01-17', 'PENDING')");
			stmt.executeUpdate("INSERT INTO orders (date, status) VALUES ('2026-01-17', 'PENDING')");
		} catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableOrderItems() {
		try (Connection conn = DriverManager.getConnection(url);
	             Statement stmt = conn.createStatement()) {
			String create_order_items = "CREATE TABLE order_items (order_id INTEGER NOT NULL, "
					+ "product_id INTEGER NOT NULL,"
					+ "quantity INTEGER NOT NULL,"
					+ "CONSTRAINT order_product_pk PRIMARY KEY (order_id, product_id),"
					+ "CONSTRAINT order_id_fk FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,"
					+ "CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE)";
			stmt.execute(create_order_items);
			stmt.executeUpdate("INSERT INTO order_items (order_id, product_id, quantity) VALUES (1, 1, 3)");
			stmt.executeUpdate("INSERT INTO order_items (order_id, product_id, quantity) VALUES (1, 2, 5)");
			stmt.executeUpdate("INSERT INTO order_items (order_id, product_id, quantity) VALUES (2, 1, 6)");
			stmt.executeUpdate("INSERT INTO order_items (order_id, product_id, quantity) VALUES (2, 2, 3)");
			stmt.executeUpdate("INSERT INTO order_items (order_id, product_id, quantity) VALUES (4, 1, 3)");
			stmt.executeUpdate("INSERT INTO order_items (order_id, product_id, quantity) VALUES (4, 2, 5)");
		} catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableInvoices() {
		try (Connection conn = DriverManager.getConnection(url);
	             Statement stmt = conn.createStatement()) {
			String create_invoices = "CREATE TABLE invoices ("
			        + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
			        + "user_id INTEGER NOT NULL, "
			        + "date DATE, "
			        + "price DECIMAL(5,2) NOT NULL, "
			        + "CONSTRAINT id_invoice_pk PRIMARY KEY (id), "
			        + "CONSTRAINT invoice_user_fk FOREIGN KEY (user_id) REFERENCES credentials(id) ON DELETE CASCADE)";
			stmt.execute(create_invoices);
			stmt.executeUpdate("INSERT INTO invoices (user_id, date, price) VALUES (2,'2026-01-16', 76.45)");
			stmt.executeUpdate("INSERT INTO invoices (user_id, date, price) VALUES (2, '2026-01-17', 89.97)");
		} catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableInvoiceItems() {
		try (Connection conn = DriverManager.getConnection(url);
	             Statement stmt = conn.createStatement()) {
			String create_invoice_items = "CREATE TABLE invoice_items (invoice_id INTEGER NOT NULL, "
					+ "product_id INTEGER NOT NULL,"
					+ "quantity INTEGER NOT NULL,"
					+ "CONSTRAINT invoice_product_pk PRIMARY KEY (invoice_id, product_id),"
					+ "CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,"
					+ "CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE)";
			stmt.execute(create_invoice_items);
			stmt.executeUpdate("INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (1, 1, 3)");
			stmt.executeUpdate("INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (1, 2, 5)");
			stmt.executeUpdate("INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (2, 1, 6)");
			stmt.executeUpdate("INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (2, 2, 3)");
		} catch (SQLException e) {
            e.printStackTrace();
        }
	}
}

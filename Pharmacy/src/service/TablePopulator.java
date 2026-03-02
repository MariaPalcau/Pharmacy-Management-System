package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.OrderStatus;
import utils.DBUtil;

/**
 * Clasa pentru popularea tabelului JTable cu date din baza de date.
 * Transforma rezultatele SQL in modele JTable pentru afisarea in interfata grafica.
 */
public class TablePopulator {
	/**
     * Creeaza un TableModel pe baza unui ResultSet.
     * @param resultSet rezultatul unei interogari SQL
     * @return un TableModel corespunzator datelor din resultSet
     * @throws SQLException daca apare o eroare la citirea rezultatelor
     */
	public static TableModel buildTableModel(final ResultSet resultSet)
	            throws SQLException {
	        int columnCount = resultSet.getMetaData().getColumnCount();

	        Vector<String> columnNames = new Vector<>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            columnNames.add(resultSet.getMetaData().getColumnName(columnIndex));
	        }

	        Vector<Vector<Object>> dataVector = new Vector<>();
	        while (resultSet.next()) {
	            Vector<Object> rowVector = new Vector<>();
	            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	                rowVector.add(resultSet.getObject(columnIndex));
	            }
	            dataVector.add(rowVector);
	        }

	        return new DefaultTableModel(dataVector, columnNames);
	    }
	
	/**
     * Returneaza toate datele dintr-o tabela sub forma unui DefaultTableModel.
     * @param name numele tabelei
     * @return DefaultTableModel cu toate inregistrarile
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getTableModel(String name) throws SQLException {
	    String sql = "SELECT * FROM " + name;

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        return (DefaultTableModel) buildTableModel(rs);
	    }
	}
	
	/**
     * Returneaza datele dintr-o tabela filtrate dupa ID.
     * @param name numele tabelei
     * @param id valoarea ID-ului
     * @return DefaultTableModel cu inregistrarea corespunzatoare
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getTableModel(String name, int id) throws SQLException {
	    String sql = "SELECT * FROM " + name + " WHERE id=?";
	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	            PreparedStatement pstmt = conn.prepareStatement(sql)) {
	           pstmt.setInt(1, id);
	           try (ResultSet rs = pstmt.executeQuery()) {
	               return (DefaultTableModel) buildTableModel(rs);
	           }
	       }
	}
	
	/**
     * Returneaza produsele pentru selectare intr-un JTable simplu.
     * @return DefaultTableModel cu coloanele id, name, quantity
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getProductsForSelectTableModel() throws SQLException {
	    String sql = "SELECT id, name, quantity FROM products";

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        return (DefaultTableModel) buildTableModel(rs);
	    }
	}
	
	/**
     * Returneaza produsele si cantitatile dintr-o comanda dupa order_id.
     * @param id ID-ul comenzii
     * @return DefaultTableModel cu product_id si quantity
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getOrderItemsTableModel(int id) throws SQLException {
	    String sql = "SELECT product_id, quantity FROM order_items WHERE order_id=?";

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	    		PreparedStatement pstmt = conn.prepareStatement(sql)) {
	           pstmt.setInt(1, id);
	           try (ResultSet rs = pstmt.executeQuery()) {
	               return (DefaultTableModel) buildTableModel(rs);
	           }
	    }
	}
	
	/**
     * Creeaza un DefaultTableModel temporar pentru lista de produse dintr-o comanda.
     * @param prodList map cu product_id ca cheie si cantitatea ca valoare
     * @return DefaultTableModel temporar pentru afisarea in JTable
     */
	public static DefaultTableModel getOrderItemsTempTableModel(Map<Integer,Integer> prodList) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("product_id");
        columnNames.add("quantity");

        Vector<Vector<Object>> dataVector = new Vector<>();
        for(Map.Entry<Integer, Integer> pL: prodList.entrySet()) {
        	Vector<Object> rowVector = new Vector<>();
        	rowVector.add(pL.getKey());
        	rowVector.add(pL.getValue());
        	dataVector.add(rowVector);
        }
        return new DefaultTableModel(dataVector, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };	
	}
	
	/**
     * Returneaza produsele unei comenzi ce trebuie aprobate, impreuna cu detalii suplimentare.
     * @param id ID-ul comenzii
     * @return DefaultTableModel cu product_id, name, quantity, supplier
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getOrderItemsTableToApproveModel(int id) throws SQLException {
	    String sql = "SELECT oi.product_id, p.name, oi.quantity, p.supplier FROM order_items oi JOIN orders o ON oi.order_id=o.id JOIN products p ON oi.product_id=p.id WHERE order_id=? AND status=?";

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	    		PreparedStatement pstmt = conn.prepareStatement(sql)) {
	           pstmt.setInt(1, id);
	           pstmt.setString(2, OrderStatus.PENDING.name());
	           try (ResultSet rs = pstmt.executeQuery()) {
	               return (DefaultTableModel) buildTableModel(rs);
	           }
	    }
	}
	
	/**
     * Returneaza produsele si preturile aferente unei facturi.
     * @param id ID-ul facturii
     * @return DefaultTableModel cu product_id, name, quantity, price per quantity si total price
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getInvoiceItemsTableModel(int id) throws SQLException {
	    String sql = "SELECT i.product_id, p.name, i.quantity, p.price \"PRICE PER QUANTITY\", p.price*i.quantity \"TOTAL PRICE\" FROM invoice_items i JOIN products p ON i.product_id=p.id WHERE invoice_id=?";

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	    		PreparedStatement pstmt = conn.prepareStatement(sql)) {
	           pstmt.setInt(1, id);
	           try (ResultSet rs = pstmt.executeQuery()) {
	               return (DefaultTableModel) buildTableModel(rs);
	           }
	    }
	}
	
	/**
     * Returneaza comenzile care sunt in asteptare pentru aprobare.
     * @return DefaultTableModel cu comenzile PENDING
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getTableToApproveModel() throws SQLException {
	    String sql = "SELECT * FROM orders WHERE status=?";

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	    		pstmt.setString(1, OrderStatus.PENDING.name());
	    		try (ResultSet rs = pstmt.executeQuery()) {
		               return (DefaultTableModel) buildTableModel(rs);
		        }
	    }
	}
	
	/**
     * Returneaza produsele care expira in urmatoarele 3 luni sau deja au expirat.
     * @return DefaultTableModel cu id, name si expiration_date
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getTableExpiringProducts() throws SQLException {
	    String sql = "SELECT id, name, expiration_date FROM products WHERE (expiration_date BETWEEN CURRENT_DATE AND ?) OR (expiration_date < CURRENT_DATE)";
	    LocalDate today = LocalDate.now();
        LocalDate threeMonthsLater = today.plusMonths(3);
	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	    		PreparedStatement pstmt = conn.prepareStatement(sql)) {
	    		pstmt.setDate(1, Date.valueOf(threeMonthsLater));
	    		try(ResultSet rs = pstmt.executeQuery()){
	    			return (DefaultTableModel) buildTableModel(rs);
	    		}
	    }
	}
	
	 /**
     * Returneaza produsele cu stoc scazut (<= 3).
     * @return DefaultTableModel cu id, name si quantity
     * @throws SQLException daca apare o eroare SQL
     */
	public static DefaultTableModel getTableLowStockProducts() throws SQLException {
	    String sql = "SELECT id, name, quantity FROM products WHERE quantity <= 3";

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	         Statement stmt = conn.createStatement();
	    		ResultSet rs = stmt.executeQuery(sql)) {
		        	return (DefaultTableModel) buildTableModel(rs);
	    }
	}
}

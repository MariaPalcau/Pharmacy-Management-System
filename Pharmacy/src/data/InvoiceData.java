package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import model.Invoice;
import utils.DBUtil;

/**
 * Clasa pentru operatii legate de facturile stocate in baza de date.
 * Contine metode pentru adaugarea si cautarea facturilor, obtinerea produselor asociate,
 * precum si calcularea vanzarilor si generarea de rapoarte.
 */
public class InvoiceData {
	 /**
     * Adauga o factura noua impreuna cu produsele asociate in baza de date.
     * Daca inserarea facturii sau a produselor esueaza, toate modificarile sunt anulate (rollback).
     * @param i obiectul Invoice care contine datele facturii
     * @return true daca factura a fost adaugata cu succes, false in caz contrar
     */
	public static boolean addInvoice(Invoice i) {
		String sql = "INSERT INTO invoices (user_id, date, price) VALUES (?, ?, ?)";
		String sql2 = "INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL)){
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
				pstmt.setInt(1, i.getUserID());
				pstmt.setDate(2, Date.valueOf(i.getOrderDate()));
				pstmt.setDouble(3, i.getTotalPrice());
				int affectedRows = pstmt.executeUpdate();
				
				if (affectedRows == 0) {
	                conn.rollback();
	                return false;
	            }
				
				try (ResultSet rs = pstmt.getGeneratedKeys()){
					if (rs.next()) {
						i.setInvoiceID(rs.getInt(1));
					} else {
						 conn.rollback();
		                 return false;
					}
				}
			}
			
			try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)){
				for(Map.Entry<Integer, Integer> pl: i.getProductList().entrySet()) {
					pstmt2.setInt(1, i.getInvoiceID());
					pstmt2.setInt(2, pl.getKey());
					pstmt2.setInt(3, pl.getValue());
					pstmt2.executeUpdate();
				}
			}
			
			conn.commit();
	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	 /**
     * Cauta o factura dupa ID.
     * @param invoice_id ID-ul facturii
     * @return obiectul Invoice daca exista, null in caz contrar
     */
	public static Invoice searchInvoice(int invoice_id) {
		String sql = "SELECT id, user_id, date, price FROM invoices WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, invoice_id);
			ResultSet rs = pstmt.executeQuery();
			Invoice i = null;
			if (rs.next()) {
				i = new Invoice(invoice_id, rs.getInt("id"), rs.getDate("date").toLocalDate(), rs.getDouble("price"));
			}
			return i;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * Returneaza lista de produse si cantitatile aferente unei facturi.
     * @param invoice_id ID-ul facturii
     * @return map cu ID produs ca cheie si cantitatea ca valoare
     */
	public static Map<Integer, Integer> getInvoiceWithItems(int invoice_id) {
		String sql = "SELECT product_id, quantity FROM invoice_items WHERE invoice_id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, invoice_id);
			ResultSet rs = pstmt.executeQuery();
			Map<Integer,Integer> prodList = new HashMap<>();
			while (rs.next()) {
				prodList.put(rs.getInt("product_id"), rs.getInt("quantity"));
			}
			return prodList;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * Calculeaza valoarea totala a vanzarilor din ziua curenta.
     * @return suma totala a vanzarilor
     */
	public static double todaySales() {
		double total=0.0;
		String sql = "SELECT price FROM invoices WHERE date=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL); 
				PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setDate(1, Date.valueOf(LocalDate.now()));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					total += rs.getDouble("price");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	/**
     * Calculeaza valoarea totala a vanzarilor din luna curenta.
     * @return suma totala a vanzarilor
     */
	public static double monthSales() {
		double total=0.0;
		String sql = "SELECT price FROM invoices WHERE YEAR(date) = ? AND MONTH(date) = ?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL); 
				PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setInt(1, LocalDate.now().getYear());
				pstmt.setInt(2, LocalDate.now().getMonthValue());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					total += rs.getDouble("price");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	 /**
     * Returneaza primele cinci produse cel mai bine vandute.
     * @return map cu numele produsului si cantitatea vanduta
     */
	public static Map<String, Integer> getTopFiveProductsSold(){
		Map<String, Integer> top = new HashMap<>();
		String sql = "SELECT i.product_id, p.name, SUM(i.quantity) total FROM invoice_items i JOIN products p ON i.product_id=p.id "
				+ "GROUP BY i.product_id, p.name "
				+ "ORDER BY total DESC FETCH FIRST 5 ROWS ONLY";
		try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
		         Statement stmt = conn.createStatement();
		         ResultSet rs = stmt.executeQuery(sql)) {
		        while (rs.next()) {
		            top.put(rs.getString("name"), rs.getInt("total"));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return top;
	}
	
	/**
     * Returneaza distributia vanzarilor pe angajati.
     * @return map cu username ca cheie si numarul de facturi ca valoare
     */
	public static Map<String, Integer> getUserSalesDistribution() {
		Map<String, Integer> employeePerformance = new HashMap<>();
		String sql = "SELECT user_id, username, count(user_id) nr FROM invoices JOIN credentials ON invoices.user_id=credentials.id "
				+ "GROUP BY user_id, username";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while(rs.next()) {
				employeePerformance.put(rs.getString("username"), rs.getInt("nr"));
			}
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return employeePerformance;
	}
	
	public static double calculateTotalPrice(Map<Integer, Integer> items) {
		double totalPrice = 0.0;
		try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
	             PreparedStatement pstmt = conn.prepareStatement("SELECT price FROM products WHERE id = ?")) {
	            	for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
	            		pstmt.setInt(1, entry.getKey());
	            		try (ResultSet rs = pstmt.executeQuery()) {
	            			while (rs.next()) {
	            				totalPrice += rs.getDouble("price") * entry.getValue();
	            			}
	            		}
	            	}
	        	} catch (SQLException e) {
	        		e.printStackTrace();
	        	}
		return totalPrice;
	}
}

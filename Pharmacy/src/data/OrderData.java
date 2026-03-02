package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import model.Order;
import model.OrderStatus;
import utils.DBUtil;

/**
 * Clasa pentru operatii legate de comenzi stocate in baza de date.
 * Contine metode pentru adaugarea, cautarea,
 * actualizarea, stergerea si procesarea comenzilor din baza de date.
 */
public class OrderData {
	/**
     * Adauga o comanda noua in baza de date impreuna cu produsele asociate.
     * Daca inserarea comenzii sau a produselor esueaza, toate modificarile sunt anulate (rollback).
     * @param o obiectul Order care contine datele comenzii
     * @return true daca comanda a fost adaugata cu succes, false in caz contrar
     */
	public static boolean addOrder(Order o) {
	    String sql = "INSERT INTO orders (date, status) VALUES (?, ?)";
	    String sql2 = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";

	    try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL)) {
	        conn.setAutoCommit(false);

	        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
	            pstmt.setDate(1, Date.valueOf(o.getOrderDate()));
	            pstmt.setString(2, o.getOrderStatus().name());
	            int affectedRows = pstmt.executeUpdate();

	            if (affectedRows == 0) {
	                conn.rollback();
	                return false;
	            }

	            try (ResultSet rs = pstmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    o.setOrderID(rs.getInt(1));
	                } else {
	                    conn.rollback();
	                    return false;
	                }
	            }
	        }

	        try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
	            for (Map.Entry<Integer, Integer> entry : o.getProductList().entrySet()) {
	                pstmt2.setInt(1, o.getOrderID());
	                pstmt2.setInt(2, entry.getKey());
	                pstmt2.setInt(3, entry.getValue());
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
     * Cauta o comanda dupa ID.
     * @param order_id ID-ul comenzii
     * @return obiectul Order daca exista, null in caz contrar
     */
	public static Order searchOrder(int order_id) {
		String sql = "SELECT date, status FROM orders WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, order_id);
	        ResultSet rs = pstmt.executeQuery();
	        Order o = null;
	        if (rs.next()) {
	            o = new Order(order_id, rs.getDate("date").toLocalDate(), OrderStatus.valueOf(rs.getString("status")));
	        }
	          return o;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * Returneaza lista de produse si cantitatile aferente unei comenzi.
     * @param order_id ID-ul comenzii
     * @return map cu ID produs ca cheie si cantitatea ca valoare
     */
	public static Map<Integer, Integer> getOrderWithItems(int order_id) {
		String sql = "SELECT product_id, quantity FROM order_items WHERE order_id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, order_id);
			ResultSet rs = pstmt.executeQuery();
			Map<Integer,Integer> prodList = new HashMap<>();
			while (rs.next()) {
				prodList.put(rs.getInt("product_id"), rs.getInt("quantity"));
			}
			return prodList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * Actualizeaza produsele asociate unei comenzi.
     * Daca lista de produse este goala, comanda este stearsa.
     * @param o obiectul Order care contine datele actualizate
     * @return true daca operatia a fost realizata cu succes, false in caz contrar
     */
	public static boolean updateOrder(Order o) {
		if (o.getProductList().isEmpty()) {
			deleteOrder(o.getOrderID());
			return true;
	    }
		
		String sql = "DELETE FROM order_items WHERE order_id=? ";
		String sql2 = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
		
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL)){
			conn.setAutoCommit(false);
			try(PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setInt(1, o.getOrderID());
				pstmt.executeUpdate();
			}
			
			try(PreparedStatement pstmt2 = conn.prepareStatement(sql2)){
				for(Map.Entry<Integer, Integer> pl: o.getProductList().entrySet()) {
					pstmt2.setInt(1, o.getOrderID());
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
     * Sterge o comanda din baza de date.
     * @param order_id ID-ul comenzii care va fi stearsa
     * @return true daca stergerea a fost realizata cu succes, false in caz contrar
     */
	public static boolean deleteOrder(int order_id) {
		String sql = "DELETE FROM orders WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL)) {
			conn.setAutoCommit(false);
			
			try(PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setInt(1, order_id);
				int rows = pstmt.executeUpdate();
				if (rows == 0) {
	                conn.rollback();
	                return false;
	            }
			}
			conn.commit();
			return true;
		}
		catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
     * Aproba sau respinge o comanda.
     * @param order_id ID-ul comenzii
     * @param approved true daca comanda este aprobata, false daca este respinsa
     */
	public static void approveOrder(int order_id, boolean approved) {
		String sql = "UPDATE orders SET status=? WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			if(approved)
				pstmt.setString(1, OrderStatus.APPROVED.name());
			else
				pstmt.setString(1, OrderStatus.DENIED.name());
			pstmt.setInt(2, order_id);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * Marcheaza o comanda ca fiind finalizata.
     * @param order_id ID-ul comenzii
     */
	public static void completeOrder(int order_id) {
		String sql = "UPDATE orders SET status=? WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, OrderStatus.COMPLETED.name());
			pstmt.setInt(2, order_id);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}

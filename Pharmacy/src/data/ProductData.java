package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import model.Product;
import utils.DBUtil;

/**
 * Clasa pentru gestionarea produselor in baza de date.
 * Contine metode pentru adaugarea, cautarea, actualizarea, stergerea produselor si modificarea stocurilor.
 */
public class ProductData {
	/**
     * Adauga un produs nou in baza de date.
     * @param p obiectul Product care contine datele produsului
     * @return true daca produsul a fost adaugat cu succes, false daca exista deja un produs cu acelasi nume
     */
	public static boolean addProduct(Product p) {
		String sql = "INSERT INTO products (name, type, expiration_date, quantity, supplier, price) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, p.getProductName());
				pstmt.setString(2, p.getProductType());
				pstmt.setDate(3, java.sql.Date.valueOf(p.getExpirationDate()));
				pstmt.setInt(4, p.getQuantity());
				pstmt.setString(5, p.getSupplier());
				pstmt.setDouble(6, p.getPrice());
				pstmt.executeUpdate();
		        return true;
		    } catch (SQLIntegrityConstraintViolationException e) {
		        return false;
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		return false;
	}
	
	/**
     * Cauta un produs dupa ID.
     * @param product_id ID-ul produsului
     * @return obiectul Product daca exista, null in caz contrar
     */
	public static Product searchProduct(int product_id) {
		String sql = "SELECT name, type, expiration_date, quantity, supplier, price FROM products WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, product_id);
			ResultSet rs = pstmt.executeQuery();
			Product p = null;
			if (rs.next()) {
				p = new Product(product_id, rs.getString("name"), rs.getString("type"), 
						rs.getDate("expiration_date").toLocalDate(), rs.getInt("quantity"), 
						rs.getString("supplier"), rs.getDouble("price"));
			}
			return p;
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/**
     * Cauta un produs dupa nume
     * @param product_name numele produsului
     * @return obiectul Product daca exista, null in caz contrar
     */
	public static Product searchProductbyName(String product_name) {
		String sql = "SELECT id, name, type, expiration_date, quantity, supplier, price FROM products WHERE LOWER(name)=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setString(1, product_name.toLowerCase());
	        ResultSet rs = pstmt.executeQuery();
	        Product p = null;
	        if (rs.next()) {
	            p = new Product(
	            	rs.getInt("id"),
	                rs.getString("name"),
	                rs.getString("type"),
	                rs.getDate("expiration_date").toLocalDate(),
	                rs.getInt("quantity"),
	                rs.getString("supplier"),
	                rs.getDouble("price")
	            );
	        }
	        return p;
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
       
	}
	
	/**
     * Actualizeaza datele unui produs existent.
     * @param p obiectul Product cu informatiile actualizate
     * @return true daca actualizarea a fost realizata cu succes, false in caz contrar
     */
	public static boolean updateProduct(Product p) {
		String sql = "UPDATE products SET name=?, type=?, expiration_date=?, quantity=?, supplier=?, price=? WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setString(1, p.getProductName());
			pstmt.setString(2, p.getProductType());
			pstmt.setDate(3, java.sql.Date.valueOf(p.getExpirationDate()));
			pstmt.setInt(4, p.getQuantity());
			pstmt.setString(5, p.getSupplier());
			pstmt.setDouble(6, p.getPrice());
			pstmt.setInt(7, p.getProductID());
			pstmt.executeUpdate();
			return true;
		}  catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	/**
     * Sterge un produs din baza de date.
     * @param product_id ID-ul produsului care va fi sters
     * @return true daca stergerea a fost realizata cu succes altfel false
     */
	public static boolean deleteProduct(int product_id) {
		String sql = "DELETE FROM products WHERE id=?";
		try(Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, product_id);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	/**
     * Scade cantitatea produselor din stoc cand se face o vanzare.
     * Daca stocul este insuficient pentru un produs, toate modificarile sunt anulate.
     * @param prodList map cu ID produs ca cheie si cantitatea vanduta ca valoare
     */
	public static void decreaseQuantity(Map<Integer, Integer> prodList) {
		String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Map.Entry<Integer, Integer> entry : prodList.entrySet()) {
                int productId = entry.getKey();
                int quantitySold = entry.getValue();

                pstmt.setInt(1, quantitySold);
                pstmt.setInt(2, productId);
                pstmt.setInt(3, quantitySold);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    throw new SQLException("Not enough stock for product ID " + productId);
                }
            }
            conn.commit();
        } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
	
	/**
     * Mareste cantitatea produselor din stoc in baza comenzilor catre furnizor.
     * Operatia este tranzactionala.
     * @param prodList map cu ID produs si cantitatea adaugata
     */
	public static void increaseQuantity(Map<Integer, Integer> prodList) {
		String sql = "UPDATE products SET quantity = quantity + ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DBUtil.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Map.Entry<Integer, Integer> entry : prodList.entrySet()) {
                int productId = entry.getKey();
                int quantityNew = entry.getValue();

                pstmt.setInt(1, quantityNew);
                pstmt.setInt(2, productId);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    throw new SQLException();
                }
            }
            conn.commit();
        } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
}

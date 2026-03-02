package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Product;
import service.ProductService;

class TestManageProducts {

    @BeforeAll
    static void setup() {
	    System.setProperty("db.url", "jdbc:derby:memory:pharmacyDB;create=true");
	    TestDBUtil.createTableProducts();
	}

    @Test
    void testAddProduct() {
    	boolean result;
    	String input = "2026-01-17";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
		try {
			date = sdf.parse(input);
			result = ProductService.addProduct("ibuprofen", "ains", date, 20, "Supplier1", 5.0);
			assertTrue(result);
			Product p = ProductService.searchProduct("ibuprofen");
			assertNotNull(p);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    void testAddProductDuplicate() {
    	boolean result;
    	String input = "2028-03-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
        	date = sdf.parse(input);
        	result = ProductService.addProduct("nurofen", "ains", date, 10, "sup1", 12.50);
        	assertFalse(result);
        }  catch (ParseException e) {
			e.printStackTrace();
		}
		
    }
    
    @Test
    void testSearchProductNameFound() {
    	Product p = ProductService.searchProduct("Borenar");
		assertNotNull(p);
    }
    
    @Test
    void testSearchProductIdFound() {
    	Product p = ProductService.searchProduct("2");
    	assertNotNull(p);
    }
    
    @Test
    void testSearchProductNameNotFound() {
    	Product p = ProductService.searchProduct("multivitamine");
		assertNull(p);
    }
    
    @Test
    void testSearchProductIdNotFound() {
    	Product p = ProductService.searchProduct("3");
		assertNull(p);
    }

    @Test
    void testUpdateProduct() {
    	boolean result;
    	String input = "2026-03-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
        	date = sdf.parse(input);
        	result = ProductService.updateProduct(1, "nurofen", "ains", date, 20, "sup1", 5.0);
        	assertTrue(result);
        	Product p = ProductService.searchProduct("nurofen");
        	assertEquals(p.getQuantity(), 20);
        	assertEquals(p.getPrice(), 5.0);
        } catch (ParseException e) {
			e.printStackTrace();
		}
		
    }

    @Test
    void testDeleteProduct() {
        boolean result;
        result = ProductService.deleteProduct(1);
        assertTrue(result);
        Product p = ProductService.searchProduct("1");
        assertNull(p);
    }
}

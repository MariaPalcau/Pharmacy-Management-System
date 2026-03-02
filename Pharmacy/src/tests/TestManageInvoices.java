package tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import data.InvoiceData;
import data.ProductData;
import model.Invoice;

class TestManageInvoices {

	@BeforeAll
    static void setup() {
	    System.setProperty("db.url", "jdbc:derby:memory:pharmacyDB;create=true");
	    TestDBUtil.createTableCredentials();
	    TestDBUtil.createTableProducts();
	    TestDBUtil.createTableInvoices();
	    TestDBUtil.createTableInvoiceItems();
	}

    @Test
    void testAddInvoice() {
    	boolean result;
    	Invoice invoice = new Invoice();
    	invoice.setUserID(2);
        Map<Integer, Integer> items = new HashMap<>();
        items.put(1, 2);
        int quantityBefore = ProductData.searchProduct(1).getQuantity();
        invoice.setProductList(items);
        invoice.setTotalPrice(InvoiceData.calculateTotalPrice(items));
        result = InvoiceData.addInvoice(invoice);
        ProductData.decreaseQuantity(invoice.getProductList());
        assertTrue(result);
        Invoice i = InvoiceData.searchInvoice(invoice.getInvoiceID());
        assertNotNull(i);
        assertEquals(i.getTotalPrice(), 21);
        Map<Integer, Integer> prods = InvoiceData.getInvoiceWithItems(invoice.getInvoiceID());
        assertEquals(prods.get(1), 2);
        assertEquals(quantityBefore-2, ProductData.searchProduct(1).getQuantity());
    }
    
    @Test
    void testSearchInvoiceFound(){
        Invoice i = InvoiceData.searchInvoice(1);
        assertNotNull(i);
        assertEquals(1, i.getInvoiceID());
        assertEquals(76.45, i.getTotalPrice());
        Map<Integer, Integer> prods = InvoiceData.getInvoiceWithItems(i.getInvoiceID());
        assertEquals(prods.get(1), 3);
        assertEquals(prods.get(2), 5);
    }
    
    @Test
    void testSearchInvoiceNotFound(){
        Invoice i = InvoiceData.searchInvoice(20);
        assertNull(i);
    }
}

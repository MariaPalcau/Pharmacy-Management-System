package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import data.OrderData;
import data.ProductData;
import model.Order;
import model.OrderStatus;

class TestManageOrders {

	@BeforeAll
    static void setup() {
	    System.setProperty("db.url", "jdbc:derby:memory:pharmacyDB;create=true");
	    TestDBUtil.createTableProducts();
	    TestDBUtil.createTableOrders();
	    TestDBUtil.createTableOrderItems();
	}

    @Test
    void testAddOrder() {
    	boolean result;
    	Order order = new Order();
    	Map<Integer, Integer> products = new HashMap<>();
        products.put(1, 1);
        order.setProductList(products);
		result = OrderData.addOrder(order);
		assertTrue(result);
        Order ord = OrderData.searchOrder(order.getOrderID());
        assertNotNull(ord);
        Map<Integer, Integer> prods = OrderData.getOrderWithItems(order.getOrderID());
        assertEquals(prods.get(1), 1);
    }
    
    @Test
    void testSearchOrderFound(){
        Order order = OrderData.searchOrder(4);
        assertNotNull(order);
        assertEquals(4, order.getOrderID());
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
        Map<Integer, Integer> prods = OrderData.getOrderWithItems(order.getOrderID());
        assertEquals(prods.get(1), 3);
        assertEquals(prods.get(2), 5);
    }
    
    @Test
    void testSearchOrderNotFound(){
        Order order = OrderData.searchOrder(10);
        assertNull(order);
    }

    @Test
    void testUpdateOrder() {
    	boolean result;
    	Order order = new Order();
    	Map<Integer, Integer> products = new HashMap<>();
        products.put(1, 1);
        order.setProductList(products);
		OrderData.addOrder(order);
    	order.removeItem(1);
    	order.addItem(2, 1);
    	result = OrderData.updateOrder(order);
        assertTrue(result);
        Map<Integer, Integer> prods = OrderData.getOrderWithItems(order.getOrderID());
        assertFalse(prods.containsKey(1));
        assertTrue(prods.containsKey(2));
        assertEquals(prods.get(2), 1);
    }

    @Test
    void testDeleteOrder() {
        boolean result;
        Order order = OrderData.searchOrder(2);
        result = OrderData.deleteOrder(order.getOrderID());
        assertTrue(result);
        Order o = OrderData.searchOrder(2);
        assertNull(o);
        Map<Integer, Integer> prods = OrderData.getOrderWithItems(order.getOrderID());
        assertTrue(prods == null || prods.isEmpty());
    }
    
    @Test
    void testApproveNotOrder() {
    	Order order = OrderData.searchOrder(3);
    	OrderData.approveOrder(order.getOrderID(), false);
    	Order updatedOrder = OrderData.searchOrder(order.getOrderID());
    	assertEquals(OrderStatus.DENIED, updatedOrder.getOrderStatus());
    }
    
    @Test
    void testCompletedOrder() {
    	Order order = OrderData.searchOrder(1);
    	OrderData.approveOrder(1, true);
    	order = OrderData.searchOrder(order.getOrderID());
        assertEquals(OrderStatus.APPROVED, order.getOrderStatus());
        order.setProductList(OrderData.getOrderWithItems(order.getOrderID()));
        OrderData.completeOrder(1);
        ProductData.increaseQuantity(order.getProductList());
        Order completedOrder = OrderData.searchOrder(1);
        assertEquals(OrderStatus.COMPLETED, completedOrder.getOrderStatus());
        assertEquals(13, ProductData.searchProduct(1).getQuantity());
        assertEquals(8, ProductData.searchProduct(2).getQuantity());
    }
    
}

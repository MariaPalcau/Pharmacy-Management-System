package model;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/** 
 * Clasa Order reprezinta o comanda de produse de la furnizor care are ca atribute ID, data de comanda, statusul comenzii (implicit PENDING) si 
 * lista de ID produselor cu cantitatea lor.
 */
public class Order {
	private int orderID;
	private LocalDate orderDate;
	private OrderStatus orderStatus;
	private Map<Integer, Integer> productList;
	
	/**
	 * Constructorul fara paramentri pentru clasa Order care initializeaza campurile data comenzii, status-ul si lista de produse cu valori implicite
	 */
	public Order() {
        this.orderDate = LocalDate.now();
        this.orderStatus = OrderStatus.PENDING;
        this.productList = new HashMap<>();
    }
	
	/**
	 * Constructorul pentru clasa Order
	 * @param orderID 
	 * @param orderDate
	 * @param orderStatus 
	 */
	public Order(int orderID, LocalDate orderDate, OrderStatus orderStatus) {
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.productList = new HashMap<>();
	}

	public int getOrderID() {
		return orderID;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	
	public Map<Integer, Integer> getProductList() {
		return productList;
	}
	
	public void setOrderID(int id) {
		this.orderID=id;
	}
	
	public void setProductList(Map<Integer, Integer> pL) {
		this.productList=pL;
	}

	/**
	 * Functie pentru afisarea comenzii
	 */
	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus
				+ ", productList=" + productList + "]";
	}
	
	/**
	 * Functie pentru a adauga elemente in lista de produse a unei comenzi
	 * @param productId id-ul produsului
	 * @param quantity cantitate de produs comandata
	 */
	public void addItem(int productId, int quantity) {
		if (quantity == 0) {
			throw new IllegalArgumentException("Quantity must be > 0");
		}
	    this.productList.merge(productId, quantity, Integer::sum);
	}
	
	/**
	 * Functie pentru a elimina elemente din lista de produse a unei comenzi
	 * @param productId id-ul produsului
	 */
	public void removeItem(int productId) {
		this.productList.remove(productId);
	}
	
	/**
	 * Functie pentru a modifica cantitatea unui element din lista de produse a unei comenzi
	 * @param productId id-ul produsului
	 * @param quantity cantitate de produs comandata
	 */
	public void updateQuantity(int productId, int quantity) {
		if (quantity == 0)
			removeItem(productId);
		else
	        this.productList.put(productId, quantity);
	}
}

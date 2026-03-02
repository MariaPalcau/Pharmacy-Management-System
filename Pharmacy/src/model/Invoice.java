package model;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/** 
 * Clasa Invoice reprezinta o factura care are ca atribute ID, ID utilizatorului care a emis-o, data de emitere, pretul total si 
 * lista de ID produselor cu cantitatea fiecaruia
 */
public class Invoice {
	private int invoiceID;
	private int userID;
	private LocalDate orderDate;
	private double totalPrice;
	private Map<Integer, Integer> productList;
	
	/**
	 * Constructorul fara paramentri pentru clasa Invoice care initializeaza campurile data cumparaturii, suma totala si lista de produse cu valori implicite
	 */
	public Invoice(){
		this.orderDate = LocalDate.now();
		this.totalPrice = 0.0;
		this.productList = new HashMap<>();
	}
	
	/**
	 * Constructor pentru clasa Invoice
	 * @param invoiceID
	 * @param userID
	 * @param orderDate
	 * @param totalPrice
	 */
	public Invoice(int invoiceID, int userID, LocalDate orderDate, double totalPrice) {
		this.invoiceID = invoiceID;
		this.userID = userID;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.productList = new HashMap<>();
	}

	public int getInvoiceID() {
		return invoiceID;
	}
	
	public int getUserID() {
		return userID;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public Map<Integer,Integer> getProductList() {
		return productList;
	}
	
	public void setInvoiceID(int id) {
		this.invoiceID=id;
	}
	
	public void setUserID(int id) {
		this.userID=id;
	}
	
	public void setTotalPrice(double price) {
		this.totalPrice=price;
	}
	
	public void setProductList(Map<Integer, Integer> pL) {
		this.productList=pL;
	}

	/**
	 * Functie pentru afisarea facturii
	 */
	@Override
	public String toString() {
		return "Invoice [invoiceID=" + invoiceID + ", userID=" + userID + ", orderDate=" + orderDate + ", totalPrice="
				+ totalPrice + ", productList=" + productList + "]";
	}
	
	/**
	 * Functie pentru a adauga elemente in lista de produse a unei facturi
	 * @param productId id-ul produsului
	 * @param quantity cantitate de produs cumparata
	 */
	public void addItem(int productId, int quantity) {
		if (quantity == 0) {
			throw new IllegalArgumentException("Quantity must be > 0");
		}
	    productList.merge(productId, quantity, Integer::sum);
	}
	 
	/**
	 * Functie pentru a elimina elemente din lista de produse a unei facturi
	 * @param productId id-ul produsului
	 */
	public void removeItem(int productId) {
	    productList.remove(productId);
	}
	 
	/**
	 * Functie pentru a modifica cantitatea unui element din lista de produse a unei facturi
	 * @param productId id-ul produsului
	 * @param quantity cantitate de produs cumparata
	 */
	public void updateQuantity(int productId, int quantity) {
		if (quantity == 0)
			removeItem(productId);
		else
	        productList.put(productId, quantity);
	}
}

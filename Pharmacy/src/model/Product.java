package model;
import java.time.LocalDate;

/** 
 * Clasa Product reprezinta un medicament care are ca atribute ID, nume, tip, data de expirare, cantitatea pe stoc, producatorul si pretul
 */
public class Product {
	private int productID;
	private String productName;
	private String productType;
	private LocalDate expirationDate;
	private int quantity;
	private String supplier;
	private double price;
	
	public Product() {}
	
	public Product(String productName, String productType, LocalDate expirationDate, int quantity, String supplier, double price) {
		this.productName = productName;
		this.productType = productType;
		this.expirationDate = expirationDate;
		this.quantity = quantity;
		this.supplier = supplier;
		this.price = price;
	}

	/**
	 * Costructorul pentru clasa Product
	 * @param productID
	 * @param productName
	 * @param productType
	 * @param expirationDate
	 * @param quantity
	 * @param supplier
	 * @param price
	 */
	public Product(int productID, String productName, String productType, LocalDate expirationDate, int quantity, String supplier,
			double price) {
		this.productID = productID;
		this.productName = productName;
		this.productType = productType;
		this.expirationDate = expirationDate;
		this.quantity = quantity;
		this.supplier = supplier;
		this.price = price;
	}

	public int getProductID() {
		return productID;
	}
	
	public void setProductID(int product_id) {
		this.productID=product_id;
	}

	public String getProductName() {
		return productName;
	}
	
	public String getProductType() {
		return productType;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSupplier() {
		return supplier;
	}

	public double getPrice() {
		return price;
	}
	
	/**
	 * Functie pentru afisarea produsului
	 */
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName=" + productName + ", productType=" + productType
				+ ", expirationDate=" + expirationDate + ", quantity=" + quantity + ", supplier=" + supplier
				+ ", price=" + price + "]";
	}
}

package service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import data.ProductData;
import model.Product;

/**
 * Clasa serviciu pentru gestionarea produselor.
 * Aceasta clasa contine metode pentru validarea datelor, adaugarea, cautarea, actualizarea si stergerea produselor.
 */
public class ProductService {
	/**
     * Valideaza numele unui produs
     * @param name numele produsului
     * @return true daca numele este valid, false in caz contrar
     */
	public static boolean validateInput(String name) {
		if (name == null || name.trim().isEmpty()) {
	        return false;
	    }
		return true;
	}
	
	/**
     * Adauga un produs nou in baza de date.
     * @param name numele produsului
     * @param type tipul produsului
     * @param expDate data expirarii
     * @param quantity cantitatea disponibila
     * @param supplier numele furnizorului
     * @param price pretul produsului
     * @return true daca produsul a fost adaugat cu succes, false in caz contrar
     */
	public static boolean addProduct(String name, String type, Date expDate, int quantity, String supplier, double price) {
		LocalDate date = expDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (!validateInput(name) || expDate == null) {
            return false;
        }
        Product p = new Product(name, type, date, quantity, supplier, price);
        return ProductData.addProduct(p);
	}
	
	/**
     * Cauta un produs dupa ID sau nume.
     * Daca textul contine doar cifre, se cauta dupa ID, altfel se cauta dupa nume.
     * @param textField textul introdus pentru cautare (ID sau nume)
     * @return obiectul Product gasit
     * @throws IllegalArgumentException daca campul de cautare este gol sau null
     */
	public static Product searchProduct(String textField) {
		if (textField == null || textField.trim().isEmpty()) {
            throw new IllegalArgumentException("Search field cannot be empty");
        }
		textField = textField.trim();
	    Product p;
	    if (textField.matches("\\d+")) {
	        int id = Integer.parseInt(textField);
	        p = ProductData.searchProduct(id);
	    } else {
	        p = ProductData.searchProductbyName(textField);
	    }
	    return p;
	}
	
	/**
     * Actualizeaza un produs existent.
     * @param id ID-ul produsului
     * @param name numele produsului
     * @param type tipul produsului
     * @param expDate data expirarii
     * @param quantity cantitatea disponibila
     * @param supplier numele furnizorului
     * @param price pretul produsului
     * @return true daca actualizarea a fost realizata cu succes, false in caz contrar
     */
	public static boolean updateProduct(int id, String name, String type, Date expDate, int quantity, String supplier, double price) {
		LocalDate date = expDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (!validateInput(name) || expDate == null) {
            return false;
        }
        Product p = new Product(id, name, type, date, quantity, supplier, price);
        return ProductData.updateProduct(p);
	}
	
	/**
     * Sterge un produs din baza de date.
     * @param id ID-ul produsului care va fi sters
     * @return true daca stergerea a fost realizata cu succes, false in caz contrar
     */
	public static boolean deleteProduct(int id) {
		return ProductData.deleteProduct(id);
	}
}

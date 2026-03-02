package ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;

import data.InvoiceData;
import data.ProductData;
import model.Invoice;
import model.User;
import service.TablePopulator;

import javax.swing.JTextArea;
import java.awt.FlowLayout;

public class GenerateBillPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final User user;
	private JTable table;
	private JTextArea textReceipt;
	private Invoice currentInvoice = new Invoice();
	private Map<Integer, Double> priceList = new HashMap<>();
	private Map<Integer, String> productName = new HashMap<>();
	
	public GenerateBillPanel(User loggedUser) {
		user=loggedUser;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		setBackground(new Color(193, 240, 255));
		
		JLabel lblNewLabel_1 = new JLabel("Products");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridwidth = 2;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 1;
		gbc_table.gridy = 1;
		
		JScrollPane tableScroll = new JScrollPane(table);
		add(tableScroll, gbc_table);
		tableScroll.setPreferredSize(new Dimension(600, 200));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setHgap(50);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 2;
		add(panel_1, gbc_panel_1);
		panel_1.setBackground(new Color(193, 240, 255));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(193, 240, 255));
		panel_1.add(panel);
		
		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(spinner);
		spinner.setModel(new SpinnerNumberModel(0, 0, 1000000, 1));
				
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(btnAdd);
				
		JButton btnRemove = new JButton("Remove");
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(btnRemove);
				
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() == -1) return;
				int product_id = (int) table.getValueAt(table.getSelectedRow(),0);
				String name = (String) table.getValueAt(table.getSelectedRow(),1);
				currentInvoice.removeItem(product_id);
				priceList.remove(product_id);
				productName.put(product_id, name);
				textReceipt.setText(refreshOrderItemsTemp(currentInvoice.getProductList(), priceList, productName));  
			}
		});
				
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() == -1) return;
				int product_id = (int) table.getValueAt(table.getSelectedRow(),0);
				String name = (String) table.getValueAt(table.getSelectedRow(),1);
				int quantity = Integer.parseInt(String.valueOf(spinner.getValue()));
				Integer currentQuantity = currentInvoice.getProductList().get(product_id);
				if (currentQuantity == null) currentQuantity = 0;
				if (quantity + currentQuantity > (int) table.getValueAt(table.getSelectedRow(), 4)) {
					JOptionPane.showMessageDialog(GenerateBillPanel.this, "No more on stock!", "Warning", JOptionPane.WARNING_MESSAGE);
				    return;
				}
				BigDecimal priceBD = (BigDecimal) table.getValueAt(table.getSelectedRow(), 6);
				double price = priceBD.doubleValue();
				currentInvoice.addItem(product_id, quantity);
				priceList.put(product_id, price);
				productName.put(product_id, name);
				textReceipt.setText(refreshOrderItemsTemp(currentInvoice.getProductList(), priceList, productName));  
			}
		});
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setVgap(15);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 3;
		add(panel_2, gbc_panel_2);
		panel_2.setBackground(new Color(193, 240, 255));
		
		textReceipt = new JTextArea();
		textReceipt.setEditable(false);
		textReceipt.setFont(new Font("Monospaced", Font.PLAIN, 12));
		textReceipt.setBorder(BorderFactory.createEtchedBorder());
		
		JScrollPane scrollPane = new JScrollPane(textReceipt);
		scrollPane.setPreferredSize(new Dimension(600, 200));
		panel_2.add(scrollPane);
		
		JButton btnCheckout = new JButton("Checkout");
		btnCheckout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_btnCheckout = new GridBagConstraints();
		gbc_btnCheckout.insets = new Insets(0, 0, 0, 5);
		gbc_btnCheckout.gridx = 1;
		gbc_btnCheckout.gridy = 4;
		add(btnCheckout, gbc_btnCheckout);
		
		btnCheckout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentInvoice.getProductList().isEmpty()) {
			        JOptionPane.showMessageDialog(GenerateBillPanel.this, "No products added!", "Warning", JOptionPane.WARNING_MESSAGE);
			        return;
			    }
				currentInvoice.setUserID(user.getUserID());
				currentInvoice.setTotalPrice(InvoiceData.calculateTotalPrice(currentInvoice.getProductList()));
				if(InvoiceData.addInvoice(currentInvoice)) {
					ProductData.decreaseQuantity(currentInvoice.getProductList());
					JOptionPane.showMessageDialog(GenerateBillPanel.this, "Bill added successfully! Printing...", "Invoices Info", JOptionPane.INFORMATION_MESSAGE);
					textReceipt.setText("");
					refreshProductsTable();
				}
			}
		});
		
	}
	
	public String refreshOrderItemsTemp(Map<Integer,Integer> prodList, Map<Integer, Double> priceList, Map<Integer, String> prodName) {
		StringBuilder sb = new StringBuilder();
		double total = 0.0;
		sb.append("     ------------------------Pharmacy------------------------\n");
		sb.append("                             ").append(LocalDate.now()).append("\n");
	    sb.append("     --------------------------------------------------------\n");
	    sb.append("                    Pharmacist ID: ").append(user.getUserID()).append("\n");
	    sb.append("     --------------------------------------------------------\n");
	    for (Map.Entry<Integer, Integer> entry : prodList.entrySet()) {
	        String name = prodName.get(entry.getKey());
	        int qty = entry.getValue();
	        double price = priceList.get(entry.getKey());

	        sb.append(String.format("               %s %d x %.2f $ = %.2f\n", name, qty, price, qty*price));
	        total += price * qty;
	    }
	    sb.append("     --------------------------------------------------------\n");
	    sb.append(String.format("                          Total: %.2f $\n", total));
	    sb.append("     --------------------------------------------------------\n");
	    return sb.toString();
	}
	
	public void refreshProductsTable() {
	    try {
	    	table.setModel(TablePopulator.getTableModel("products"));
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
}

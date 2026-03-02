package ui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import data.ProductData;
import model.Product;
import service.ProductService;
import service.TablePopulator;

import java.awt.Font;
import java.awt.FlowLayout;

public class ManageStockPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableStock;
	private JTextField txtSearch;
	private JScrollPane tableScrollStock;

	public ManageStockPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelAddSearch = new JPanel();
		add(panelAddSearch, BorderLayout.NORTH);
		panelAddSearch.setPreferredSize(new Dimension(800, 150));
		GridBagLayout gbl_panelAddSearch = new GridBagLayout();
		gbl_panelAddSearch.rowWeights = new double[]{0.0, 0.0, 1.0};
		gbl_panelAddSearch.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0};
		panelAddSearch.setLayout(gbl_panelAddSearch);
		panelAddSearch.setBackground(new Color(193, 240, 255));
		
		JLabel lblNewLabel = new JLabel("Manage Stock");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 21;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panelAddSearch.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(50);
		flowLayout.setHgap(170);
		panel.setBackground(new Color(193, 240, 255));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 21;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		panelAddSearch.add(panel, gbc_panel);
		
		JButton btnAdd = new JButton("Add");
		panel.add(btnAdd);
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnRefresh = new JButton("Refresh");
		panel.add(btnRefresh);
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setBackground(new Color(193, 240, 255));
		
		txtSearch = new JTextField();
		panel_1.add(txtSearch);
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		panel_1.add(btnSearch);
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product p = ProductService.searchProduct(txtSearch.getText());
		        if (p == null)
		        	JOptionPane.showMessageDialog(ManageStockPanel.this, "Product not found!", "Stock Warning", JOptionPane.ERROR_MESSAGE);
		        else
		            refreshProductTable(p.getProductID());
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshProductsTable();
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window parentWindow = SwingUtilities.getWindowAncestor(ManageStockPanel.this);
				AddProductDialog addP = new AddProductDialog(parentWindow);
				addP.setModal(true);
				addP.pack();
				addP.setLocationRelativeTo(parentWindow);
				addP.setVisible(true);
				if (addP.added()){
					refreshProductsTable();
					JOptionPane.showMessageDialog(ManageStockPanel.this, "Product added successfully!", "Stock Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		JPanel panelEditDelete = new JPanel();
		add(panelEditDelete, BorderLayout.EAST);
		panelEditDelete.setPreferredSize(new Dimension(100, 650));
		GridBagLayout gbl_panelEditDelete = new GridBagLayout();
		gbl_panelEditDelete.rowWeights = new double[]{1.0};
		gbl_panelEditDelete.columnWeights = new double[]{1.0};
		panelEditDelete.setLayout(gbl_panelEditDelete);
		panelEditDelete.setBackground(new Color(193, 240, 255));
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setVgap(150);
		flowLayout_1.setHgap(10);
		panel_2.setBackground(new Color(193, 240, 255));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panelEditDelete.add(panel_2, gbc_panel_2);
		
		JButton btnEdit = new JButton("Edit");
		panel_2.add(btnEdit);
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnDelete = new JButton("Delete");
		panel_2.add(btnDelete);
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableStock.getSelectedRow() != -1) {
					int productId = (int) tableStock.getValueAt(tableStock.getSelectedRow(), 0);
					int confirm = JOptionPane.showConfirmDialog(ManageStockPanel.this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (confirm != JOptionPane.YES_OPTION) return;
					if(ProductService.deleteProduct(productId)) {
			        refreshProductsTable();
				    JOptionPane.showMessageDialog(ManageStockPanel.this, "Product deleted successfully!", "Stock Info", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window parentWindow = SwingUtilities.getWindowAncestor(ManageStockPanel.this);
				if (tableStock.getSelectedRow() != -1) {
					int productId = (int) tableStock.getValueAt(tableStock.getSelectedRow(), 0);
					Product p = ProductData.searchProduct(productId);
					UpdateProductDialog updateP = new UpdateProductDialog(parentWindow, p);
					updateP.setModal(true);
					updateP.pack();
					updateP.setLocationRelativeTo(parentWindow);
					updateP.setVisible(true);
					if (updateP.updated()) {
						refreshProductsTable();
						JOptionPane.showMessageDialog(ManageStockPanel.this, "Product updated successfully!", "Stock Info", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		
		tableStock = new JTable();
		tableStock.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableStock.setDefaultEditor(Object.class, null);			
		tableStock.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tableScrollStock = new JScrollPane(tableStock);
		add(tableScrollStock, BorderLayout.CENTER);
	}
	
	 public void refreshProductsTable() {
		    try {
		        tableStock.setModel(TablePopulator.getTableModel("products"));
		    } catch (SQLException e) {
		        JOptionPane.showMessageDialog(this,
		            "Failed to refresh table: " + e.getMessage(),
		            "Database Error",
		            JOptionPane.ERROR_MESSAGE);
		    }
	 }
	 
	 public void refreshProductTable(int id) {
		    try {
		        tableStock.setModel(TablePopulator.getTableModel("products", id));
		    } catch (SQLException e) {
		        JOptionPane.showMessageDialog(this,
		            "Failed to refresh table: " + e.getMessage(),
		            "Database Error",
		            JOptionPane.ERROR_MESSAGE);
		    }
		}

}

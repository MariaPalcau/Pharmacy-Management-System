package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import data.OrderData;
import model.Order;
import service.TablePopulator;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JSpinner;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;

public class AddOrderDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JSpinner spinnerQuantity;
	private JTable tableProducts;
	private JTable tableItems;
	private JScrollPane tableScrollProducts;
	private JScrollPane tableScrollItems;
	private Order currentOrder = new Order();
	private boolean added = false;
	
	public AddOrderDialog(Window owner) {
		setTitle("ADD ORDER");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel.setBackground(new Color(193, 240, 255));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Products");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Order Items");
			lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 3;
			gbc_lblNewLabel_1.gridy = 0;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			tableProducts = new JTable();
			GridBagConstraints gbc_tableProducts = new GridBagConstraints();
			gbc_tableProducts.fill = GridBagConstraints.BOTH;
			gbc_tableProducts.insets = new Insets(0, 0, 5, 5);
			gbc_tableProducts.gridx = 1;
			gbc_tableProducts.gridy = 1;
			
			tableProducts.setFont(new Font("Tahoma", Font.PLAIN, 12));
			tableProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableProducts.setDefaultEditor(Object.class, null);
			refreshProductsTable();
			
			tableScrollProducts = new JScrollPane(tableProducts);
			contentPanel.add(tableScrollProducts, gbc_tableProducts);
			
		}
		{
			tableItems = new JTable();
			GridBagConstraints gbc_tableItems = new GridBagConstraints();
			gbc_tableItems.insets = new Insets(0, 0, 5, 5);
			gbc_tableItems.fill = GridBagConstraints.BOTH;
			gbc_tableItems.gridx = 3;
			gbc_tableItems.gridy = 1;
			
			tableItems.setEnabled(false);
			
			tableScrollItems = new JScrollPane(tableItems);
			contentPanel.add(tableScrollItems, gbc_tableItems);
		}
		{
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setHgap(50);
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.insets = new Insets(0, 0, 0, 5);
				gbc_panel.fill = GridBagConstraints.BOTH;
				gbc_panel.gridx = 1;
				gbc_panel.gridy = 2;
				panel.setBackground(new Color(193, 240, 255));
				contentPanel.add(panel, gbc_panel);
				{
					spinnerQuantity = new JSpinner();
					spinnerQuantity.setFont(new Font("Tahoma", Font.PLAIN, 12));
					panel.add(spinnerQuantity);
					spinnerQuantity.setModel(new SpinnerNumberModel(0, 0, 1000000, 1));
				}
				JButton btnAdd = new JButton("Add");
				btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
				panel.add(btnAdd);
				
				btnAdd.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (tableProducts.getSelectedRow() == -1) return;
						Integer product_id = (int) tableProducts.getValueAt(tableProducts.getSelectedRow(), 0);
						currentOrder.addItem(product_id, Integer.parseInt(String.valueOf(spinnerQuantity.getValue())));
						refreshOrderItemsTempTable(currentOrder.getProductList());
					}
				});
			}
		}
		{
			JPanel buttonPane = new JPanel();
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
			fl_buttonPane.setHgap(30);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSave = new JButton("Save");
				btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
				btnSave.setActionCommand("Save");
				buttonPane.add(btnSave);
				getRootPane().setDefaultButton(btnSave);
				
				btnSave.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (currentOrder.getProductList().isEmpty()) {
					        JOptionPane.showMessageDialog(AddOrderDialog.this, "No products added!", "Warning", JOptionPane.WARNING_MESSAGE);
					        return;
					    }
						added = OrderData.addOrder(currentOrder);
						if(added) 
							dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						added = false;
						dispose();
					}
				});
			}
		}
		
	}
	
	public void refreshOrderItemsTempTable(Map<Integer,Integer> prodList) {
	    try {
	        tableItems.setModel(TablePopulator.getOrderItemsTempTableModel(prodList));
	        } catch (Exception e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Temp Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void refreshProductsTable() {
	    try {
	    	tableProducts.setModel(TablePopulator.getProductsForSelectTableModel());
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public boolean added() {
	    return added;
	}
}

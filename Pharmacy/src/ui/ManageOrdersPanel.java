package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import data.OrderData;
import data.ProductData;
import model.Order;
import model.OrderStatus;
import service.TablePopulator;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.FlowLayout;

public class ManageOrdersPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableOrders;
	private JScrollPane tableScrollOrders;
	
	public ManageOrdersPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelAddSearch = new JPanel();
		add(panelAddSearch, BorderLayout.NORTH);
		panelAddSearch.setBackground(new Color(193, 240, 255));
		panelAddSearch.setPreferredSize(new Dimension(800, 150));
		GridBagLayout gbl_panelAddSearch = new GridBagLayout();
		gbl_panelAddSearch.rowWeights = new double[]{0.0, 0.0, 1.0};
		gbl_panelAddSearch.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panelAddSearch.setLayout(gbl_panelAddSearch);
		
		JLabel lblNewLabel = new JLabel("Manage Orders");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 25;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panelAddSearch.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(160);
		flowLayout.setVgap(50);
		panel.setBackground(new Color(193, 240, 255));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 25;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		panelAddSearch.add(panel, gbc_panel);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(btnAdd);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(btnRefresh);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setBackground(new Color(193, 240, 255));
		
		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(spinner);
		spinner.setModel(new SpinnerNumberModel(0, 0, 100000000, 1));
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Order o = OrderData.searchOrder((int) spinner.getValue());
		        if (o == null)
		        	JOptionPane.showMessageDialog(ManageOrdersPanel.this, "Order not found!", "Orders Warning", JOptionPane.ERROR_MESSAGE);
		        else
		            refreshOrdersTable(o.getOrderID());
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshOrdersTable();
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window parentWindow = SwingUtilities.getWindowAncestor(ManageOrdersPanel.this);
				AddOrderDialog addO = new AddOrderDialog(parentWindow);
				addO.setModal(true);
				addO.pack();
				addO.setLocationRelativeTo(parentWindow);
				addO.setVisible(true);
				if (addO.added()){
					refreshOrdersTable();
					JOptionPane.showMessageDialog(ManageOrdersPanel.this, "Order added successfully!", "Orders Info", JOptionPane.INFORMATION_MESSAGE);
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
		flowLayout_1.setVgap(80);
		flowLayout_1.setHgap(10);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panelEditDelete.add(panel_2, gbc_panel_2);
		panel_2.setBackground(new Color(193, 240, 255));
		
		JButton btnDetails = new JButton("Details");
		btnDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnDetails);
		
		btnDetails.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window parentWindow = SwingUtilities.getWindowAncestor(ManageOrdersPanel.this);
				if (tableOrders.getSelectedRow() != -1) {
					int orderId = (int) tableOrders.getValueAt(tableOrders.getSelectedRow(), 0);
					Order o = OrderData.searchOrder(orderId);
					OrderDetailsDialog details = new OrderDetailsDialog(parentWindow, o);
					details.setModal(true);
					details.pack();
					details.setLocationRelativeTo(parentWindow);
					details.setVisible(true);
				}
			}
		});
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnEdit);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnDelete);
		
		JButton btnCompleted = new JButton("Completed");
		btnCompleted.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnCompleted);
		btnCompleted.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableOrders.getSelectedRow() != -1) {
					int orderId = (int) tableOrders.getValueAt(tableOrders.getSelectedRow(), 0);
					Order o = OrderData.searchOrder(orderId);
					o.setProductList(OrderData.getOrderWithItems(orderId));
					if(String.valueOf(o.getOrderStatus()).equals("APPROVED") ) {
						OrderData.completeOrder(orderId);
						System.out.println(o.getProductList());
						ProductData.increaseQuantity(o.getProductList());
						refreshOrdersTable();
					}
				}
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableOrders.getSelectedRow() != -1) {
					String statusStr = (String) tableOrders.getValueAt(tableOrders.getSelectedRow(), 2);
					OrderStatus status = OrderStatus.valueOf(statusStr);
					if (status == OrderStatus.APPROVED || status == OrderStatus.DENIED) return;
					int orderId = (int) tableOrders.getValueAt(tableOrders.getSelectedRow(), 0);
					int confirm = JOptionPane.showConfirmDialog(ManageOrdersPanel.this, "Are you sure you want to delete this order?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (confirm != JOptionPane.YES_OPTION) return;
					if(OrderData.deleteOrder(orderId)) {
						refreshOrdersTable();
						JOptionPane.showMessageDialog(ManageOrdersPanel.this, "Order deleted successfully!", "Orders Info", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window parentWindow = SwingUtilities.getWindowAncestor(ManageOrdersPanel.this);
				if (tableOrders.getSelectedRow() != -1) {
					String statusStr = (String) tableOrders.getValueAt(tableOrders.getSelectedRow(), 2);
					OrderStatus status = OrderStatus.valueOf(statusStr);
					if (status == OrderStatus.APPROVED || status == OrderStatus.DENIED) return;
					int orderId = (int) tableOrders.getValueAt(tableOrders.getSelectedRow(), 0);
					Order o = OrderData.searchOrder(orderId);
					UpdateOrderDialog updateO = new UpdateOrderDialog(parentWindow, o);
					updateO.setModal(true);
					updateO.pack();
					updateO.setLocationRelativeTo(parentWindow);
					updateO.setVisible(true);
					if (updateO.updated()) {
						refreshOrdersTable();
						JOptionPane.showMessageDialog(ManageOrdersPanel.this, "Order updated successfully!", "Orders Info", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		tableOrders = new JTable();
		tableOrders.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableOrders.setDefaultEditor(Object.class, null);			
		tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tableScrollOrders = new JScrollPane(tableOrders);
		add(tableScrollOrders, BorderLayout.CENTER);
	
	}

	public void refreshOrdersTable() {
	    try {
	        tableOrders.setModel(TablePopulator.getTableModel("orders"));
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void refreshOrdersTable(int id) {
	    try {
	        tableOrders.setModel(TablePopulator.getTableModel("orders", id));
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
}

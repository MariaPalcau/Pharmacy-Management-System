package ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import data.OrderData;
import service.TablePopulator;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;

public class ApproveOrderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableOrder;
	private JButton btnApprove;
	private JButton btnDeny;
	private JTable tableOrderItems;
	private JScrollPane tableScrollOrder;
	private JScrollPane tableScrollOrderItems;
	private JPanel panel;
	private JLabel lblNewLabel;

	public ApproveOrderPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		setBackground(new Color(193, 240, 255));
		
		tableOrder = new JTable();
		GridBagConstraints gbc_tableOrder = new GridBagConstraints();
		gbc_tableOrder.insets = new Insets(0, 0, 5, 5);
		gbc_tableOrder.fill = GridBagConstraints.BOTH;
		gbc_tableOrder.gridx = 1;
		gbc_tableOrder.gridy = 1;
		gbc_tableOrder.weightx = 1.0;
		gbc_tableOrder.weighty = 1.0;
		tableOrder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableOrder.setDefaultEditor(Object.class, null);
		tableOrder.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		lblNewLabel = new JLabel("Approve Orders");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 5;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		tableScrollOrder = new JScrollPane(tableOrder);
		add(tableScrollOrder, gbc_tableOrder);
		
		tableOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 1) {
					if (tableOrder.getSelectedRow() == -1) return;
					int order_id = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 0);
		            refreshOrderItemsTable(order_id);
		            }
				}
		});
		
		tableOrderItems = new JTable();
		tableOrderItems.setEnabled(false);
		GridBagConstraints gbc_tableOrderItems = new GridBagConstraints();
		gbc_tableOrderItems.insets = new Insets(0, 0, 5, 5);
		gbc_tableOrderItems.fill = GridBagConstraints.BOTH;
		gbc_tableOrderItems.gridx = 3;
		gbc_tableOrderItems.gridy = 1;
		gbc_tableOrderItems.weightx = 1.0;
		gbc_tableOrderItems.weighty = 1.0;
		tableOrderItems.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		tableScrollOrderItems = new JScrollPane(tableOrderItems);
		add(tableScrollOrderItems, gbc_tableOrderItems);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(150);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		add(panel, gbc_panel);
		
		btnApprove = new JButton("Approve");
		btnApprove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(btnApprove);
		
		btnDeny = new JButton("Deny");
		btnDeny.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(btnDeny);
		
		btnDeny.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableOrder.getSelectedRow() == -1) return;
				int order_id = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 0);
				OrderData.approveOrder(order_id, false);
				refreshOrderItemsTable(order_id);
				refreshOrdersToApproveTable();
			}
		});
		
		btnApprove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableOrder.getSelectedRow() == -1) return;
				int order_id = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 0);
				OrderData.approveOrder(order_id, true);
				refreshOrderItemsTable(order_id);
				refreshOrdersToApproveTable();
			}
		});
	}
	
	public void refreshOrdersToApproveTable() {
	    try {
	        tableOrder.setModel(TablePopulator.getTableToApproveModel());
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void refreshOrderItemsTable(int id) {
	    try {
	        tableOrderItems.setModel(TablePopulator.getOrderItemsTableToApproveModel(id));
	       } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
}
}

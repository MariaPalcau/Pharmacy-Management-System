package ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

import service.TablePopulator;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;

public class ReportsPharmacistPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollE;
	private JScrollPane scrollS;
	private JTable tableExpiring;
	private JTable tableLowStock;

	public ReportsPharmacistPanel() {
		setBackground(new Color(193, 240, 255)); 
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Reports");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		add(btnRefresh, gbc_btnNewButton);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshProductsExpTable();
				refreshProductsLowTable();
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("Expiring Products");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBackground(new Color(193, 240, 255)); 
		
		tableExpiring = new JTable();
		tableExpiring.setEnabled(false);
		tableExpiring.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollE = new JScrollPane(tableExpiring);
		panel.add(scrollE, BorderLayout.CENTER);
		
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 5;
		add(separator, gbc_separator);
		
		JLabel lblNewLabel_2 = new JLabel("Products with Low Stock");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 6;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridheight = 3;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 7;
		add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setBackground(new Color(193, 240, 255)); 
		
		tableLowStock = new JTable();
		tableLowStock.setEnabled(false);
		tableLowStock.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollS= new JScrollPane(tableLowStock);
		panel_1.add(scrollS, BorderLayout.CENTER);
	}
	
	public void refreshProductsExpTable() {
	    try {
	    	tableExpiring.setModel(TablePopulator.getTableExpiringProducts());
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void refreshProductsLowTable() {
	    try {
	    	tableLowStock.setModel(TablePopulator.getTableLowStockProducts());
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
}

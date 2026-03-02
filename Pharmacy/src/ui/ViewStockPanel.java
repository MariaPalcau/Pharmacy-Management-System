package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import service.TablePopulator;

import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.Font;

public class ViewStockPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableStock;
	private JScrollPane tableScrollStock;
	private JLabel lblNewLabel;
	
	public ViewStockPanel() {
		setBackground(new Color(193, 240, 255));
		GridBagLayout gbl_panelViewStock = new GridBagLayout();
    	gbl_panelViewStock.columnWidths = new int[]{0, 0};
    	gbl_panelViewStock.rowHeights = new int[]{0, 0, 0};
    	gbl_panelViewStock.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    	gbl_panelViewStock.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
    	setLayout(gbl_panelViewStock);
		
		lblNewLabel = new JLabel("Stock of Products");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
    	
		tableStock = new JTable();
		tableStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
    	GridBagConstraints gbc_tableStock = new GridBagConstraints();
    	gbc_tableStock.fill = GridBagConstraints.BOTH;
    	gbc_tableStock.gridx = 0;
    	gbc_tableStock.gridy = 1;
    	tableStock.setEnabled(false);
    	
    	tableScrollStock = new JScrollPane(tableStock);
    	gbc_tableStock.fill = GridBagConstraints.BOTH;
    	add(tableScrollStock, gbc_tableStock);
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
}

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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import data.InvoiceData;
import model.Invoice;
import service.TablePopulator;

import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;

public class ManageInvoicesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableInvoices;
	private JScrollPane tableScrollInvoices;
	
	public ManageInvoicesPanel() {
		setLayout(new BorderLayout(0, 0));
		setPreferredSize(new Dimension(800,600));
		
		JPanel panelAddSearch = new JPanel();
		add(panelAddSearch, BorderLayout.NORTH);
		panelAddSearch.setPreferredSize(new Dimension(800, 150));
		panelAddSearch.setBackground(new Color(193, 240, 255));
		GridBagLayout gbl_panelAddSearch = new GridBagLayout();
		gbl_panelAddSearch.rowWeights = new double[]{0.0, 0.0, 1.0};
		gbl_panelAddSearch.columnWeights = new double[]{0.0, 1.0};
		panelAddSearch.setLayout(gbl_panelAddSearch);
		
		JLabel lblNewLabel = new JLabel("Manage Invoices");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panelAddSearch.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setHgap(150);
		flowLayout_1.setVgap(50);
		panel.setBackground(new Color(193, 240, 255));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		panelAddSearch.add(panel, gbc_panel);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(btnRefresh);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setBackground(new Color(193, 240, 255));
		
		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(spinner);
		spinner.setModel(new SpinnerNumberModel(0, 0, 10000000, 1));
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Invoice i = InvoiceData.searchInvoice((int) spinner.getValue());
		        if (i == null)
		        	JOptionPane.showMessageDialog(ManageInvoicesPanel.this, "Invoice not found!", "Invoices Warning", JOptionPane.ERROR_MESSAGE);
		        else
		            refreshInvoicesTable(i.getInvoiceID());
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshInvoicesTable();
			}
		});
		
		JPanel panelDetails = new JPanel();
		add(panelDetails, BorderLayout.EAST);
		panelDetails.setPreferredSize(new Dimension(100, 650));
		GridBagLayout gbl_panelDetails = new GridBagLayout();
		gbl_panelDetails.rowWeights = new double[]{1.0};
		gbl_panelDetails.columnWeights = new double[]{1.0};
		panelDetails.setLayout(gbl_panelDetails);
		panelDetails.setBackground(new Color(193, 240, 255));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(200);
		panel_1.setBackground(new Color(193, 240, 255));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panelDetails.add(panel_1, gbc_panel_1);
		
		JButton btnDetails = new JButton("Details");
		btnDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(btnDetails);
		
		btnDetails.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window parentWindow = SwingUtilities.getWindowAncestor(ManageInvoicesPanel.this);
				if (tableInvoices.getSelectedRow() != -1) {
					int invoiceId = (int) tableInvoices.getValueAt(tableInvoices.getSelectedRow(), 0);
					Invoice i = InvoiceData.searchInvoice(invoiceId);
					InvoiceDetailsDialog details = new InvoiceDetailsDialog(parentWindow, i);
					details.setModal(true);
					details.pack();
					details.setLocationRelativeTo(parentWindow);
					details.setVisible(true);
					
				}
			}
		});
		tableInvoices = new JTable();
		tableInvoices.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableInvoices.setDefaultEditor(Object.class, null);			
		tableInvoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tableScrollInvoices = new JScrollPane(tableInvoices);
		add(tableScrollInvoices, BorderLayout.CENTER);
	}

	public void refreshInvoicesTable() {
	    try {
	        tableInvoices.setModel(TablePopulator.getTableModel("invoices"));
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void refreshInvoicesTable(int id) {
	    try {
	    	tableInvoices.setModel(TablePopulator.getTableModel("invoices", id));
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
}

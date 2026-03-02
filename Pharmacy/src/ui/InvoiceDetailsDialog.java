package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import model.Invoice;
import service.TablePopulator;

import java.awt.Font;

public class InvoiceDetailsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JScrollPane tableScroll;
	
	public InvoiceDetailsDialog(Window owner, Invoice i) {
		setTitle("INVOICE DETAILS");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel.setBackground(new Color(193, 240, 255));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			table = new JTable();
			table.setEnabled(false);
			table.setFont(new Font("Tahoma", Font.PLAIN, 12));
			
			tableScroll = new JScrollPane(table);
			contentPanel.add(tableScroll);
			refreshInvoiceItemsTable(i.getInvoiceID());
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton CloseButton = new JButton("Close");
				CloseButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
				buttonPane.add(CloseButton);
				getRootPane().setDefaultButton(CloseButton);
				CloseButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
			}
		}
	}
	
	public void refreshInvoiceItemsTable(int id) {
	    try {
	        table.setModel(TablePopulator.getInvoiceItemsTableModel(id));
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this,
	            "Failed to refresh table: " + e.getMessage(),
	            "Database Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
}

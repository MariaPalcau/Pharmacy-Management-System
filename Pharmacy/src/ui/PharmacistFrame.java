package ui;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.User;

import javax.swing.JMenuBar;
import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PharmacistFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private final User user;
	private JPanel contentPane;
	
	private ManageStockPanel msPanel;
	private ManageOrdersPanel moPanel;
	private ManageInvoicesPanel miPanel;
	private ReportsPharmacistPanel rPanel;
	private GenerateBillPanel generateBillPanel;
	
	public PharmacistFrame(User loggedUser) {
		user = loggedUser;
		setTitle("PHARMACY MANAGEMENT SYSTEM");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu manage = new JMenu("Manage");
		menuBar.add(manage);
		
		JMenuItem manageStock = new JMenuItem("Stock");
		manage.add(manageStock);
		
		JMenuItem manageOrders = new JMenuItem("Orders");
		manage.add(manageOrders);
		
		JMenuItem manageInvoices = new JMenuItem("Invoices");
		manage.add(manageInvoices);
		
		JMenu generateReceipt = new JMenu("Receipt");
		menuBar.add(generateReceipt);
		
		JMenuItem generateBill = new JMenuItem("Generate Receipt");
		generateReceipt.add(generateBill);
		
		JMenu account = new JMenu("Account");
		menuBar.add(account);
		
		JMenuItem reports = new JMenuItem("Reports");
		account.add(reports);
		
		JMenuItem logout = new JMenuItem("Logout");
		account.add(logout);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(193, 240, 255)); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		rPanel = new ReportsPharmacistPanel();
		contentPane.add(rPanel, "panelReports");
		
		msPanel = new ManageStockPanel();
		contentPane.add(msPanel, "panelStock");
		
		moPanel = new ManageOrdersPanel();
		contentPane.add(moPanel, "panelOrders");
		
		miPanel = new ManageInvoicesPanel();
		contentPane.add(miPanel, "panelInvoices");
		
		generateBillPanel = new GenerateBillPanel(user);
		contentPane.add(generateBillPanel, "panelGenerateBill");
		
		reports.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	rPanel.refreshProductsExpTable();
            	rPanel.refreshProductsLowTable();
            	showPanel("panelReports");
            }
    	});
		
		manageStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	msPanel.refreshProductsTable();
            	showPanel("panelStock");
            }
    	});
		
		manageOrders.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	msPanel.refreshProductsTable();
            	moPanel.refreshOrdersTable();
            	showPanel("panelOrders");
            }
    	});
		
		manageInvoices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	msPanel.refreshProductsTable();
            	miPanel.refreshInvoicesTable();
            	showPanel("panelInvoices");
            }
    	});
		
		generateBill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	generateBillPanel.refreshProductsTable();
            	showPanel("panelGenerateBill");
            }
    	});
		
		logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int dialogResult = JOptionPane.showConfirmDialog (PharmacistFrame.this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            	if(dialogResult == JOptionPane.YES_OPTION){
            	  dispose();
            	}
            }
    	});
		
		pack();
	    setLocationRelativeTo(null);
	}
	
	private void showPanel(String name) {
	    CardLayout card = (CardLayout) contentPane.getLayout();
	    card.show(contentPane, name);
	}
}

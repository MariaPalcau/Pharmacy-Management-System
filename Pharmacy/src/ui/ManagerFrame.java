package ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.User;

public class ManagerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ViewStockPanel viewStockPanel;
	private ApproveOrderPanel approveOrderPanel;
	private ReportsManagerPanel rPanel;

	public ManagerFrame(User loggedUser) {
		setTitle("PHARMACY MANAGEMENT SYSTEM");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu manage = new JMenu("Manage");
		menuBar.add(manage);
		
		JMenuItem viewStockItem = new JMenuItem("View Stock");
		manage.add(viewStockItem);
		
		JMenuItem approveOrderItem = new JMenuItem("Approve Order");
		manage.add(approveOrderItem);
		
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
		
		rPanel = new ReportsManagerPanel();
		contentPane.add(rPanel, "panelReports");
		
		reports.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showPanel("panelReports");
            }
    	});
		
		viewStockPanel = new ViewStockPanel();
		contentPane.add(viewStockPanel, "panelViewStock");
		
		viewStockItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	viewStockPanel.refreshProductsTable();
            	showPanel("panelViewStock");
            }
    	});
		
		approveOrderPanel = new ApproveOrderPanel();
		contentPane.add(approveOrderPanel, "panelApproveOrder");
		
		approveOrderItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	approveOrderPanel.refreshOrdersToApproveTable();
            	showPanel("panelApproveOrder");
            }
    	});
		
		logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int dialogResult = JOptionPane.showConfirmDialog (ManagerFrame.this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
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

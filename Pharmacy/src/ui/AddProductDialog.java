package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import service.ProductService;

import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import java.awt.FlowLayout;
import java.util.Date;
import java.util.Calendar;

public class AddProductDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textName;
	private JTextField textType;
	private JTextField textSupplier;
	private JSpinner spinnerQuantity;
	private JSpinner spinnerPrice;
	private JSpinner spinnerDate;
	private boolean added = false;
	
	public AddProductDialog(Window owner) {
		setTitle("ADD PRODUCT");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel.setBackground(new Color(193, 240, 255));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		contentPanel.setLayout(gbl_contentPanel);
		contentPanel.setPreferredSize(new Dimension(350, 350));
		{
			JLabel lblNewLabel = new JLabel("Name");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			textName = new JTextField();
			GridBagConstraints gbc_textName = new GridBagConstraints();
			gbc_textName.fill = GridBagConstraints.HORIZONTAL;
			gbc_textName.insets = new Insets(0, 0, 5, 0);
			gbc_textName.gridx = 3;
			gbc_textName.gridy = 0;
			contentPanel.add(textName, gbc_textName);
			textName.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Type");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 0;
			gbc_lblNewLabel_1.gridy = 2;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			textType = new JTextField();
			GridBagConstraints gbc_textType = new GridBagConstraints();
			gbc_textType.insets = new Insets(0, 0, 5, 0);
			gbc_textType.fill = GridBagConstraints.HORIZONTAL;
			gbc_textType.gridx = 3;
			gbc_textType.gridy = 2;
			contentPanel.add(textType, gbc_textType);
			textType.setColumns(10);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Expiration Date");
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_2.gridx = 0;
			gbc_lblNewLabel_2.gridy = 4;
			contentPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		}
		{
			spinnerDate = new JSpinner(new SpinnerDateModel(new Date(1767273184288L), null, null, Calendar.DAY_OF_MONTH));
			JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerDate, "yyyy-MM-dd");
			spinnerDate.setEditor(editor);
			GridBagConstraints gbc_spinnerDate = new GridBagConstraints();
			gbc_spinnerDate.insets = new Insets(0, 0, 5, 0);
			gbc_spinnerDate.fill = GridBagConstraints.HORIZONTAL;
			gbc_spinnerDate.gridx = 3;
			gbc_spinnerDate.gridy = 4;
			contentPanel.add(spinnerDate, gbc_spinnerDate);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Quantity");
			GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
			gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_3.gridx = 0;
			gbc_lblNewLabel_3.gridy = 6;
			contentPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		}
		{
			spinnerQuantity = new JSpinner();
			spinnerQuantity.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
			GridBagConstraints gbc_spinnerQuantity = new GridBagConstraints();
			gbc_spinnerQuantity.fill = GridBagConstraints.HORIZONTAL;
			gbc_spinnerQuantity.insets = new Insets(0, 0, 5, 0);
			gbc_spinnerQuantity.gridx = 3;
			gbc_spinnerQuantity.gridy = 6;
			contentPanel.add(spinnerQuantity, gbc_spinnerQuantity);
		}
		{
			JLabel lblNewLabel_4 = new JLabel("Supplier");
			GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
			gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_4.gridx = 0;
			gbc_lblNewLabel_4.gridy = 8;
			contentPanel.add(lblNewLabel_4, gbc_lblNewLabel_4);
		}
		{
			textSupplier = new JTextField();
			GridBagConstraints gbc_textSupplier = new GridBagConstraints();
			gbc_textSupplier.insets = new Insets(0, 0, 5, 0);
			gbc_textSupplier.fill = GridBagConstraints.HORIZONTAL;
			gbc_textSupplier.gridx = 3;
			gbc_textSupplier.gridy = 8;
			contentPanel.add(textSupplier, gbc_textSupplier);
			textSupplier.setColumns(10);
		}
		{
			JLabel lblNewLabel_5 = new JLabel("Price");
			GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
			gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_5.gridx = 0;
			gbc_lblNewLabel_5.gridy = 10;
			contentPanel.add(lblNewLabel_5, gbc_lblNewLabel_5);
		}
		{
			spinnerPrice = new JSpinner();
			spinnerPrice.setModel(new SpinnerNumberModel(Double.valueOf(0), Double.valueOf(0), null, Double.valueOf(1)));
			GridBagConstraints gbc_spinnerPrice = new GridBagConstraints();
			gbc_spinnerPrice.insets = new Insets(0, 0, 5, 0);
			gbc_spinnerPrice.fill = GridBagConstraints.HORIZONTAL;
			gbc_spinnerPrice.gridx = 3;
			gbc_spinnerPrice.gridy = 10;
			contentPanel.add(spinnerPrice, gbc_spinnerPrice);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
			{
				JButton btnAdd = new JButton("Add");
				buttonPane.add(btnAdd);
				getRootPane().setDefaultButton(btnAdd);
				btnAdd.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(!ProductService.validateInput(textName.getText())) {
		    				JOptionPane.showMessageDialog(AddProductDialog.this, "Invalid product name", "Stock Warning", JOptionPane.WARNING_MESSAGE);
		    				return;
						}
						if ((Date) spinnerDate.getValue() == null) {
							JOptionPane.showMessageDialog(AddProductDialog.this, "Please select an expiration date", "Stock Warning",JOptionPane.WARNING_MESSAGE);
						    return;
						}
						added = ProductService.addProduct(textName.getText(), textType.getText(), (Date) spinnerDate.getValue(), (int) spinnerQuantity.getValue(), textSupplier.getText(), (double) spinnerPrice.getValue());
	    				if (added) 
	    					dispose();
	    				else
	    					JOptionPane.showMessageDialog(AddProductDialog.this, "Product already exists!", "Stock Error", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
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

	public boolean added() {
	    return added;
	}
}

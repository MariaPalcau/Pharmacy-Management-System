package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.time.ZoneId;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import model.Product;
import service.ProductService;

import java.awt.FlowLayout;

public class UpdateProductDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textName;
	private JTextField textType;
	private JTextField textSupplier;
	private JSpinner spinnerDate;
	private JSpinner spinnerQuantity;
	private JSpinner spinnerPrice;
	private boolean updated = false;

	public UpdateProductDialog(Window owner, Product p) {
		setTitle("UPDATE PRODUCT");
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
			textName.setText(p.getProductName());
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Type\r\n");
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
			textType.setText(p.getProductType());
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
			Date initialValue = Date.from(p.getExpirationDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			spinnerDate = new JSpinner(new SpinnerDateModel(initialValue, null, null, Calendar.DAY_OF_MONTH));
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
			spinnerQuantity.setValue(p.getQuantity());
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
			textSupplier.setText(p.getSupplier());
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
			spinnerPrice.setValue(p.getPrice());
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
			{
				JButton btnUpdate = new JButton("Update");
				buttonPane.add(btnUpdate);
				getRootPane().setDefaultButton(btnUpdate);
				btnUpdate.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(!ProductService.validateInput(textName.getText())) {
		    				JOptionPane.showMessageDialog(UpdateProductDialog.this, "Invalid product name", "Stock Warning", JOptionPane.WARNING_MESSAGE);
		    				return;
						}
						updated = ProductService.updateProduct(p.getProductID(), textName.getText(), textType.getText(), (Date) spinnerDate.getValue(), (int) spinnerQuantity.getValue(), textSupplier.getText(), (double) spinnerPrice.getValue());
	    				if (updated) {
	    					dispose();
	    				} else {
	    					JOptionPane.showMessageDialog(UpdateProductDialog.this, "Could not update product", "Stock Error", JOptionPane.ERROR_MESSAGE);
	    				}
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
						updated = false;
						dispose();
					}
				});
			}
		}
	}
	public boolean updated() {
	    return updated;
	}
}

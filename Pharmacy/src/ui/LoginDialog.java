package ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.User;
import service.LoginService;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import java.awt.Dimension;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField usernameField;
	private JPasswordField passwordField;
	private User loggedUser;
	
	public User getLoggedUser() {
	    return loggedUser;
	}
	
	public LoginDialog() {
		setTitle("PHARMACY MANAGEMENT SYSTEM");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(193, 240, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel loginLabel = new JLabel("Login");
			loginLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
			GridBagConstraints gbc_loginLabel = new GridBagConstraints();
			gbc_loginLabel.insets = new Insets(0, 0, 5, 5);
			gbc_loginLabel.anchor = GridBagConstraints.BASELINE;
			gbc_loginLabel.gridx = 5;
			gbc_loginLabel.gridy = 1;
			contentPanel.add(loginLabel, gbc_loginLabel);
		}
		{
			Component rigidArea = Box.createRigidArea(new Dimension(40, 40));
			GridBagConstraints gbc_rigidArea = new GridBagConstraints();
			gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
			gbc_rigidArea.gridx = 5;
			gbc_rigidArea.gridy = 2;
			contentPanel.add(rigidArea, gbc_rigidArea);
		}
		{
			Component rigidArea = Box.createRigidArea(new Dimension(80, 80));
			GridBagConstraints gbc_rigidArea = new GridBagConstraints();
			gbc_rigidArea.gridheight = 3;
			gbc_rigidArea.gridwidth = 3;
			gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
			gbc_rigidArea.gridx = 0;
			gbc_rigidArea.gridy = 3;
			contentPanel.add(rigidArea, gbc_rigidArea);
		}
		{
			JLabel usernameLabel = new JLabel("Username");
			usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
			gbc_usernameLabel.insets = new Insets(0, 0, 5, 5);
			gbc_usernameLabel.gridx = 3;
			gbc_usernameLabel.gridy = 3;
			contentPanel.add(usernameLabel, gbc_usernameLabel);
		}
		{
			Component horizontalStrut = Box.createHorizontalStrut(5);
			GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
			gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
			gbc_horizontalStrut.gridx = 4;
			gbc_horizontalStrut.gridy = 3;
			contentPanel.add(horizontalStrut, gbc_horizontalStrut);
		}
		{
			usernameField = new JTextField();
			usernameField.setFont(new Font("Tahoma", Font.PLAIN, 13));
			GridBagConstraints gbc_usernameField = new GridBagConstraints();
			gbc_usernameField.insets = new Insets(0, 0, 5, 5);
			gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
			gbc_usernameField.gridx = 5;
			gbc_usernameField.gridy = 3;
			contentPanel.add(usernameField, gbc_usernameField);
			usernameField.setColumns(10);
		}
		{
			Component rigidArea = Box.createRigidArea(new Dimension(40, 40));
			GridBagConstraints gbc_rigidArea = new GridBagConstraints();
			gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
			gbc_rigidArea.gridx = 5;
			gbc_rigidArea.gridy = 4;
			contentPanel.add(rigidArea, gbc_rigidArea);
		}
		{
			JLabel passwordLabel = new JLabel("Password");
			passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
			gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
			gbc_passwordLabel.gridx = 3;
			gbc_passwordLabel.gridy = 5;
			contentPanel.add(passwordLabel, gbc_passwordLabel);
		}
		{
			passwordField = new JPasswordField();
			passwordField.setFont(new Font("Tahoma", Font.PLAIN, 13));
			GridBagConstraints gbc_passwordField = new GridBagConstraints();
			gbc_passwordField.insets = new Insets(0, 0, 5, 5);
			gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField.gridx = 5;
			gbc_passwordField.gridy = 5;
			contentPanel.add(passwordField, gbc_passwordField);
		}
		{
			Component rigidArea = Box.createRigidArea(new Dimension(40, 40));
			GridBagConstraints gbc_rigidArea = new GridBagConstraints();
			gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
			gbc_rigidArea.gridx = 5;
			gbc_rigidArea.gridy = 6;
			contentPanel.add(rigidArea, gbc_rigidArea);
		}
		{
			JButton loginButton = new JButton("Login");
			loginButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
			GridBagConstraints gbc_loginButton = new GridBagConstraints();
			gbc_loginButton.insets = new Insets(0, 0, 5, 5);
			gbc_loginButton.gridx = 5;
			gbc_loginButton.gridy = 7;
			contentPanel.add(loginButton, gbc_loginButton);
			getRootPane().setDefaultButton(loginButton);
			loginButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String username = usernameField.getText();
					char[] password = passwordField.getPassword();
					if(!LoginService.validateInput(username, password)) {
						JOptionPane.showMessageDialog(LoginDialog.this, "Username and password are required", "Login Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
					User user;
					try {
						user = LoginService.login(username, password);
						if (user != null) {
							loggedUser = LoginService.role(user);
						    dispose();
						}
						else
							JOptionPane.showMessageDialog(LoginDialog.this, "Invalid login", "Login Error", JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		
	}

}

package Main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;

import Item.User;

public class Login extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	JPanel topPanel, centerPanel, bottomPanel;
	JLabel lblJudul, lblUsername, lblPassword;
	JTextField txtUsername;
	JPasswordField txtPassword;
	JButton btnRegister, btnSubmit;
	Dimension size = new Dimension(150, 30);
	
	Connection connection;
	Statement statement;
	
	ArrayList<User> userList = new ArrayList<User>();
	
	public Login() {
		
		// Top Panel
		topPanel = new JPanel(new FlowLayout());
		lblJudul = new JLabel("Login");
		topPanel.add(lblJudul);
		
		// Center Panel
		centerPanel = new JPanel(new FlowLayout());
		lblUsername = new JLabel("Username");
		lblUsername.setPreferredSize(size);
		txtUsername = new JTextField();
		txtUsername.setPreferredSize(size);
		lblPassword = new JLabel("Password");
		lblPassword.setPreferredSize(size);
		txtPassword = new JPasswordField();
		txtPassword.setPreferredSize(size);
		centerPanel.add(lblUsername);
		centerPanel.add(txtUsername);
		centerPanel.add(lblPassword);
		centerPanel.add(txtPassword);
		
		// Bottom Panel
		bottomPanel = new JPanel(new FlowLayout());
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(this);
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(this);
		bottomPanel.add(btnRegister);
		bottomPanel.add(btnSubmit);
		
		// Startup
		connect();
		getUser();
		
		// JFrame
		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(400,200));
		setLocationRelativeTo(null);
		setTitle("JKLaptop"); 
		setVisible(true);
	}

	public void connect() {
		System.out.println("Connecting...");
		try {
			// Connect to database
//			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JKLaptop","root","");
			System.out.println("Connection Success !");
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection Failed !");
		}
	}
	
	public void getUser() {
		String sql = "SELECT * FROM user";
		int userRole;
		String userId, userName, userEmail, userPassword, userGender, userAddress;
		// Clear current userList
		userList.clear();
		// Get all user data
		try {
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				userId = result.getString("userId");
				userName = result.getString("userName");
				userEmail = result.getString("userEmail");
				userPassword = result.getString("userPassword");
				userGender = result.getString("userGender");
				userAddress = result.getString("userAddress");
				userRole = result.getInt("userRole");
				userList.add(new User(userRole, userId, userName, userEmail, userPassword, userGender, userAddress));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean validasiLogin() {
		if(txtUsername.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Username Field must be filled");
			return false;
		} else if (txtPassword.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Password Field must be filled");
			return false;
		} else if (validasiUser() == null) {
			JOptionPane.showMessageDialog(null, "Inputted username and password is invalid");
			return false;
		} else {
			return true;
		}
	}
	
	public void register() {
		new Register(connection, statement);
		dispose();
	}
	
	public void login() {
		if(validasiLogin()) {
			User user = (User) validasiUser();
			new MainForm(user.getUserId(), connection, statement);
			dispose();
		}
	}
	
	public User validasiUser() {
		String username = txtUsername.getText();
		@SuppressWarnings("deprecation")
		String password = txtPassword.getText();
		for(int i = 0 ; i < userList.size() ; i ++) {
			User user = (User) userList.get(i);
			if(user.getUserName().equals(username) && user.getUserPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnRegister) {
			register();
		} else if (arg0.getSource() == btnSubmit) {
			login();
		}
		
	}

}

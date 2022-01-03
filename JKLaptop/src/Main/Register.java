package Main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

public class Register extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	JPanel topPanel, centerPanel, bottomPanel, genderPanel;
	JLabel lblJudul, lblUsername, lblEmail, lblPassword, lblGender, lblAddress;
	JTextField txtUsername, txtEmail;
	JPasswordField txtPassword;
	JRadioButton rbMale, rbFemale;
	ButtonGroup genderGroup;
	JTextArea txtAddress;
	JButton btnReset, btnLogin, btnSubmit;
	
	Connection connection;
	Statement statement;
	
	ArrayList<User> userList = new ArrayList<User>();
	
	public Register(Connection connection, Statement statement) {
		
		// Top Panel
		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		lblJudul = new JLabel("JKLAPTOP");
		lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
		topPanel.add(lblJudul);
		
		// Center Panel
		centerPanel = new JPanel(new GridLayout(5, 2, 20, 20));
		lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		txtUsername = new JTextField();
		lblEmail = new JLabel("Email");
		lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
		txtEmail = new JTextField();
		lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		txtPassword = new JPasswordField();
		lblGender = new JLabel("Gender");
		lblGender.setHorizontalAlignment(SwingConstants.LEFT);
		rbMale = new JRadioButton("Male");
		rbFemale = new JRadioButton("Female");
		genderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		genderPanel.add(rbMale);
		genderPanel.add(rbFemale);
		genderGroup = new ButtonGroup();
		genderGroup.add(rbMale);
		genderGroup.add(rbFemale);
		lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.LEFT);
		txtAddress = new JTextArea();
		txtAddress.setLineWrap(true);
		centerPanel.add(lblUsername);
		centerPanel.add(txtUsername);
		centerPanel.add(lblEmail);
		centerPanel.add(txtEmail);
		centerPanel.add(lblPassword);
		centerPanel.add(txtPassword);
		centerPanel.add(lblGender);
		centerPanel.add(genderPanel);
		centerPanel.add(lblAddress);
		centerPanel.add(txtAddress);
		
		// Bottom Panel
		bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
		Dimension btnSize = new Dimension(100, 30);
		btnReset = new JButton("Reset");
		btnReset.setPreferredSize(btnSize);
		btnReset.addActionListener(this);
		btnLogin = new JButton("Login");
		btnLogin.setPreferredSize(btnSize);
		btnLogin.addActionListener(this);
		btnSubmit = new JButton("Submit");
		btnSubmit.setPreferredSize(btnSize);
		btnSubmit.addActionListener(this);
		bottomPanel.add(btnReset);
		bottomPanel.add(btnLogin);
		bottomPanel.add(btnSubmit);
		
		// Startup
		setConnection(connection);
		setStatement(statement);
		getUser();
		
		// JFrame
		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(500,600));
		setLocationRelativeTo(null);
		setTitle("JKLaptop"); 
		setVisible(true);
	}

	public void connect() {
		System.out.println("Connecting...");
		try {
			// Connect to database
//			 Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JKLaptop","root","");
			System.out.println("Connection Success !");
			statement = connection.createStatement();
		} catch (Exception e) {
			System.out.println("Connection Failed !");
		}
	}
	
	public void setConnection(Connection con) {
		if(con == null) {
			JOptionPane.showMessageDialog(null, "No Connection !");
			connection = null;
		} else {
			connection = con;
		}
	}
	
	public void setStatement(Statement stmt) {
		if(stmt == null) {
			statement = null;
		} else {
			statement = stmt;
		}
	}
	
	public void getUser() {
		// Open Connection if connection is null
		if(connection == null) {
			connect();
		}
		// Create statement if statement is null
		if(statement == null) {
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	
	public void reset() {
		txtUsername.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
		genderGroup.clearSelection();
		txtAddress.setText("");
	}
	
	public void submit() {
		if(validasiRegister()) {
			addUser();
			reset();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void addUser() {
		int userRole;
		String userId, userName, userEmail, userPassword, userGender, userAddress;
		@SuppressWarnings("unused")
		User user;
		// Get new user data
		userId = newUserId();
		userName = txtUsername.getText();
		userEmail = txtEmail.getText();
		userPassword = txtPassword.getText();
		if(rbMale.isSelected()) {
			userGender = "Male";
		} else {
			userGender = "Female";
		}
		userAddress = txtAddress.getText();
		userRole = 0;
		if (connection != null) {
			// Add new data in database
			String sql = "INSERT INTO user VALUES ('" + userId + "', '" + userName + "', '" + userEmail + "', '"
					+ userPassword + "', '" + userGender + "', '" + userAddress + "', " + userRole + ")";
			try {
				statement.executeUpdate(sql);
				JOptionPane.showMessageDialog(null, "Register Success!");
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else {
			JOptionPane.showMessageDialog(null, "Database not connected!");
		}
	}
	
	public String newUserId() {
		boolean running;
		String newUserId;
		do {
			running = false;
			newUserId = "US";
			int randomNumber = (int) (Math.random()*998+1);
			newUserId += String.format("%03d", randomNumber);
			for(int i = 0 ; i < userList.size() ; i ++) {
				User user = (User) userList.get(i);
				if(user.getUserId().equals(newUserId)) {
					running = true;
				}
			}
		} while (running);
		return newUserId;
	}
	
	@SuppressWarnings("deprecation")
	public boolean validasiRegister() {
		if(txtUsername.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Username field must fill!");
			return false;
		} else if (txtUsername.getText().length()<5 || txtUsername.getText().length()>20) {
			JOptionPane.showMessageDialog(null, "Username length must between 5 and 20 characters");
			return false;
		} else if (txtEmail.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Email field must fill!");
			return false;
		} else if (txtEmail.getText().charAt(txtEmail.getText().indexOf("@")+1) == '.') {
			JOptionPane.showMessageDialog(null, "Email character '@' must not be next to '.'");
			return false;
		} else if (txtEmail.getText().startsWith("@") || txtEmail.getText().startsWith(".")) {
			JOptionPane.showMessageDialog(null, "Email input must not starts with '@' or '.'");
			return false;
		} else if ((txtEmail.getText().length() - txtEmail.getText().replace("@", "").length()) > 1) {
			JOptionPane.showMessageDialog(null, "Email input must not contains more than 1 '@' or '.'");
			return false;
		} else if ((txtEmail.getText().length() - txtEmail.getText().replace(".", "").length()) > 1) {
			JOptionPane.showMessageDialog(null, "Email input must not contains more than 1 '@' or '.'");
			return false;
		} else if (!txtEmail.getText().endsWith(".com")) {
			JOptionPane.showMessageDialog(null, "Email input must end with '.com'");
			return false;
		} else if (txtPassword.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Password field must fill!");
			return false;
		} else if (!txtPassword.getText().matches("^[a-zA-Z0-9]+$")) {
			JOptionPane.showMessageDialog(null, "Password must alphanumeric");
			return false;
		} else if (!rbMale.isSelected() && !rbFemale.isSelected()) {
			JOptionPane.showMessageDialog(null, "One gender must be selected");
			return false;
		} else if (txtAddress.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Address field must fill!");
			return false;
		} else if (!txtAddress.getText().endsWith("Street")) {
			JOptionPane.showMessageDialog(null, "Address must ends with 'Street'");
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnReset) {
			reset();
		} else if (arg0.getSource() == btnLogin) {
			new Login();
			dispose();
		} else if (arg0.getSource() == btnSubmit) {
			submit();
		}
		
	}

}

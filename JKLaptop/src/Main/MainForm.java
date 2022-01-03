package Main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Item.Brand;
import Item.Cart;
import Item.DetailTransaction;
import Item.HeaderTransaction;
import Item.Product;
import Item.User;

public class MainForm extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	JMenuBar menuBar;
	JMenu menuManage, menuTransaction, menuLogout;
	JMenuItem menuItemManageBrand, menuItemManageProduct;
	JMenuItem menuItemBuyProduct, menuItemViewTransaction;
	JMenuItem menuItemLogout;
	JDesktopPane desktop;
	JInternalFrame frameManageBrand, frameManageProduct, frameBuyProduct, frameCart, frameViewTransaction;
	
	// Component Manage Product
	JPanel topProductPanel, botProductPanel;
	JPanel botLeftProductPanel, botCenterProductPanel, botRightProductPanel;
	JLabel lblJudulManageProduct, lblProductId, lblProductName, lblProductPrice, lblProductRating, lblProductStock, lblProductBrand;
	JTable tableProduct;
	DefaultTableModel dtmProduct;
	JScrollPane jspProduct;
	JTextField txtProductId, txtProductName, txtProductPrice;
	JSpinner txtProductRating, txtProductStock;
	JComboBox<String> cbProductBrand;
	JButton btnProductInsert, btnProductUpdate, btnProductDelete, btnProductSubmit, btnProductCancel;
	String statusProduct = "";
	Vector<String> cbProductBrandData = new Vector<>();
	Object[][] dataProduct = {{}};
	Object[] colProduct = {"ProductId","BrandName","ProductName","ProductPrice","ProductStock","ProductRating"};
	
	// Component Manage Brand
	JPanel topBrandPanel, botBrandPanel;
	JPanel botLeftBrandPanel, botCenterBrandPanel, botRightBrandPanel;
	JLabel lblJudulManageBrand, lblBrandId, lblBrandName;
	JTable tableBrand;
	DefaultTableModel dtmBrand;
	JScrollPane jspBrand;
	JTextField txtBrandId, txtBrandName;
	JButton btnBrandInsert, btnBrandUpdate, btnBrandDelete, btnBrandSubmit, btnBrandCancel;
	String statusBrand = "";
	Object[][] dataBrand = {{}};
	Object[] colBrand = {"BrandID", "BrandName"};
	
	// Component Buy Product
	JPanel topBuyProductPanel, botBuyProductPanel, btnBuyProductPanel;
	JPanel botLeftBuyProductPanel, botCenterBuyProductPanel, botRightBuyProductPanel;
	JLabel lblJudulBuyProduct, lblBuyProductId, lblBuyProductName, lblBuyProductPrice, lblBuyProductBrand, lblBuyProductQty, lblBuyProductRating;
	JLabel txtBuyProductId, txtBuyProductName, txtBuyProductPrice, txtBuyProductBrand, txtBuyProductRating;
	JButton btnAddCart;
	JSpinner txtBuyQty;
	JTable tableBuyProduct;
	DefaultTableModel dtmBuyProduct;
	JScrollPane jspBuyProduct;
	Object[][] dataBuyProduct = {{}};
	Object[] colBuyProduct = {"ProductID", "BrandName", "ProductName", "ProductPrice", "ProductQuantity", "ProductRating"};
	
	// Component Cart
	JPanel topCartPanel, botCartPanel, topCenterCartPanel;
	JLabel lblJudulCartPanel, lblJudulCartTable, lblCartUserId, lblCartDate, lblCartUsername, lblCartTotalPrice;
	JLabel txtCartUserId, txtCartDate, txtCartUsername, txtCartTotalPrice;
	JButton btnCheckOut;
	JTable tableCart;
	DefaultTableModel dtmCart;
	JScrollPane jspCart;
	Object[][] dataCart = {{}};
	Object[] colCart = {"ProductID", "ProductName", "ProductPrice", "Qty"};
	
	// Component View Transaction
	JPanel topViewTransactionPanel, botViewTransactionPanel;
	JLabel lblJudulTransactionList, lblJudulTransactionDetail;
	JTable tableTransactionList, tableTransactionDetail;
	DefaultTableModel dtmTransactionList, dtmTransactionDetail;
	JScrollPane jspTransactionList, jspTransactionDetail;
	Object[][] dataTransactionList = {{}};
	Object[] colTransactionList = {"TransactionID", "UserID", "TransactionDate"};
	Object[][] dataTransactionDetail = {{}};
	Object[] colTransactionDetail = {"TransactionID", "ProductID", "Qty"};
	
	Connection connection;
	Statement statement;
	
	User loggedUser;
	ArrayList<User> userList = new ArrayList<User>();
	ArrayList<Product> productList = new ArrayList<Product>();
	ArrayList<Brand> brandList = new ArrayList<Brand>();
	ArrayList<Cart> cartList = new ArrayList<Cart>();
	ArrayList<HeaderTransaction> headerTransactionList = new ArrayList<HeaderTransaction>();
	ArrayList<DetailTransaction> detailTransactionList = new ArrayList<DetailTransaction>();
	
	public MainForm(String userId, Connection connection, Statement statement) {
		
		// Startup
		setConnection(connection);
		setStatement(statement);
		refresh();
		setUser(userId);
		
		// MenuBar
		menuBar = new JMenuBar();
		menuManage = new JMenu("Manage");
		menuItemManageBrand = new JMenuItem("Brand");
		menuItemManageBrand.addActionListener(this);
		menuItemManageProduct = new JMenuItem("Product");
		menuItemManageProduct.addActionListener(this);
		menuManage.add(menuItemManageBrand);
		menuManage.add(menuItemManageProduct);
		menuTransaction = new JMenu("Transaction");
		menuItemBuyProduct = new JMenuItem("Buy Product");
		menuItemBuyProduct.addActionListener(this);
		menuItemViewTransaction = new JMenuItem("View Transaction");
		menuItemViewTransaction.addActionListener(this);
		menuTransaction.add(menuItemBuyProduct);
		menuTransaction.add(menuItemViewTransaction);
		menuLogout = new JMenu("Logout");
		menuItemLogout = new JMenuItem("Logout");
		menuItemLogout.addActionListener(this);
		menuLogout.add(menuItemLogout);
		menuBar.add(menuManage);
		menuBar.add(menuTransaction);
		menuBar.add(menuLogout);
		
		// Dekstop Pane & Internal Frame
		desktop = new JDesktopPane();
		frameManageProduct = new JInternalFrame("Manage Product");
		frameManageBrand = new JInternalFrame("Manage Brand");
		frameBuyProduct = new JInternalFrame("Buy Product");
		frameViewTransaction = new JInternalFrame("View Transaction");
		frameCart = new JInternalFrame("Cart");
		desktop.add(frameManageBrand);
		desktop.add(frameManageProduct);
		desktop.add(frameBuyProduct);
		desktop.add(frameCart);
		desktop.add(frameViewTransaction);
		
		// Manage Product Frame
		frameManageProduct.setVisible(false);
		frameManageProduct.setSize(1000,500);
		frameManageProduct.setMaximizable(true);
		frameManageProduct.setIconifiable(true);
		frameManageProduct.setResizable(true);
		frameManageProduct.setClosable(false);
		frameManageProduct.setLayout(new GridLayout(2, 1));
			// Top Panel
			topProductPanel = new JPanel(new BorderLayout());
			lblJudulManageProduct = new JLabel("Product List");
			lblJudulManageProduct.setFont(new Font("Arial", Font.BOLD, 20));
			lblJudulManageProduct.setHorizontalAlignment(SwingConstants.CENTER);
			dtmProduct = new DefaultTableModel(dataProduct, colProduct){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			tableProduct = new JTable(dtmProduct);
			tableProduct.getTableHeader().setReorderingAllowed(false);
			tableProduct.getTableHeader().setResizingAllowed(false);
			tableProduct.addMouseListener(manageProductMouseListener);
			tableProduct.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jspProduct = new JScrollPane();
			jspProduct.setViewportView(tableProduct);
			jspProduct.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			topProductPanel.add(lblJudulManageProduct, BorderLayout.NORTH);
			topProductPanel.add(jspProduct, BorderLayout.CENTER);
			frameManageProduct.add(topProductPanel);
			// Bot Panel
			botProductPanel = new JPanel(new GridLayout(1, 3));
			botLeftProductPanel = new JPanel(new GridLayout(6, 1));
			lblProductId = new JLabel("Product ID");
			lblProductName = new JLabel("Product Name");
			lblProductPrice = new JLabel("Product Price");
			lblProductRating = new JLabel("Product Rating");
			lblProductStock = new JLabel("Product Stock");
			lblProductBrand = new JLabel("ProduckBrand");
			botLeftProductPanel.add(lblProductId);
			botLeftProductPanel.add(lblProductName);
			botLeftProductPanel.add(lblProductPrice);
			botLeftProductPanel.add(lblProductRating);
			botLeftProductPanel.add(lblProductStock);
			botLeftProductPanel.add(lblProductBrand);
			botCenterProductPanel = new JPanel(new GridLayout(6, 1));
			txtProductId = new JTextField();
			txtProductId.setEditable(false);
			txtProductId.setEnabled(false);
			txtProductName = new JTextField();
			txtProductName.setEnabled(false);
			txtProductPrice = new JTextField();
			txtProductPrice.setEnabled(false);
			txtProductRating = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 1));
			txtProductRating.setEnabled(false);
			txtProductStock = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 1));
			txtProductStock.setEnabled(false);
			cbProductBrand = new JComboBox<String>(cbProductBrandData);
			cbProductBrand.setEnabled(false);
			botCenterProductPanel.add(txtProductId);
			botCenterProductPanel.add(txtProductName);
			botCenterProductPanel.add(txtProductPrice);
			botCenterProductPanel.add(txtProductRating);
			botCenterProductPanel.add(txtProductStock);
			botCenterProductPanel.add(cbProductBrand);
			botRightProductPanel = new JPanel(new GridLayout(5, 1));
			btnProductInsert = new JButton("Insert");
			btnProductInsert.addActionListener(manageProductActionListener);
			btnProductUpdate = new JButton("Update");
			btnProductUpdate.addActionListener(manageProductActionListener);
			btnProductDelete = new JButton("Delete");
			btnProductDelete.addActionListener(manageProductActionListener);
			btnProductSubmit = new JButton("Submit");
			btnProductSubmit.addActionListener(manageProductActionListener);
			btnProductSubmit.setEnabled(false);
			btnProductCancel = new JButton("Cancel");
			btnProductCancel.addActionListener(manageProductActionListener);
			btnProductCancel.setEnabled(false);
			botRightProductPanel.add(btnProductInsert);
			botRightProductPanel.add(btnProductUpdate);
			botRightProductPanel.add(btnProductDelete);
			botRightProductPanel.add(btnProductSubmit);
			botRightProductPanel.add(btnProductCancel);
			botProductPanel.add(botLeftProductPanel);
			botProductPanel.add(botCenterProductPanel);
			botProductPanel.add(botRightProductPanel);
			frameManageProduct.add(botProductPanel);
		
		// Manage Brand Frame
		frameManageBrand.setVisible(false);
		frameManageBrand.setSize(1000,500);
		frameManageBrand.setMaximizable(true);
		frameManageBrand.setIconifiable(true);
		frameManageBrand.setResizable(true);
		frameManageBrand.setClosable(false);
		frameManageBrand.setLayout(new GridLayout(2, 1));
			// Top Panel
			topBrandPanel = new JPanel(new BorderLayout());
			lblJudulManageBrand = new JLabel("Brand List");
			lblJudulManageBrand.setFont(new Font("Arial", Font.BOLD, 20));
			lblJudulManageBrand.setHorizontalAlignment(SwingConstants.CENTER);
			dtmBrand = new DefaultTableModel(dataBrand, colBrand){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			tableBrand = new JTable(dtmBrand);
			tableBrand.getTableHeader().setReorderingAllowed(false);
			tableBrand.getTableHeader().setResizingAllowed(false);
			tableBrand.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableBrand.addMouseListener(manageBrandMouseListener);
			jspBrand = new JScrollPane();
			jspBrand.setViewportView(tableBrand);
			jspBrand.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			topBrandPanel.add(lblJudulManageBrand, BorderLayout.NORTH);
			topBrandPanel.add(jspBrand, BorderLayout.CENTER);
			frameManageBrand.add(topBrandPanel);
			// Bot Panel
			botBrandPanel = new JPanel(new GridLayout(1, 3));
			botLeftBrandPanel = new JPanel(new GridLayout(4, 1));
			lblBrandId = new JLabel("Brand ID");
			lblBrandName = new JLabel("Brand Name");
			botLeftBrandPanel.add(lblBrandId);
			botLeftBrandPanel.add(lblBrandName);
			botCenterBrandPanel = new JPanel(new GridLayout(4, 1));
			txtBrandId = new JTextField();
			txtBrandId.setEnabled(false);
			txtBrandId.setEditable(false);
			txtBrandName = new JTextField();
			txtBrandName.setEnabled(false);
			botCenterBrandPanel.add(txtBrandId);
			botCenterBrandPanel.add(txtBrandName);
			botRightBrandPanel = new JPanel(new GridLayout(5, 1));
			btnBrandInsert = new JButton("Insert");
			btnBrandInsert.addActionListener(manageBrandActionListener);
			btnBrandUpdate = new JButton("Update");
			btnBrandUpdate.addActionListener(manageBrandActionListener);
			btnBrandDelete = new JButton("Delete");
			btnBrandDelete.addActionListener(manageBrandActionListener);
			btnBrandSubmit = new JButton("Submit");
			btnBrandSubmit.addActionListener(manageBrandActionListener);
			btnBrandSubmit.setEnabled(false);
			btnBrandCancel = new JButton("Cancel");
			btnBrandCancel.addActionListener(manageBrandActionListener);
			btnBrandCancel.setEnabled(false);
			botRightBrandPanel.add(btnBrandInsert);
			botRightBrandPanel.add(btnBrandUpdate);
			botRightBrandPanel.add(btnBrandDelete);
			botRightBrandPanel.add(btnBrandSubmit);
			botRightBrandPanel.add(btnBrandCancel);
			botBrandPanel.add(botLeftBrandPanel);
			botBrandPanel.add(botCenterBrandPanel);
			botBrandPanel.add(botRightBrandPanel);
			frameManageBrand.add(botBrandPanel);
		
		// Buy Product Frame
		frameBuyProduct.setVisible(false);
		frameBuyProduct.setSize(1000,500);
		frameBuyProduct.setMaximizable(true);
		frameBuyProduct.setIconifiable(true);
		frameBuyProduct.setResizable(true);
		frameBuyProduct.setClosable(false);
		frameBuyProduct.setLayout(new GridLayout(2, 1));
			// Top Panel
			topBuyProductPanel = new JPanel(new BorderLayout());
			lblJudulBuyProduct = new JLabel("Our Product");
			lblJudulBuyProduct.setFont(new Font("Arial", Font.BOLD, 20));
			lblJudulBuyProduct.setHorizontalAlignment(SwingConstants.CENTER);
			dtmBuyProduct = new DefaultTableModel(dataBuyProduct, colBuyProduct){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			tableBuyProduct = new JTable(dtmBuyProduct);
			tableBuyProduct.getTableHeader().setReorderingAllowed(false);
			tableBuyProduct.getTableHeader().setResizingAllowed(false);
			tableBuyProduct.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableBuyProduct.addMouseListener(buyProductMouseListener);
			jspBuyProduct = new JScrollPane();
			jspBuyProduct.setViewportView(tableBuyProduct);
			jspBuyProduct.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			topBuyProductPanel.add(lblJudulBuyProduct, BorderLayout.NORTH);
			topBuyProductPanel.add(jspBuyProduct, BorderLayout.CENTER);
			frameBuyProduct.add(topBuyProductPanel);
			// Bot Panel
			botBuyProductPanel = new JPanel(new GridLayout(1, 3));
			botLeftBuyProductPanel = new JPanel();
			botBuyProductPanel.add(botLeftBuyProductPanel);
			botCenterBuyProductPanel = new JPanel(new GridLayout(7, 1));
			lblBuyProductId = new JLabel("ProductID");
			lblBuyProductName = new JLabel("ProductName");
			lblBuyProductPrice = new JLabel("Product Price");
			lblBuyProductBrand = new JLabel("Product Brand");
			lblBuyProductQty = new JLabel("Quantity");
			lblBuyProductRating = new JLabel("Rating");
			btnBuyProductPanel = new JPanel();
			btnAddCart = new JButton("Add to Cart");
			btnAddCart.setEnabled(false);
			btnAddCart.addActionListener(buyProductActionListener);
			btnBuyProductPanel.add(btnAddCart);
			botCenterBuyProductPanel.add(lblBuyProductId);
			botCenterBuyProductPanel.add(lblBuyProductName);
			botCenterBuyProductPanel.add(lblBuyProductPrice);
			botCenterBuyProductPanel.add(lblBuyProductBrand);
			botCenterBuyProductPanel.add(lblBuyProductQty);
			botCenterBuyProductPanel.add(lblBuyProductRating);
			botCenterBuyProductPanel.add(btnBuyProductPanel);
			botBuyProductPanel.add(botCenterBuyProductPanel);
			botRightBuyProductPanel = new JPanel(new GridLayout(7, 1));
			txtBuyProductId = new JLabel("-");
			txtBuyProductName = new JLabel("-");
			txtBuyProductPrice = new JLabel("-");
			txtBuyProductBrand = new JLabel("-");
			txtBuyQty = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 1));
			txtBuyProductRating = new JLabel("-");
			botRightBuyProductPanel.add(txtBuyProductId);
			botRightBuyProductPanel.add(txtBuyProductName);
			botRightBuyProductPanel.add(txtBuyProductPrice);
			botRightBuyProductPanel.add(txtBuyProductBrand);
			botRightBuyProductPanel.add(txtBuyQty);
			botRightBuyProductPanel.add(txtBuyProductRating);
			botBuyProductPanel.add(botRightBuyProductPanel);
			frameBuyProduct.add(botBuyProductPanel);
		
		// Cart Frame
		frameCart.setVisible(false);
		frameCart.setSize(700,500);
		frameCart.setMaximizable(true);
		frameCart.setIconifiable(true);
		frameCart.setResizable(true);
		frameCart.setClosable(false);
		frameCart.setLayout(new GridLayout(2, 1));
			// Top Panel
			topCartPanel = new JPanel(new BorderLayout());
			lblJudulCartPanel = new JLabel("Cart");
			lblJudulCartPanel.setFont(new Font("Arial", Font.BOLD, 20));
			lblJudulCartPanel.setHorizontalAlignment(SwingConstants.CENTER);
			topCenterCartPanel = new JPanel(new GridLayout(2, 4));
			lblCartUserId = new JLabel("User ID :");
			lblCartDate = new JLabel("Date :");
			lblCartUsername = new JLabel("Username :");
			lblCartTotalPrice = new JLabel("Total Price :");
			txtCartUserId = new JLabel("-");
			txtCartDate = new JLabel("-");
			txtCartUsername = new JLabel("-");
			txtCartTotalPrice = new JLabel("-");
			topCenterCartPanel.add(lblCartUserId);
			topCenterCartPanel.add(txtCartUserId);
			topCenterCartPanel.add(lblCartUsername);
			topCenterCartPanel.add(txtCartUsername);
			topCenterCartPanel.add(lblCartDate);
			topCenterCartPanel.add(txtCartDate);
			topCenterCartPanel.add(lblCartTotalPrice);
			topCenterCartPanel.add(txtCartTotalPrice);
			topCartPanel.add(lblJudulCartPanel, BorderLayout.NORTH);
			topCartPanel.add(topCenterCartPanel, BorderLayout.CENTER);
			frameCart.add(topCartPanel);
			// Bot Panel
			botCartPanel = new JPanel(new BorderLayout());
			lblJudulCartTable = new JLabel("Detail");
			lblJudulCartTable.setFont(new Font("Arial", Font.BOLD, 20));
			lblJudulCartTable.setHorizontalAlignment(SwingConstants.CENTER);
			dtmCart = new DefaultTableModel(dataCart, colCart){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			tableCart = new JTable(dtmCart);
			tableCart.getTableHeader().setReorderingAllowed(false);
			tableCart.getTableHeader().setResizingAllowed(false);
			tableCart.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jspCart = new JScrollPane();
			jspCart.setViewportView(tableCart);
			jspCart.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			btnCheckOut = new JButton("Check Out");
			btnCheckOut.addActionListener(cartActionListener);
			botCartPanel.add(lblJudulCartTable, BorderLayout.NORTH);
			botCartPanel.add(jspCart, BorderLayout.CENTER);
			botCartPanel.add(btnCheckOut, BorderLayout.SOUTH);
			frameCart.add(botCartPanel);
		
		// View Transaction Frame
		frameViewTransaction.setVisible(false);
		frameViewTransaction.setSize(1000,500);
		frameViewTransaction.setMaximizable(true);
		frameViewTransaction.setIconifiable(true);
		frameViewTransaction.setResizable(true);
		frameViewTransaction.setClosable(false);
		frameViewTransaction.setLayout(new GridLayout(2, 1));
			// Top Panel
			topViewTransactionPanel = new JPanel(new BorderLayout());
			lblJudulTransactionList = new JLabel("Transaction List");
			lblJudulTransactionList.setFont(new Font("Arial", Font.BOLD, 20));
			lblJudulTransactionList.setHorizontalAlignment(SwingConstants.CENTER);
			dtmTransactionList = new DefaultTableModel(dataTransactionList, colTransactionList){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			tableTransactionList = new JTable(dtmTransactionList);
			tableTransactionList.getTableHeader().setReorderingAllowed(false);
			tableTransactionList.getTableHeader().setResizingAllowed(false);
			tableTransactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableTransactionList.addMouseListener(viewTransactionMouseListener);
			jspTransactionList = new JScrollPane();
			jspTransactionList.setViewportView(tableTransactionList);
			jspTransactionList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			topViewTransactionPanel.add(lblJudulTransactionList, BorderLayout.NORTH);
			topViewTransactionPanel.add(jspTransactionList, BorderLayout.CENTER);
			frameViewTransaction.add(topViewTransactionPanel);
			// Bot Panel
			botViewTransactionPanel = new JPanel(new BorderLayout());
			lblJudulTransactionDetail = new JLabel("Transaction Detail");
			lblJudulTransactionDetail.setFont(new Font("Arial", Font.BOLD, 20));
			lblJudulTransactionDetail.setHorizontalAlignment(SwingConstants.CENTER);
			dtmTransactionDetail = new DefaultTableModel(dataTransactionDetail, colTransactionDetail){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			tableTransactionDetail = new JTable(dtmTransactionDetail);
			tableTransactionDetail.getTableHeader().setReorderingAllowed(false);
			tableTransactionDetail.getTableHeader().setResizingAllowed(false);
			tableTransactionDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jspTransactionDetail = new JScrollPane();
			jspTransactionDetail.setViewportView(tableTransactionDetail);
			jspTransactionDetail.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			botViewTransactionPanel.add(lblJudulTransactionDetail, BorderLayout.NORTH);
			botViewTransactionPanel.add(jspTransactionDetail, BorderLayout.CENTER);
			frameViewTransaction.add(botViewTransactionPanel);
		
		// Startup Manage Product
		refreshProductTable();
		setCbProductBrand();
		// Startup Manage Brand
		refreshBrandTable();
		// Startup Buy Product
		refreshBuyProductTable();
		// Startup Cart
		refreshCartLayout();
		// Startup View Transaction
		refreshViewTransactionTable();
		
		// JFrame
		layout(menuManage, menuItemBuyProduct);
		setJMenuBar(menuBar);
		add(desktop);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(1200,800));
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
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
	
	public void getProduct() {
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
		String sql = "SELECT * FROM product";
		String productId, brandId, productName;
		int productPrice, productStock, productRating;
		// Clear current productList
		productList.clear();
		// Get all product data
		try {
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				productId = result.getString("productId");
				brandId = result.getString("brandId");
				productName = result.getString("productName");
				productPrice = result.getInt("productPrice");
				productStock = result.getInt("productStock");
				productRating = result.getInt("productRating");
				productList.add(new Product(productId, brandId, productName, productPrice, productStock, productRating));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getBrand() {
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
		String sql = "SELECT * FROM brand";
		String brandId, brandName;
		// Clear current brandList
		brandList.clear();
		// Get all brand data
		try {
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				brandId = result.getString("brandId");
				brandName = result.getString("brandName");
				brandList.add(new Brand(brandId, brandName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getCart() {
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
		String sql = "SELECT * FROM cart";
		String userId, productId;
		int qty;
		// Clear current cartList
		cartList.clear();
		// Get all cart data
		try {
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				userId = result.getString("userId");
				productId = result.getString("productId");
				qty = result.getInt("qty");
				cartList.add(new Cart(userId, productId, qty));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getHeaderTransaction() {
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
		String sql = "SELECT * FROM headertransaction";
		String transactionId, userId;
		Date transactionDate;
		Object temp;
		// Clear current headerTransactionList
		headerTransactionList.clear();
		// Get all header transaction data
		try {
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				transactionId = result.getString("transactionId");
				userId = result.getString("userId");
				temp = result.getObject("transactionDate");
				transactionDate = (Date) temp;
				headerTransactionList.add(new HeaderTransaction(transactionId, userId, transactionDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getDetailTransaction() {
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
		String sql = "SELECT * FROM detailtransaction";
		String transactionId, productId;
		int qty;
		// Clear current detailTransactionList
		detailTransactionList.clear();
		// Get all detail transaction data
		try {
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				transactionId = result.getString("transactionId");
				productId = result.getString("productId");
				qty = result.getInt("qty");
				detailTransactionList.add(new DetailTransaction(transactionId, productId, qty));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void refresh() {
		// Refresh all data
		getUser();
		getProduct();
		getBrand();
		getCart();
		getHeaderTransaction();
		getDetailTransaction();
	}
	
	public void setConnection(Connection con) {
		if(con == null) {
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

	public void setUser(String userId) {
		for(int i = 0 ; i < userList.size() ; i ++) {
			User user = (User) userList.get(i);
			if(user.getUserId().equals(userId)) {
				loggedUser = user;
				break;
			}
		}
	}

	public void layout(JMenu menuManage, JMenuItem menuItemBuyProduct) {
		if(loggedUser != null) {
			if(loggedUser.getUserRole() == 1) {
				menuManage.setVisible(true);
				menuItemBuyProduct.setVisible(false);
			} else {
				menuManage.setVisible(false);
				menuItemBuyProduct.setVisible(true);
			}
		} else {
			menuManage.setVisible(false);
			menuItemBuyProduct.setVisible(true);
		}
	}
	
	public void resetManageProductLayout() {
		btnProductInsert.setEnabled(true);
		btnProductUpdate.setEnabled(true);
		btnProductDelete.setEnabled(true);
		btnProductSubmit.setEnabled(false);
		btnProductCancel.setEnabled(false);
		txtProductId.setEnabled(false);
		txtProductId.setText("");
		txtProductName.setEnabled(false);
		txtProductName.setText("");
		txtProductPrice.setEnabled(false);
		txtProductPrice.setText("");
		txtProductRating.setEnabled(false);
		txtProductRating.setValue(0);
		txtProductStock.setEnabled(false);
		txtProductStock.setValue(0);
		cbProductBrand.setEnabled(false);
		cbProductBrand.setSelectedIndex(0);
	}
	
	public void resetManageBrandLayout() {
		btnBrandInsert.setEnabled(true);
		btnBrandUpdate.setEnabled(true);
		btnBrandDelete.setEnabled(true);
		btnBrandSubmit.setEnabled(false);
		btnBrandCancel.setEnabled(false);
		txtBrandId.setEnabled(false);
		txtBrandId.setText("");
		txtBrandName.setEnabled(false);
		txtBrandName.setText("");
	}
	
	public void resetBuyProductLayout() {
		txtBuyProductId.setText("-");
		txtBuyProductName.setText("-");
		txtBuyProductPrice.setText("-");
		txtBuyProductBrand.setText("-");
		txtBuyProductRating.setText("-");
		txtBuyQty.setValue(0);
		btnAddCart.setEnabled(false);
	}
	
	public void setCbProductBrand() {
		// get all brand name
		if (cbProductBrand.getItemCount() == 0) {
			cbProductBrand.removeAll();
		}
		cbProductBrand.addItem("<Choose Product Brand>");
		for(int i = 0 ; i < brandList.size() ; i ++) {
			Brand brand = (Brand) brandList.get(i);
			cbProductBrand.addItem(brand.getBrandName());
		}
	}
	
	public String newProductId() {
		boolean running;
		String newProductId;
		do {
			running = false;
			newProductId = "PD";
			int randomNumber = (int) (Math.random()*998+1);
			newProductId += String.format("%03d", randomNumber);
			for(int i = 0 ; i < productList.size() ; i ++) {
				Product product = (Product) productList.get(i);
				if(product.getProductId().equals(newProductId)) {
					running = true;
				}
			}
		} while (running);
		return newProductId;
	}
	
	public String newBrandId() {
		boolean running;
		String newBrandId;
		do {
			running = false;
			newBrandId = "BD";
			int randomNumber = (int) (Math.random()*998+1);
			newBrandId += String.format("%03d", randomNumber);
			for(int i = 0 ; i < brandList.size() ; i ++) {
				Brand brand = (Brand) brandList.get(i);
				if(brand.getBrandId().equals(newBrandId)) {
					running = true;
				}
			}
		} while (running);
		return newBrandId;
	}
	
	public String newTransactionId() {
		boolean running;
		String newTransactionId;
		do {
			running = false;
			newTransactionId = "TR";
			int randomNumber = (int) (Math.random()*998+1);
			newTransactionId += String.format("%03d", randomNumber);
			for(int i = 0 ; i < headerTransactionList.size() ; i ++) {
				HeaderTransaction transaction = (HeaderTransaction) headerTransactionList.get(i);
				if(transaction.getTransactionId().equals(newTransactionId)) {
					running = true;
				}
			}
		} while (running);
		return newTransactionId;
	}
	
	public boolean productInsertValidation() {
		int temp = 0;
		try {
			temp = Integer.parseInt(txtProductPrice.getText());
		} catch (NumberFormatException e) {
			temp = 0;
		}
		if(txtProductName.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Product Name must be filled");
			return false;
		} else if (txtProductPrice.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Product Price must be filled");
			return false;
		} else if (temp == 0) {
			JOptionPane.showMessageDialog(null, "Product Price must be numeric");
			return false;
		} else if ((int)txtProductRating.getValue() > 10 || (int)txtProductRating.getValue() < 1) {
			JOptionPane.showMessageDialog(null, "Product Rating must be 1 to 10");
			return false;
		} else if (cbProductBrand.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(null, "Product Brand must be choosen");
			return false;
		} else {
			return true;
		}
	}
		
	public boolean productUpdateValidation() {
		int temp = 0;
		try {
			temp = Integer.parseInt(txtProductPrice.getText());
		} catch (NumberFormatException e) {
			temp = 0;
		}
		if(!(tableProduct.getSelectedRows().length>0)) {
			JOptionPane.showMessageDialog(null, "You must select the data on the table");
			return false;
		} else if (txtProductName.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Product Name must be filled");
			return false;
		} else if (txtProductPrice.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Product Price must be filled");
			return false;
		} else if (temp == 0) {
			JOptionPane.showMessageDialog(null, "Product Price must be numeric");
			return false;
		} else if ((int)txtProductRating.getValue() > 10 || (int)txtProductRating.getValue() < 1) {
			JOptionPane.showMessageDialog(null, "Product Rating must be 1 to 10");
			return false;
		} else if (cbProductBrand.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(null, "Product Brand must be choosen");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean productDeleteValidation() {
		if(!(tableProduct.getSelectedRows().length>0) || txtProductName.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "You must select the data on the table");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean brandInsertValidation() {
		if(txtBrandName.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Brand Name must be filled");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean brandUpdateValidation() {
		if(txtBrandName.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Brand Name must be filled");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean brandDeleteValidation() {
		if(!(tableBrand.getSelectedRows().length>0) || txtBrandName.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "You must select the data on the table");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean buyProductValidation() {
		int qty = 0;
		String id = txtBuyProductId.getText();
		for(int i = 0 ; i < productList.size() ; i ++) {
			Product product = (Product) productList.get(i);
			if(product.getProductId().equals(id)) {
				qty = product.getProductStock();
				break;
			}
		}
		if((int)txtBuyQty.getValue() == 0) {
			JOptionPane.showMessageDialog(null, "Quantity minimum is 1");
			return false;
		} else if ((int)txtBuyQty.getValue() > qty) {
			JOptionPane.showMessageDialog(null, "Quantity cannot be more than available stock");
			return false;
		} else {
			return true;
		}
	}
	
	public void refreshProductTable() {
		refresh();
		// Clear product table
		dtmProduct.setRowCount(0);
		// Insert new product table data
		String productId, productName;
		String productPrice, productStock, productRating;
		String brandName = null;
		for(int i = 0 ; i < productList.size() ; i ++) {
			Product product = (Product) productList.get(i);
			productId = product.getProductId();
			productName = product.getProductName();
			productPrice = Integer.toString(product.getProductPrice());
			productStock = Integer.toString(product.getProductStock());
			productRating = Integer.toString(product.getProductRating());
			for(int j = 0 ; j < brandList.size() ; j ++) {
				Brand brand = (Brand) brandList.get(j);
				if(brand.getBrandId().equals(product.getBrandId())) {
					brandName = brand.getBrandName();
					break;
				}
			}
			dtmProduct.addRow(new Object[] {productId, brandName, productName, productPrice, productStock, productRating});
		}
	}
	
	public void refreshBrandTable() {
		refresh();
		// Clear brand table
		dtmBrand.setRowCount(0);
		// Insert new brand table data
		String brandId, brandName;
		for(int i = 0 ; i < brandList.size() ; i ++) {
			Brand brand = (Brand) brandList.get(i);
			brandId = brand.getBrandId();
			brandName = brand.getBrandName();
			dtmBrand.addRow(new Object[] {brandId, brandName});
		}
	}
	
	public void refreshBuyProductTable() {
		refresh();
		// Clear product table
		dtmBuyProduct.setRowCount(0);
		// Insert new product table data
		String productId, productName;
		String productPrice, productStock, productRating;
		String brandName = null;
		for(int i = 0 ; i < productList.size() ; i ++) {
			Product product = (Product) productList.get(i);
			productId = product.getProductId();
			productName = product.getProductName();
			productPrice = Integer.toString(product.getProductPrice());
			productStock = Integer.toString(product.getProductStock());
			productRating = Integer.toString(product.getProductRating());
			for(int j = 0 ; j < brandList.size() ; j ++) {
				Brand brand = (Brand) brandList.get(j);
				if(brand.getBrandId().equals(product.getBrandId())) {
					brandName = brand.getBrandName();
					break;
				}
			}
			dtmBuyProduct.addRow(new Object[] {productId, brandName, productName, productPrice, productStock, productRating});
		}
	}
	
	public void refreshCartLayout() {
		refresh();
		// Clear cart table & label
		txtCartUserId.setText("-");
		txtCartUsername.setText("-");
		txtCartDate.setText("-");
		txtCartTotalPrice.setText("-");
		btnCheckOut.setEnabled(false);
		dtmCart.setRowCount(0);
		// Insert cart table data and label
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime currentDate = LocalDateTime.now();
		String userId = "", productId = "", productName = "", username = "";
		int qty = 0, productPrice = 0, totalPrice = 0;
		userId = loggedUser.getUserId();
		username = loggedUser.getUserName();
		for(int i = 0 ; i < cartList.size() ; i ++) {
			Cart cart = (Cart) cartList.get(i);
			if(cart.getUserId().equals(userId)) {
				productId = cart.getProductId();
				qty = cart.getQty();
				for(int j = 0 ; j < productList.size() ; j ++) {
					Product product = (Product) productList.get(j);
					if(product.getProductId().equals(productId)) {
						productName = product.getProductName();
						productPrice = product.getProductPrice();
						totalPrice += productPrice*qty;
						break;
					}
				}
				dtmCart.addRow(new Object[] {productId, productName, productPrice, qty});
			}
		}
		txtCartUserId.setText(userId);
		txtCartUsername.setText(username);
		txtCartDate.setText(formatter.format(currentDate));
		txtCartTotalPrice.setText(Integer.toString(totalPrice));
		if(tableCart.getRowCount()>0) {
			btnCheckOut.setEnabled(true);
		}
	}
	
	public void refreshViewTransactionTable() {
		refresh();
		// Clear table data
		dtmTransactionList.setRowCount(0);
		dtmTransactionDetail.setRowCount(0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// Insert new data
		if (loggedUser.getUserRole() == 0) {
			String transactionId, loggedUserId, transactionDate;
			Date date;
			loggedUserId = loggedUser.getUserId();
			for (int i = 0; i < headerTransactionList.size(); i++) {
				HeaderTransaction header = (HeaderTransaction) headerTransactionList.get(i);
				if(header.getUserId().equals(loggedUserId)) {
					transactionId = header.getTransactionId();
					date = header.getTransactionDate();
					transactionDate = formatter.format(date);
					dtmTransactionList.addRow(new Object[] {transactionId, loggedUserId, transactionDate});
				}
			} 
		} else {
			String transactionId, userId, transactionDate;
			Date date;
			for (int i = 0; i < headerTransactionList.size(); i++) {
				HeaderTransaction header = (HeaderTransaction) headerTransactionList.get(i);
				userId = header.getUserId();
				transactionId = header.getTransactionId();
				date = header.getTransactionDate();
				transactionDate = formatter.format(date);
				dtmTransactionList.addRow(new Object[] {transactionId, userId, transactionDate});
			} 
		}
	}
	
	public void productInsert() {
		statusProduct = "insert";
		btnProductInsert.setEnabled(false);
		btnProductUpdate.setEnabled(false);
		btnProductDelete.setEnabled(false);
		btnProductSubmit.setEnabled(true);
		btnProductCancel.setEnabled(true);
		txtProductId.setEnabled(true);
		txtProductId.setText(newProductId());
		txtProductName.setEnabled(true);
		txtProductPrice.setEnabled(true);
		txtProductRating.setEnabled(true);
		txtProductStock.setEnabled(true);
		cbProductBrand.setEnabled(true);
	}
	
	public void productUpdate() {
		statusProduct = "update";
		btnProductInsert.setEnabled(false);
		btnProductUpdate.setEnabled(false);
		btnProductDelete.setEnabled(false);
		btnProductSubmit.setEnabled(true);
		btnProductCancel.setEnabled(true);
		txtProductId.setEnabled(true);
		txtProductName.setEnabled(true);
		txtProductPrice.setEnabled(true);
		txtProductRating.setEnabled(true);
		txtProductStock.setEnabled(true);
		cbProductBrand.setEnabled(true);
	}
	
	public void productDelete() {
		statusProduct = "delete";
		btnProductInsert.setEnabled(false);
		btnProductUpdate.setEnabled(false);
		btnProductDelete.setEnabled(false);
		btnProductSubmit.setEnabled(true);
		btnProductCancel.setEnabled(true);
		txtProductId.setEnabled(true);
	}
	
	public void brandInsert() {
		statusBrand = "insert";
		btnBrandInsert.setEnabled(false);
		btnBrandUpdate.setEnabled(false);
		btnBrandDelete.setEnabled(false);
		btnBrandSubmit.setEnabled(true);
		btnBrandCancel.setEnabled(true);
		txtBrandId.setEnabled(true);
		txtBrandId.setText(newBrandId());
		txtBrandName.setEnabled(true);
	}
	
	public void brandUpdate() {
		statusBrand = "update";
		btnBrandInsert.setEnabled(false);
		btnBrandUpdate.setEnabled(false);
		btnBrandDelete.setEnabled(false);
		btnBrandSubmit.setEnabled(true);
		btnBrandCancel.setEnabled(true);
		txtBrandId.setEnabled(true);
		txtBrandName.setEnabled(true);
	}
	
	public void brandDelete() {
		statusBrand = "delete";
		btnBrandInsert.setEnabled(false);
		btnBrandUpdate.setEnabled(false);
		btnBrandDelete.setEnabled(false);
		btnBrandSubmit.setEnabled(true);
		btnBrandCancel.setEnabled(true);
		txtBrandId.setEnabled(true);
	}
	
	public void productInsertSubmit() {
		if(productInsertValidation()) {
			String productId, brandId = null, productName;
			int productPrice = 0, productStock, productRating;
			// Get new product data
			productId = txtProductId.getText();
			productName = txtProductName.getText();
			try {
				productPrice = Integer.parseInt(txtProductPrice.getText());
			} catch (NumberFormatException e1) {
			}
			productStock = (int) txtProductStock.getValue();
			productRating = (int) txtProductRating.getValue();
			for(int i = 0 ; i < brandList.size() ; i ++) {
				Brand brand = (Brand) brandList.get(i);
				if(brand.getBrandName().equals((String)cbProductBrand.getSelectedItem())) {
					brandId = brand.getBrandId();
					break;
				}
				brandId = "";
			}
			if (connection != null) {
				// Add new data in database
				String sql = "INSERT INTO product VALUES ('"+productId+
						"', '"+brandId+
						"', '"+productName+
						"', "+productPrice+
						", "+productStock+
						", "+productRating+
						")";
				try {
					statement.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Insert Success");
					resetManageProductLayout();
					statusProduct = "";
					refreshProductTable();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Insert Failed");
				} 
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}
	}
	
	public void productUpdateSubmit() {
		if(productUpdateValidation()) {
			String productId, brandId = null, productName;
			int productPrice = 0, productStock, productRating;
			// Get updated product data
			productId = txtProductId.getText();
			productName = txtProductName.getText();
			try {
				productPrice = Integer.parseInt(txtProductPrice.getText());
			} catch (NumberFormatException e1) {
			}
			productStock = (int) txtProductStock.getValue();
			productRating = (int) txtProductRating.getValue();
			for(int i = 0 ; i < brandList.size() ; i ++) {
				Brand brand = (Brand) brandList.get(i);
				if(brand.getBrandName().equals((String)cbProductBrand.getSelectedItem())) {
					brandId = brand.getBrandId();
					break;
				}
				brandId = "";
			}
			if (connection != null) {
				// update the data in database
				String sql = "UPDATE product SET brandId = '"+brandId+
						"', productName = '"+productName+
						"', productPrice = "+productPrice+
						", productStock = "+productStock+
						", productRating = "+productRating+
						" WHERE productId = '"+productId+
						"'";
				try {
					statement.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Update Success");
					resetManageProductLayout();
					statusProduct = "";
					refreshProductTable();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Update Failed");
				} 
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}
	}
	
	public void productDeleteSubmit() {
		if(productDeleteValidation()) {
			String productId;
			// Get product data to be deleted
			productId = txtProductId.getText();
			if (connection != null) {
				// update the data in database
				String sql = "DELETE FROM product WHERE productId = '"+productId+"'";
				try {
					statement.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Delete Success");
					resetManageProductLayout();
					statusProduct = "";
					refreshProductTable();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Delete Failed");
				} 
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}
	}
	
	public void brandInsertSubmit() {
		if(brandInsertValidation()) {
			String brandId, brandName;
			// Get new brand data
			brandId = txtBrandId.getText();
			brandName = txtBrandName.getText();
			if (connection != null) {
				// Add new data in database
				String sql = "INSERT INTO brand VALUES ('"+brandId+"', '"+brandName+"')";
				try {
					statement.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Insert Success");
					resetManageBrandLayout();
					statusBrand = "";
					refreshBrandTable();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Insert Failed");
				} 
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}		
	}
	
	public void brandUpdateSubmit() {
		if(brandUpdateValidation()) {
			String brandId, brandName;
			// Get updated brand data
			brandId = txtBrandId.getText();
			brandName = txtBrandName.getText();
			if (connection != null) {
				// update the data in database
				String sql = "UPDATE brand SET brandName = '"+brandName+"' WHERE brandId = '"+brandId+"'";
				try {
					statement.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Update Success");
					resetManageBrandLayout();
					statusBrand = "";
					refreshBrandTable();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Update Failed");
				} 
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}
	}
	
	public void brandDeleteSubmit() {
		if(brandDeleteValidation()) {
			String brandId;
			// Get brand data to be deleted
			brandId = txtBrandId.getText();
			if (connection != null) {
				// update the data in database
				String sql = "DELETE FROM brand WHERE brandId = '"+brandId+"'";
				try {
					statement.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Delete Success");
					resetManageBrandLayout();
					statusBrand = "";
					refreshBrandTable();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Delete Failed");
				} 
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}
	}
	
	public void addCart() {
		if(buyProductValidation()) {
			String userId, productId;
			int qty, productQty, newProductStock;
			// get new cart data
			productQty = 0;
			qty = (int)txtBuyQty.getValue();
			userId = loggedUser.getUserId();
			productId = txtBuyProductId.getText();
			for(int i = 0 ; i < productList.size() ; i ++) {
				Product product = (Product) productList.get(i);
				if(product.getProductId().equals(productId)) {
					productQty = product.getProductStock();
					break;
				}
			}
			newProductStock = productQty-qty;
			if (connection != null) {
				// Add new data in database
				String sql = "INSERT INTO cart VALUES ('"+userId+"', '"+productId+"', "+qty+")";
				String sql2 = "UPDATE product SET productStock = "+newProductStock+" WHERE productId = '"+productId+"'";
				try {
					statement.executeUpdate(sql);
					statement.executeUpdate(sql2);
					resetBuyProductLayout();
					refreshBuyProductTable();
					JOptionPane.showMessageDialog(null, "Item is added to cart !");
					refreshCartLayout();
					frameCart.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Insert Failed");
				} 
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}
	}
	
	public void checkOut() {
		if(tableCart.getRowCount()>0) {
			String userId, productId, transactionId, transactionDate;
			int qty;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime currentDate = LocalDateTime.now();
			// get new transaction data
			transactionId = newTransactionId();
			userId = txtCartUserId.getText();
			transactionDate = formatter.format(currentDate);
			// add new transaction header data to database
			if (connection != null) {
				String sql = "INSERT INTO headertransaction VALUES ('"+transactionId+"', '"+userId+"', '"+transactionDate+"')";
				try {
					statement.executeUpdate(sql);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Insert Failed");
				}
				// add new transaction detail data to database
				for(int i = 0 ; i < cartList.size() ; i ++) {
					Cart cart = (Cart) cartList.get(i);
					if(cart.getUserId().equals(userId)) {
						productId = cart.getProductId();
						qty = cart.getQty();
						String sql2 = "INSERT INTO detailtransaction VALUES ('"+transactionId+"', '"+productId+"', "+qty+")";
						String sql3 = "DELETE FROM cart WHERE (productId = '"+productId+"' AND qty = "+qty+")";
						try {
							statement.executeUpdate(sql2);
							statement.executeUpdate(sql3);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				JOptionPane.showMessageDialog(null, "Check Out Success");
				refreshCartLayout();
			} else {
				JOptionPane.showMessageDialog(null, "Database not connected!");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == menuItemManageBrand) {
			refreshBrandTable();
			frameManageBrand.setVisible(true);
		} else if (arg0.getSource() == menuItemManageProduct) {
			refreshProductTable();
			setCbProductBrand();
			frameManageProduct.setVisible(true);
		} else if (arg0.getSource() == menuItemBuyProduct) {
			refreshBuyProductTable();
			frameBuyProduct.setVisible(true);
		} else if (arg0.getSource() == menuItemViewTransaction) {
			refreshViewTransactionTable();
			frameViewTransaction.setVisible(true);
		} else if (arg0.getSource() == menuItemLogout) {
			new Login();
			dispose();
		}
		
	}
	
	ActionListener manageProductActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == btnProductSubmit) {
				if(statusProduct.equals("insert")) {
					productInsertSubmit();
				} else if (statusProduct.equals("update")) {
					productUpdateSubmit();
				} else if (statusProduct.equals("delete")) {
					productDeleteSubmit();
				}
			} else if (arg0.getSource() == btnProductCancel) {
				resetManageProductLayout();
				statusProduct = "";
			} else if (arg0.getSource() == btnProductInsert) {
				productInsert();
			} else if (arg0.getSource() == btnProductUpdate) {
				productUpdate();
			} else if (arg0.getSource() == btnProductDelete) {
				productDelete();
			}
		}
	};
	
	MouseAdapter manageProductMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (statusProduct.equals("update") || statusProduct.equals("delete")) {
				if (tableProduct.getSelectedRows().length > 0) {
					if (e.getSource() == tableProduct) {
						int row = tableProduct.getSelectedRow();
						String id = (String) tableProduct.getValueAt(row, 0);
						for (int i = 0; i < productList.size(); i++) {
							Product product = (Product) productList.get(i);
							if (product.getProductId().equals(id)) {
								txtProductId.setText(product.getProductId());
								txtProductName.setText(product.getProductName());
								txtProductPrice.setText(Integer.toString(product.getProductPrice()));
								txtProductRating.setValue(product.getProductRating());
								txtProductStock.setValue(product.getProductStock());
								for (int j = 0; j < brandList.size(); j++) {
									Brand brand = (Brand) brandList.get(j);
									if (brand.getBrandId().equals(product.getBrandId())) {
										int index = brandList.indexOf(brand);
										cbProductBrand.setSelectedIndex(index+1);
										break;
									}
								}
								break;
							}
						}
					} 
				}
			}
		}
	};	
	
	ActionListener manageBrandActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == btnBrandInsert) {
				brandInsert();
			} else if (arg0.getSource() == btnBrandUpdate) {
				brandUpdate();
			} else if (arg0.getSource() == btnBrandDelete) {
				brandDelete();
			} else if (arg0.getSource() == btnBrandSubmit) {
				if(statusBrand.equals("insert")) {
					brandInsertSubmit();
				} else if (statusBrand.equals("update")) {
					brandUpdateSubmit();
				} else if (statusBrand.equals("delete")) {
					brandDeleteSubmit();
				}
			} else if (arg0.getSource() == btnBrandCancel) {
				resetManageBrandLayout();
				statusBrand = "";
			}
		}
	};
	
	MouseAdapter manageBrandMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (statusBrand.equals("update") || statusBrand.equals("delete")) {
				if (tableBrand.getSelectedRows().length > 0) {
					if (e.getSource() == tableBrand) {
						int row = tableBrand.getSelectedRow();
						String id = (String) tableBrand.getValueAt(row, 0);
						for (int i = 0; i < brandList.size(); i++) {
							Brand brand = (Brand) brandList.get(i);
							if(brand.getBrandId().equals(id)) {
								txtBrandId.setText(brand.getBrandId());
								txtBrandName.setText(brand.getBrandName());
								break;
							}
						}
					} 
				}
			}
		}
	};
	
	ActionListener buyProductActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == btnAddCart) {
				addCart();
			}
		}
	};
	
	MouseAdapter buyProductMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (tableBuyProduct.getSelectedRows().length > 0) {
				if (e.getSource() == tableBuyProduct) {
					int row = tableBuyProduct.getSelectedRow();
					String id = (String) tableBuyProduct.getValueAt(row, 0);
					for (int i = 0; i < productList.size(); i++) {
						Product product = (Product) productList.get(i);
						if(product.getProductId().equals(id)) {
							txtBuyProductId.setText(product.getProductId());
							txtBuyProductName.setText(product.getProductName());
							txtBuyProductPrice.setText(Integer.toString(product.getProductPrice()));
							txtBuyProductRating.setText(Integer.toString(product.getProductRating()));
							txtBuyQty.setValue(0);
							btnAddCart.setEnabled(true);
							for(int j = 0 ; j < brandList.size() ; j ++) {
								Brand brand = (Brand) brandList.get(j);
								if(brand.getBrandId().equals(product.getBrandId())) {
									txtBuyProductBrand.setText(brand.getBrandName());
									break;
								}
							}
							break;
						}
					} 
				}
			}
		}
	};
	
	ActionListener cartActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == btnCheckOut) {
				checkOut();
			}
		}
	};
	
	MouseAdapter viewTransactionMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (tableTransactionList.getSelectedRows().length > 0) {
				if (e.getSource() == tableTransactionList) {
					int row = tableTransactionList.getSelectedRow();
					String id = (String) tableTransactionList.getValueAt(row, 0);
					for(int i = 0 ; i < headerTransactionList.size() ; i ++) {
						HeaderTransaction header = (HeaderTransaction) headerTransactionList.get(i);
						if(header.getTransactionId().equals(id)) {
							dtmTransactionDetail.setRowCount(0);
							for(int j = 0 ; j < detailTransactionList.size() ; j ++) {
								DetailTransaction detail = (DetailTransaction) detailTransactionList.get(j);
								if(detail.getTransactionId().equals(header.getTransactionId())) {
									String transactionId = detail.getTransactionId();
									String productId = detail.getProductId();
									int qty = detail.getQty();
									dtmTransactionDetail.addRow(new Object[] {transactionId, productId, qty});
								}
							}
							break;
						}
					}
				}
			}
		}
	};
}

package Item;

public class Cart {
	
	private String userId, productId;
	private int qty;
	
	public Cart(String userId, String productId, int qty) {
		super();
		this.userId = userId;
		this.productId = productId;
		this.qty = qty;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
}

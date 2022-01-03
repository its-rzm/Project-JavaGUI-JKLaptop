package Item;

public class DetailTransaction {

	private String transactionId, productId;
	private int qty;
	
	public DetailTransaction(String transactionId, String productId, int qty) {
		super();
		this.transactionId = transactionId;
		this.productId = productId;
		this.qty = qty;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

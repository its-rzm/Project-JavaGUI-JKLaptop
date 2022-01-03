package Item;

import java.util.Date;

public class HeaderTransaction {

	private String transactionId, userId;
	private Date transactionDate;
	
	public HeaderTransaction(String transactionId, String userId, Date transactionDate) {
		super();
		this.transactionId = transactionId;
		this.userId = userId;
		this.transactionDate = transactionDate;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
}

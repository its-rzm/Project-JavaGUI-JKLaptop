package Item;

public class Product {

	private String productId, brandId, productName;
	private int productPrice, productStock, productRating;
	
	public Product(String productId, String brandId, String productName, int productPrice, int productStock,
			int productRating) {
		super();
		this.productId = productId;
		this.brandId = brandId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productStock = productStock;
		this.productRating = productRating;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	public int getProductStock() {
		return productStock;
	}
	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}
	public int getProductRating() {
		return productRating;
	}
	public void setProductRating(int productRating) {
		this.productRating = productRating;
	}
	
}

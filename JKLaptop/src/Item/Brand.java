package Item;

public class Brand {

	private String brandId, brandName;

	public Brand(String brandId, String brandName) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
	}
	
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
}

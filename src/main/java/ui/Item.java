package ui;

public class Item {
	private String name;
	private double price;
	private String description;
	private int amount;
	private String manufacturer;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getManufacturer() { return manufacturer; }
	public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }




	public Item(String name, String description, String manufacturer, double price, int amount)
	{
		this.setName(name);
		this.setDescription(description);
		this.setManufacturer(manufacturer);
		this.setPrice(price);
		this.setAmount(amount);
	}

	public String toString()
	{
		return "Name: "+getName()+" Description:  "+getDescription()+" Manufacturer:  "+getManufacturer()+" Price:  "+getPrice()+" Amount:  "+getAmount();
	}

	public int hashCode() {
		return name.hashCode();
	}
}

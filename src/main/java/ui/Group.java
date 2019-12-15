package ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Group {

	private String name;
	private String description;

	public Group(String name, String description)
	{
		this.setName(name);
		this.setDescription(description);
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getName() {
		return name;
	}

	/**
	 * set / edit name of group
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
//
//
//
//	/**
//	 * delete a item
//	 *
//	 * @param item
//	 */
//	public void deleteProduct(Item item) {
//		this.items.remove(item);
//	}
//
//	/**
//	 * get total price in group
//	 *
//	 * @return total price
//	 */
//	public double getTotalPriceInGroup() {
//		double num = 0;
//		if (this.items.size() == 0) {
//			return 0;
//		}
//		for (Item item : this.items) {
//			num += item.getPrice() * item.getAmount();
//		}
//
//		return num;
//	}
//
//	/**
//	 * save info about the group
//	 */
//	public void saveGroupInfo() {
//		BufferedWriter myWriter;
//		try {
//			myWriter = new BufferedWriter(new FileWriter(this.getName() + "GroupInfo.txt"));
//			myWriter.write("ui.Group: " + this.getName() + "\r\n");
//			myWriter.write("\r\n" + "Average price in group: " + this.getTotalPriceInGroup() + "\r\n");
//			for (Item item : this.items) {
//				myWriter.write("ui.Item: " + item.getName() + "\r\n");
//				myWriter.write("\r\n" + "Price: " + item.getPrice());
//				myWriter.write("\r\n" + "Amount: " + item.getAmount());
//				myWriter.write("\r\n" + "Producer: " + item.getManufacturer());
//				myWriter.write("\r\n" + "Describtion: " + item.getDescription() + "\r\n");
//			}
//
//			myWriter.flush();
//			myWriter.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		JOptionPane.showMessageDialog(null, "The information was saved to \"GroupInfo.txt\" ", "File saved",
//				JOptionPane.INFORMATION_MESSAGE);
//	}
}

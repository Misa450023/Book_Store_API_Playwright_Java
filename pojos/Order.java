package pojos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "bookId,customerName" })
public class Order {

	int bookId;
	String customerName;

	public Order() {
		super();
	}

	public Order(int bookId, String customerName) {
		super();
		this.bookId = bookId;
		this.customerName = customerName;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}

package pojos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "clientName,clientEmail" })
public class User {

	String clientName;
	String clientEmail;

	public User() {
		super();
	}

	public User(String clientName, String clientEmail) {
		super();
		this.clientName = clientName;
		this.clientEmail = clientEmail;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

}

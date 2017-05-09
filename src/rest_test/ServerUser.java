package rest_test;
import main.java.data.members.StickersColor;

public class ServerUser {
	private String name;
	private String phoneNumber;
	private String carNumber;
	private String email;
	private StickersColor sticker;
	
	public ServerUser() {
		this.setName("");
		this.setCarNumber("");
		this.setEmail("");
		this.setPhoneNumber("");
		this.setSticker(StickersColor.WHITE);	
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public StickersColor getSticker() {
		return sticker;
	}
	public void setSticker(StickersColor sticker) {
		this.sticker = sticker;
	}

}

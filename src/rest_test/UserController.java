package rest_test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.Exceptions.LoginException;
import main.java.data.members.StickersColor;
import main.java.logic.LoginManager;

/**
 * @author DavidCohen55
 * @author Shahar-Y Created: May 2017
 * 
 *         This file contains the java methods for the user services in the
 *         local host
 */

@Controller
public class UserController {
	ServerUser user;
	UCStatus status;
	String lastRegVal = "";

	UserController() {
		user = new ServerUser();
		status = UCStatus.NOT_REGISTERED;
	}

	void setUserData(String userName, String carNumber, String eMail, String phoneNum, StickersColor color) {
		user.setName(userName);
		user.setCarNumber(carNumber);
		user.setEmail(eMail);
		user.setPhoneNumber(phoneNum);
		user.setSticker(color);
		System.out.println("setUserData: " + userName);

	}

	// login get method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Login", produces = "application/json")
	@ResponseBody
	public ServerUser login() {
		return user != null ? user : (user = new ServerUser());
	}

	// login post method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Login", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void login(@RequestParam("name") String name, @RequestParam("pass") String pass) throws LoginException {

		if (name == null)
			return;

		if (name.equals("")) {
			System.out.println("Logging out");
			setUserData("", "", "", "", StickersColor.WHITE);
			return;
		}

		LoginManager login = new LoginManager();
		if (!login.userLogin(name, pass))
			return;

		setUserData(login.getUserName(), login.getCarNumber(), login.getEmail(), login.getPhoneNumber(),
				login.getSticker());

	}

	// register get method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Register", produces = "application/json")
	@ResponseBody
	public String register() {
		return (lastRegVal == "" ? statusToString(status) : JSONize(lastRegVal));
	}

	// register post method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Register", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String register(@RequestParam("name") String name, @RequestParam("pass") String pass,
			@RequestParam("phone") String phone, @RequestParam("car") String car, @RequestParam("email") String email,
			@RequestParam("type") int type) {

		if (name == null) {
			System.out.println("Register: name is null");
			status = UCStatus.BAD_REGISTER;
			return statusToString1(status, "");
		}

		LoginManager login = new LoginManager();
		try {
			if (!(login.userSignUp(name, pass, phone, car, email, StickersColor.values()[type])).equals("SignUpError"))
				status = UCStatus.SUCCESS;
			setUserData(login.getUserName(), login.getCarNumber(), login.getEmail(), login.getPhoneNumber(),
					login.getSticker());
			System.out.println("Succsesful signUp: " + name + " " + pass);
			return statusToString1(status, "");

		} catch (LoginException e) {
			status = UCStatus.BAD_PARAMS;
			System.out.println("status: " + status + "e.toString: " + e.toString());
			return statusToString1(status, "" + e.toString());
		}

	}

	public String JSONize(String str){
		return ("{" + '"' + "status" + '"' + ":" + '"' + str + '"' + "}");
	}
	
	public String statusToString(UCStatus ucStatus) {
		String JsonStatus = "{" + '"' + "status" + '"' + ":" + '"';
		switch (ucStatus) {
		case SUCCESS:
			JsonStatus += "Success";
			break;
		case BAD_REGISTER:
			JsonStatus += "Bad Register";
			break;
		case NOT_REGISTERED:
			JsonStatus += "Not Registered";
			break;
		case BAD_PARAMS:
			JsonStatus += "Bad Params";
			break;
		}
		JsonStatus += '"' + "}";
		// System.out.println("JsonStatus: " + JsonStatus);
		return JsonStatus;
	}

	public String statusToString1(UCStatus ucStatus, String message) {
		System.out.println("in statusToString. status: " + ucStatus + "message: " + message);
		String JsonStatus = "{" + '"' + "status" + '"' + ":" + '"';
		if (message != "") {
			JsonStatus += message;
			lastRegVal = message;
		} else {
			lastRegVal = "";
			switch (ucStatus) {
			case SUCCESS:
				JsonStatus += "Success";
				break;
			case BAD_REGISTER:
				JsonStatus += "Bad Register";
				break;
			case NOT_REGISTERED:
				JsonStatus += "Not Registered";
				break;
			case BAD_PARAMS:
				JsonStatus += "Bad Params";
				break;
			}
		}
		JsonStatus += '"' + "}";
		// System.out.println("JsonStatus: " + JsonStatus);
		return JsonStatus;
	}

	// changeDetails get method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/ChangeDetails", produces = "application/json")
	@ResponseBody
	public boolean changeDetails() {
		return false;
	}

	// changeDetails post method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/ChangeDetails", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean changeDetails(@RequestParam("name") String name, @RequestParam("phone") String phone,
			@RequestParam("newCar") String newCar, @RequestParam("email") String email,
			@RequestParam("type") String type, @RequestParam("car") String car) {
		boolean retVal = false;
		System.out.println("in UC.changeDetails. ");
		LoginManager login = new LoginManager();
		try {
			retVal = login.userUpdate(car, name, phone, email, newCar, SCStringToSC(type));
			System.out.println("in UC.changeDetails. retVal = " + retVal);
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;

	}

	// convert String to StickersColor
	private StickersColor SCStringToSC(String type) {
		switch (type) {
		case "Green":
			return StickersColor.GREEN;
		case "Blue":
			return StickersColor.BLUE;
		case "Red":
			return StickersColor.RED;
		case "Yellow":
			return StickersColor.YELLOW;
		case "Bordeaux":
			return StickersColor.BORDEAUX;
		}

		return StickersColor.WHITE;
	}

}
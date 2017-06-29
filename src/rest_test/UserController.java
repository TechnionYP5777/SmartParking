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

//import javax.servlet.http.HttpServletResponse;

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
	boolean detailsChanged;
	LoginManager login;

	UserController() {
		user = new ServerUser();
		status = UCStatus.NOT_REGISTERED;
		detailsChanged = false;
		login = new LoginManager();
	}

	/**
	 * Updates the user data according to the parameters.
	 * 
	 * @param userName
	 * @param carNumber
	 * @param eMail
	 * @param phoneNum
	 * @param color
	 */
	void setUserData(String userName, String carNumber, String eMail, String phoneNum, StickersColor color) {
		user.setName(userName);
		user.setCarNumber(carNumber);
		user.setEmail(eMail);
		user.setPhoneNumber(phoneNum);
		user.setSticker(color);
		System.out.println("setUserData: " + userName);

	}

	/**
	 * Login get method
	 * 
	 * @return the user object
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Login", produces = "application/json")
	@ResponseBody
	public ServerUser login() {
		return user != null ? user : (user = new ServerUser());
	}

	/**
	 * Login post method. logs the user into the heroku server.
	 * 
	 * @param name
	 *            : the unique user id.
	 * @param pass
	 *            : the user password.
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Login", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void login(@RequestParam("name") String name, @RequestParam("pass") String pass) {
		System.out.println("Login.POST: name:" + name + "pass:" + pass);

		if (name == null)
			return;

		if (name.equals("")) {
			System.out.println("Logging out");
			setUserData("", "", "", "", StickersColor.WHITE);
			lastRegVal = "";
			detailsChanged = false;
			return;
		}

		// LoginManager login = new LoginManager();
		if (!login.userLogin(name, pass))
			return;

		setUserData(login.getUserName(), login.getCarNumber(), login.getEmail(), login.getPhoneNumber(),
				login.getSticker());

	}

	/**
	 * Register get method
	 * 
	 * @return the register status.
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Register", produces = "application/json")
	@ResponseBody
	public String register() {
		return (lastRegVal == "" ? statusToString(status) : JSONize("status", lastRegVal));
	}

	/**
	 * Register post method Creates a new user with the parameters.
	 * 
	 * @param name
	 * @param pass
	 * @param phone
	 * @param car
	 * @param email
	 * @param type
	 * @return a JSONized string of the login status.
	 */
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

		// LoginManager login = new LoginManager();
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

	/**
	 * ChangeDetails get method
	 * 
	 * 
	 * @return a JSONized string of the change status.
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/ChangeDetails", produces = "application/json")
	@ResponseBody
	public String changeDetails() {
		System.out.println("in UC.changeDetails.GET");
		String changed = detailsChanged ? "true" : "false";
		return JSONize("changed", changed);
	}

	/**
	 * ChangeDetails post method. Receives the new information of the user,
	 * checks it and updates it on the heroku server.
	 * 
	 * @param name
	 * @param phone
	 * @param newCar
	 * @param email
	 * @param type
	 * @param oldCar
	 * @return A JSONized string of the success/error value.
	 */
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/ChangeDetails", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String changeDetails(@RequestParam("name") String name, @RequestParam("phone") String phone,
			@RequestParam("newCar") String newCar, @RequestParam("email") String email,
			@RequestParam("type") String type, @RequestParam("oldCar") String oldCar) {
		boolean retVal = false;
		// LoginManager login = new LoginManager();
		try {
			retVal = login.userUpdate(oldCar, name, phone, email, newCar, SCStringToSC(type));
			System.out.println("in UC.changeDetails.POST retVal=" + retVal);
			if (retVal) {
				setUserData(name, newCar, email, phone, SCStringToSC(type));
				System.out.println("in UC.changeDetails.POST changeDetails success!");
			} else {

				System.out.println("in UC.changeDetails.POST changeDetails failed!");
			}

		} catch (LoginException e) {
			System.out.println("in UC.changeDetails.POST Exception thrown: " + e.toString());
			e.printStackTrace();
		}
		detailsChanged = true;
		return JSONize("val", retVal ? "true" : "false");
	}

	/*
	 * @RequestMapping(value= "/User/ChangeDetails",
	 * method=RequestMethod.OPTIONS) public void corsHeaders(HttpServletResponse
	 * response) { response.addHeader("Access-Control-Allow-Origin", "*");
	 * response.addHeader("Access-Control-Allow-Methods",
	 * "GET, POST, PUT, DELETE, OPTIONS");
	 * response.addHeader("Access-Control-Allow-Headers",
	 * "origin, content-type, accept, x-requested-with");
	 * response.addHeader("Access-Control-Max-Age", "3600"); }
	 */

	/**
	 * Creates a JSON string of the parameter and its value.
	 * @param name
	 * @param value
	 * @return the JSON string.
	 */
	public String JSONize(String name, String value) {
		return ("{" + '"' + name + '"' + ":" + '"' + value + '"' + "}");
	}

	/**
	 * Converts the UCStatus to a JSON string
	 * @param ucStatus
	 * @return the JSON String.
	 */
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
		return JsonStatus;
	}

	/**
	 * Creates a JSON string of the status, or the message if there is any.
	 * @param ucStatus
	 * @param message
	 * @return The JSON string.
	 */
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

	/**
	 * Convert String to StickersColor
	 * @param type
	 * @return The string of the color.
	 */
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
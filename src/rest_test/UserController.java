package rest_test;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.Exceptions.LoginException;
import main.java.data.members.StickersColor;
import main.java.data.members.User;
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
	static Map<String, UserState> users;

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

		/* after change */
		users = new HashMap<String, UserState>();
	}

	/**
	 * Updates the user data according to the parameters.
	 * 
	 * @param userName
	 * @param carNumber
	 * @param eMail
	 * @param phoneNum
	 * @param c
	 */
	void setUserData(String userName, String carNumber, String eMail, String phoneNum, StickersColor c) {
		user.setName(userName);
		user.setCarNumber(carNumber);
		user.setEmail(eMail);
		user.setPhoneNumber(phoneNum);
		user.setSticker(c);
		System.out.println("setUserData: " + userName);
	}

	/**
	 * New Login get method
	 * 
	 * @return the user object
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/LoginDemo/{key}", produces = "application/json")
	@ResponseBody
	public ServerUser loginDemo(@PathVariable String key) {
		if (users.get(key) != null) {
			final ParseQuery<ParseObject> query = ParseQuery.getQuery("PMUser");
			try {
				ParseObject o = query.get(users.get(key).getUserId());
				return (o == null) ? new ServerUser() : new ServerUser(new User(o));
			} catch (final Exception e) {
				return null;
			}
		}
		return new ServerUser();
	}

	/**
	 * New Login post method. logs the user into the heroku server.
	 * 
	 * @param name
	 *            : the unique user id.
	 * @param pass
	 *            : the user password.
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/LoginDemo/{key}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String loginDemo(@PathVariable("key") String key, @RequestParam("name") String name,
			@RequestParam("pass") String pass) {
		if (name == null)
			return statusToString(UCStatus.BAD_PARAMS);
		if (!"".equals(name)) {
			System.out.println("Logging in " + name);
			UserState us = new UserState();
			if ((users.get(key) != null && users.get(key).getStatus() == UCStatus.SUCCESS) || !us.UserLogin(name, pass))
				return statusToString(UCStatus.ALREADY_CONNECTED);
			if (users.get(key) != null)
				users.remove(key);
			us.setStatus(UCStatus.SUCCESS);
			users.put(key, us);
			return statusToString(users.get(key).getStatus());
		}
		if (users.get(key) == null)
			return statusToString(UCStatus.NOT_CONNECTED);
		System.out.println("Logging out");
		users.remove(key);
		return statusToString(UCStatus.SUCCESS);
	}

	/**
	 * New Register get method
	 * 
	 * @return the register status.
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/RegisterDemo/{key}", produces = "application/json")
	@ResponseBody
	public String registerDemo(@PathVariable("key") String key) {
		if (users.get(key) == null)
			return "";
		JSONObject o = new JSONObject();
		o.put("status", users.get(key).getStatusString());
		o.put("error", users.get(key).getError());
		return o + "";
	}

	/**
	 * New Register post method Creates a new user with the parameters.
	 * 
	 * @param name
	 * @param pass
	 * @param phone
	 * @param car
	 * @param email
	 * @param type
	 * @return a JSONized string of the login status.
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/RegisterDemo/{key}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String registerDemo(@PathVariable("key") String key, @RequestParam("name") String name,
			@RequestParam("pass") String pass, @RequestParam("phone") String phone, @RequestParam("car") String car,
			@RequestParam("email") String email, @RequestParam("type") int type) {
		UserState us = new UserState();
		try {
			if ("SignUpError"
					.equals(us.getLogin().userSignUp(name, pass, phone, car, email, StickersColor.values()[type]))) {
				us.setStatus(UCStatus.BAD_REGISTER);
				JSONObject o = new JSONObject();
				o.put("status", us.getStatus());
				users.put(key, us);
				return o + "";
			}

			if ((users.get(key) != null && users.get(key).getStatus() == UCStatus.SUCCESS) || !us.UserLogin(car, pass))
				return statusToString(UCStatus.ALREADY_CONNECTED);
			if (users.get(key) != null)
				users.remove(key);

			us.setStatus(UCStatus.SUCCESS);
			users.put(key, us);
			System.out.println("Succsesful signUp: " + name);
			return statusToString(users.get(key).getStatus());
		} catch (LoginException e) {
			us.setStatus(UCStatus.BAD_PARAMS);
			users.put(key, us);
			System.out.println("status: " + status + "e.toString: " + e);
			JSONObject o = new JSONObject();
			o.put("status", users.get(key).getStatus());
			o.put("error", (e + ""));
			us.setError(e + "");
			users.put(key, us);
			return o + "";
		}
	}

	/**
	 * New ChangeDetails get method
	 * 
	 * 
	 * @return a JSONized string of the change status.
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/ChangeDetailsDemo/{key}", produces = "application/json")
	@ResponseBody
	public String changeDetailsDemo(@PathVariable("key") String key) {
		System.out.println("in UC.changeDetails.GET");
		JSONObject o = new JSONObject();
		if (users.get(key) != null) {
			o.put("status", users.get(key).getStatusString());
			o.put("error", users.get(key).getError());
		}
		return o + "";
	}

	/**
	 * New ChangeDetails post method. Receives the new information of the user,
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
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/ChangeDetailsDemo/{key}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String changeDetailsDemo(@PathVariable("key") String key, @RequestParam("name") String name,
			@RequestParam("phone") String phone, @RequestParam("newCar") String newCar,
			@RequestParam("email") String email, @RequestParam("type") String type,
			@RequestParam("oldCar") String oldCar) {

		JSONObject o = new JSONObject();
		// Not supposed to happen through GUI
		if (users.get(key) == null) {
			System.out.println("Error: in UC.changeDetails.POST users.get(key)=null!");
			o.put("status", "Not Connected");
			return o + "";
		}
		try {
			users.get(key).getLogin().userUpdate(oldCar, name, phone, email, newCar, SCStringToSC(type));
			System.out.println("in UC.changeDetails.POST changeDetails SUCCESS!");
			users.get(key).setStatus(UCStatus.SUCCESS);
		} catch (LoginException e1) {
			System.out.println("in UC.changeDetails.POST Exception thrown: " + e1);
			users.get(key).setError("" + e1);
			users.get(key).setStatus(UCStatus.BAD_PARAMS);
		}
		o.put("error", users.get(key).getError());
		o.put("status", users.get(key).getStatusString());
		return o + "";
	}

	/********************************************************/

	/**
	 * Login get method
	 * 
	 * @return the user object
	 */
	@CrossOrigin(origins = "*")
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
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/Login", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void login(@RequestParam("name") String name, @RequestParam("pass") String pass) {
		System.out.println("Login.POST: name:" + name + "pass:" + pass);

		if (name != null)
			if (!"".equals(name)) {
				if (login.userLogin(name, pass))
					setUserData(login.getUserName(), login.getCarNumber(), login.getEmail(), login.getPhoneNumber(),
							login.getSticker());
			} else {
				System.out.println("Logging out");
				setUserData("", "", "", "", StickersColor.WHITE);
				lastRegVal = "";
				detailsChanged = false;
			}

	}

	/**
	 * Register get method
	 * 
	 * @return the register status.
	 */
	@CrossOrigin(origins = "*")
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
	@CrossOrigin(origins = "*")
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
			if (!"SignUpError".equals(login.userSignUp(name, pass, phone, car, email, StickersColor.values()[type])))
				status = UCStatus.SUCCESS;
			setUserData(login.getUserName(), login.getCarNumber(), login.getEmail(), login.getPhoneNumber(),
					login.getSticker());
			System.out.println("Succsesful signUp: " + name + " " + pass);
			return statusToString1(status, "");

		} catch (LoginException e) {
			status = UCStatus.BAD_PARAMS;
			System.out.println("status: " + status + "e.toString: " + e);
			return statusToString1(status, e + "");
		}

	}

	/**
	 * ChangeDetails get method
	 * 
	 * 
	 * @return a JSONized string of the change status.
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/User/ChangeDetails", produces = "application/json")
	@ResponseBody
	public String changeDetails() {
		System.out.println("in UC.changeDetails.GET");
		return JSONize("changed", (detailsChanged ? "true" : "false"));
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
	@CrossOrigin(origins = "*")
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
			if (!retVal)
				System.out.println("in UC.changeDetails.POST changeDetails failed!");
			else {
				setUserData(name, newCar, email, phone, SCStringToSC(type));
				System.out.println("in UC.changeDetails.POST changeDetails success!");
			}

		} catch (LoginException e) {
			System.out.println("in UC.changeDetails.POST Exception thrown: " + e);
			e.printStackTrace();
		}
		detailsChanged = true;
		return JSONize("val", retVal ? "true" : "false");
	}

	/**
	 * Creates a JSON string of the parameter and its value.
	 * 
	 * @param name
	 * @param value
	 * @return the JSON string.
	 */
	public String JSONize(String name, String value) {
		return ("{" + '"' + name + '"' + ":" + '"' + value + '"' + "}");
	}

	/**
	 * Converts the UCStatus to a JSON string
	 * 
	 * @param s
	 * @return the JSON String.
	 */
	public String statusToString(UCStatus s) {
		JSONObject o = new JSONObject();
		String value = "";
		switch (s) {
		case SUCCESS:
			value = "Success";
			break;
		case BAD_REGISTER:
			value = "Bad Register";
			break;
		case NOT_REGISTERED:
			value = "Not Registered";
			break;
		case BAD_PARAMS:
			value = "Bad Params";
			break;
		case ALREADY_CONNECTED:
			value = "Already Connected";
			break;
		case NOT_CONNECTED:
			value = "Not Connected";
			break;
		case NOT_USED:
			value = "Not Used";
			break;
		}
		o.put("status", value);
		return o + "";
	}

	/**
	 * Creates a JSON string of the status, or the message if there is any.
	 * 
	 * @param s
	 * @param message
	 * @return The JSON string.
	 */
	public String statusToString1(UCStatus s, String message) {
		System.out.println("in statusToString. status: " + s + "message: " + message);
		String JsonStatus = "{" + '"' + "status" + '"' + ":" + '"';
		if (message != "") {
			JsonStatus += message;
			lastRegVal = message;
		} else {
			lastRegVal = "";
			switch (s) {
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
			case ALREADY_CONNECTED:
				JsonStatus += "Already Connected";
				break;
			case NOT_CONNECTED:
				JsonStatus += "Not Connected";
				break;
			case NOT_USED:
				JsonStatus += "Not Used";
				break;

			}
		}
		return JsonStatus += '"' + "}";
	}

	/**
	 * Convert String to StickersColor
	 * 
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
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
 * @author Shahar-Y
 * Created: May 2017
 * 
 * This file contains the java methods for the user services in the local host
 */

@Controller
public class UserController {
	ServerUser user;
	String signUpStatus;
	
	
	//login get method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User", produces = "application/json")
	@ResponseBody
	public ServerUser login() {
		return user != null ? user : (user = new ServerUser());
	}
	
	//login post method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void login(@RequestParam("name") String name, @RequestParam("pass") String pass) throws LoginException {

		if (name == null)
			return;
		LoginManager login = new LoginManager();
		if (!login.userLogin(name, pass))
			return;
		user.setName(login.getUserName());
		user.setCarNumber(login.getCarNumber());
		user.setEmail(login.getEmail());
		user.setPhoneNumber(login.getPhoneNumber());
		user.setSticker(login.getSticker());
	}

	//register get method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Register", produces = "application/json")
	@ResponseBody
	public String register() {
		return  signUpStatus  != null ? signUpStatus : "Please try to signUp";
	}

	//register post method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Register", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void register(@RequestParam("name") String name, @RequestParam("pass") String pass,
			@RequestParam("phone") String phone, @RequestParam("car") String car, @RequestParam("email") String email,
			@RequestParam("type") int type) {

		if (name == null)
			return;
		LoginManager login = new LoginManager();
		signUpStatus = "";
		try {
			signUpStatus = login.userSignUp(name, pass, phone, car, email, StickersColor.values()[type]);
			if (!signUpStatus.equals("SignUpError"))
				signUpStatus = "Succsesful signUp";
				System.out.println("Succsesful signUp: " + name + " " + pass);

		} catch (LoginException e) {
			signUpStatus = e.toString();
			System.out.println("Bad signUp!");
		}

		user.setName(login.getUserName());
		user.setCarNumber(login.getCarNumber());
		user.setEmail(login.getEmail());
		user.setPhoneNumber(login.getPhoneNumber());
		user.setSticker(login.getSticker());
	}
	
	//logout get method
	/*@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User", produces = "application/json")
	@ResponseBody
	public ServerUser logout() {
		return new ServerUser();
	}*/

}
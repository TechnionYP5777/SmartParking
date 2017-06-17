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
	UCStatus status;
	
	UserController(){
		user = new ServerUser();
		status = UCStatus.UNSIGNED;
	}
	
	
	void setUserData(String userName, String carNumber, String eMail, String phoneNum, StickersColor color){
		user.setName(userName);
		user.setCarNumber(carNumber);
		user.setEmail(eMail);
		user.setPhoneNumber(phoneNum);
		user.setSticker(color);
		System.out.println("setUserData: " + userName);
		
	}
	
	//login get method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Login", produces = "application/json")
	@ResponseBody
	public ServerUser login() {
		return user != null ? user : (user = new ServerUser());
	}
	
	//login post method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Login", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void login(@RequestParam("name") String name, @RequestParam("pass") String pass) throws LoginException {
		
		if (name == null)
			return;
		
		
		if(name.equals("")){
			System.out.println("Logging out");
			setUserData("","","","",StickersColor.WHITE);
			return;
		}


		LoginManager login = new LoginManager();
		if (!login.userLogin(name, pass))
			return;
		
		setUserData(login.getUserName(), login.getCarNumber(),
				login.getEmail(), login.getPhoneNumber(), login.getSticker());

	}

	
	
	//register get method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Register", produces = "application/json")
	@ResponseBody
	public String register() {
		return  statusToString(status);
	}

	//register post method
	@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping(value = "/User/Register", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String register(@RequestParam("name") String name, @RequestParam("pass") String pass,
			@RequestParam("phone") String phone, @RequestParam("car") String car, @RequestParam("email") String email,
			@RequestParam("type") int type) {

		if (name == null){
			System.out.println("Register: name is null");
			status = UCStatus.BAD_REGISTER;
			return statusToString(status);
		}
		
		LoginManager login = new LoginManager();
		try {
			if (!(login.userSignUp(name, pass, phone, car, email, StickersColor.values()[type])).equals("SignUpError"))
				status = UCStatus.SUCCESS;
				setUserData(login.getUserName(), login.getCarNumber(),
					login.getEmail(), login.getPhoneNumber(), login.getSticker());
				System.out.println("Succsesful signUp: " + name + " " + pass);
				return statusToString(status);

		} catch (LoginException e) {
			status = UCStatus.BAD_REGISTER;
			System.out.println("Bad signUp! error:" + status);
		}
		return statusToString(status);


	}
	
	public String statusToString(UCStatus ucStatus){
		
		switch(ucStatus){
		case SUCCESS:
			return "Success";
		case BAD_LOGIN:
			return "Bad Login";
		case BAD_REGISTER:
			return "Bad Register";
		case UNSIGNED:
			return "Not Signed";
		}
		
		return "Bad Status";
	}

}
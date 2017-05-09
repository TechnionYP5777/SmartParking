package rest_test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.Exceptions.LoginException;
import main.java.logic.LoginManager;

@Controller
public class UserController {
	ServerUser user;

	@RequestMapping(value = "/User", produces = "application/json")
	@ResponseBody
	public ServerUser login() {
		if (user == null)
			user = new ServerUser();
		return user;
	}

	@RequestMapping(value = "/User", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void login(@RequestParam("name") String name,@RequestParam("pass") String pass) throws LoginException {
		
		if (name != null) {
			LoginManager login = new LoginManager();
			login.userLogin(name, pass);
			user.setName(login.getUserName());
			user.setCarNumber(login.getCarNumber());
			user.setEmail(login.getEmail());
			user.setPhoneNumber(login.getPhoneNumber());
			user.setSticker(login.getSticker());
		}
	}
}

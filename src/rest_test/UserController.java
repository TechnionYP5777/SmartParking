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
		return user != null ? user : (user = new ServerUser());
	}

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
}

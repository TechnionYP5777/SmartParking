package rest_test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.Exceptions.LoginException;
import main.java.data.members.User;

@RestController
public class TestController {
	@RequestMapping("/test")
	public Test test() {

		User u;
		try {
			u = new User("3209654");
			Test t = new Test();
			t.setName(u.getName());
			t.setStr(u.getPhoneNumber());
			return t;
		} catch (LoginException e) {
			e.printStackTrace();
		}

		return null;
	}
}
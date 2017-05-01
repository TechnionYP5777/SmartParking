package rest_test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.Exceptions.LoginException;
import main.java.data.members.User;

@RestController
public class TestController {
	@RequestMapping("/shahar")
	public Test test() {
		User u;
		try {
			u = new User("3209654");
			Test t = new Test();
			t.setNum(u.getName());
			t.setStr(u.getPhoneNumber());
			return t;
		} catch (LoginException e) {
			e.printStackTrace();
		}

		return null;
	}
}
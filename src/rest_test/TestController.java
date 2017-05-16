package rest_test;

//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import main.java.Exceptions.LoginException;
import main.java.data.management.DBManager;
import main.java.data.members.Destination;
import main.java.data.members.MapLocation;
import main.java.data.members.User;
import main.java.gui.app.AbstractWindow;
import main.java.logic.NavigationController;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

@Controller
public class TestController {
	User user;

	@RequestMapping(value = "/shahar")
	// @CrossOrigin(origins = "http://localhost:8100")
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

	@RequestMapping(value = "/tmpUser", produces = "application/json")
	@ResponseBody
	public Test login() {

		Test t = new Test();

		if (user == null) {
			t.setNum("");
			t.setStr("");
		} else {
			t.setNum(user.getName());
			t.setStr(user.getPhoneNumber());
		}

		return t;
	}

	@RequestMapping(value = "/tmpUser", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void login(@RequestParam("name") String name) {
		if (name != null)
			try {
				user = new User(name);
				Test t = new Test();
				t.setNum(user.getName());
				t.setStr(user.getPhoneNumber());
			} catch (LoginException e) {
				e.printStackTrace();
			}
	}
}
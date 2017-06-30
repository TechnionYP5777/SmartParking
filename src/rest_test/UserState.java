package rest_test;

import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import main.java.data.members.User;
import main.java.logic.LoginManager;

public class UserState {
	private String userId;
	private UCStatus status;
	private String error;
	boolean detailsChanged;
	LoginManager login;

	public UserState() {
		userId = "";
		status = UCStatus.NOT_USED;
		detailsChanged = false;
		login = new LoginManager();
		error = "";
	}

	public boolean UserLogin(String name, String pass) {
		boolean res = this.login.userLogin(name, pass);
		this.userId = this.login.getUser().getObjectId();
		return res;
	}

	public User getUser() {
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("PMUser");
		try {
			ParseObject o = query.get(this.userId);
			return o == null ? null : new User(o);
		} catch (final Exception e) {
			return null;
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UCStatus getStatus() {
		return status;
	}

	public void setStatus(UCStatus s) {
		this.status = s;
	}

	public String getStatusString() {
		switch (this.status) {
		case SUCCESS:
			return "Success";
		case BAD_REGISTER:
			return "Bad Register";
		case NOT_REGISTERED:
			return "Not Registered";
		case BAD_PARAMS:
			return "Bad Params";
		case ALREADY_CONNECTED:
			return "Already Connected";
		case NOT_CONNECTED:
			return "Not Connected";
		case NOT_USED:
			return "Not Used";
		}
		return "";
	}

	public String getError() {
		return error;
	}

	public void setError(String e) {
		this.error = e;
	}

	public boolean isDetailsChanged() {
		return detailsChanged;
	}

	public void setDetailsChanged(boolean detailsChanged) {
		this.detailsChanged = detailsChanged;
	}

	public LoginManager getLogin() {
		return login;
	}

	public void setLogin(LoginManager m) {
		this.login = m;
	}
}
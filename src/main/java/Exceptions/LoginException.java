package main.java.Exceptions;

/* LoginException class - class representing exception of error in login
@author DavidCohen55
@since 2017-03-27
*/
public class LoginException extends Exception {
	private static final long serialVersionUID = 1L;
	public String exception;

	public LoginException(final String str) {
		exception = str;
	}

	@Override
	public String toString() {
		return exception;
	}
}
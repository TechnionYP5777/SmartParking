package main.java.Exceptions;

/* AlreadyExists class - class representing exception of AlreadyExists in parse object 
@author DavidCohen55
@since 2017-03-27
*/

public class AlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;

	public AlreadyExists(final String message) {
		super(message);
	}
}

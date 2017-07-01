package main.java.Exceptions;


/* NoSlotAvailable class - class representing exception of no parking slots available. 
@author DavidCohen55
@since 2017-03-27
*/

public class NoSlotAvailable extends Exception {
	private static final long serialVersionUID = 1L;

	public NoSlotAvailable(final String message) {
		super(message);
	}
}

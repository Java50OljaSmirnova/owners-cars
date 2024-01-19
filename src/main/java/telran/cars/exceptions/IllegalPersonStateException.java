package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class IllegalPersonStateException extends IllegalStateException {
	
	public IllegalPersonStateException () {
		super(ServiceExceptionMessages.PERSON_ALREADY_EXISTS);
	}
}

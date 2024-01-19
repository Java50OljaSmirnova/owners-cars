package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class IllegalCarStateException extends IllegalStateException {
	
	public IllegalCarStateException() {
		super(ServiceExceptionMessages.CAR_ALREADY_EXISTS);
	}

}

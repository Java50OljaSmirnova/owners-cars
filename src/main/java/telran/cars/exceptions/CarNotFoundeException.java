package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class CarNotFoundeException extends NotFoundException {

	public CarNotFoundeException() {
		super(ServiceExceptionMessages.CAR_NOT_FOUND);
	
	}

}

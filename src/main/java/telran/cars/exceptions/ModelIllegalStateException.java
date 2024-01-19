package telran.cars.exceptions;

@SuppressWarnings("serial")
public class ModelIllegalStateException extends IllegalStateException {
    public ModelIllegalStateException() {
		super("Model already exists");
	}
}

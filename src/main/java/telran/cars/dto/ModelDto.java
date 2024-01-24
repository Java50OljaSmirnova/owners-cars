package telran.cars.dto;

import static telran.cars.api.ValidationConstants.*;

import jakarta.validation.constraints.*;
import lombok.*;
@Getter
@AllArgsConstructor
public class ModelDto{
		@NotEmpty(message=MISSING_CAR_MODEL_MESSAGE) String modelName; 
		@NotNull(message = MISSING_MODEL_YEAR_MESSAGE) @Min(value = MIN_YEAR_VALUE, message = WRONG_MIN_YEAR_VALUE) 
		  int modelYear;
		@NotEmpty(message=MISSING_MODEL_COMPANY_MESSAGE) String company;
	      int enginePower;
		  int engineCapacity;

}

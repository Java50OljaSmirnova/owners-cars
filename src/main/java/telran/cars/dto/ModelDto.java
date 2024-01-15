package telran.cars.dto;

import static telran.cars.api.ValidationConstants.*;

import jakarta.validation.constraints.*;

public record ModelDto(
		@NotEmpty(message=MISSING_CAR_MODEL_MESSAGE) String model, 
		@NotNull(message = MISSING_MODEL_YEAR_MESSAGE) @Min(value = MIN_YEAR_VALUE, message = WRONG_MIN_YEAR_VALUE) 
		    @Max(value = MAX_YEAR_VALUE, message = WRONG_MAX_YEAR_VALUE) int year,
		@NotEmpty(message=MISSING_MODEL_COMPANY_MESSAGE) String company,
		@NotNull(message = MISSING_ENGINE_POWER_MESSAGE) @Min(value = MIN_ENGINE_POWER_VALUE, 
		message = WRONG_MIN_ENGINE_POWER_VALUE) @Max(value = MAX_ENGINE_POWER_VALUE, 
		message = WRONG_MAX_ENGINE_POWER_VALUE) int enginePower,
		@NotNull(message = MISSING_ENGINE_CAPACITY_MESSAGE) @Min(value = MIN_ENGINE_CAPACITY_VALUE, 
		message = WRONG_MIN_ENGINE_CAPACITY_VALUE) @Max(value = MAX_ENGINE_CAPACITY_VALUE, 
		message = WRONG_MAX_ENGINE_CAPACITY_VALUE) int engineCapacity) {

}

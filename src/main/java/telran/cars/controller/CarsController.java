package telran.cars.controller;

import static telran.cars.api.ValidationConstants.CAR_NUMBER_REGEXP;
import static telran.cars.api.ValidationConstants.MAX_PERSON_ID_VALUE;
import static telran.cars.api.ValidationConstants.MIN_PERSON_ID_VALUE;
import static telran.cars.api.ValidationConstants.MISSING_CAR_NUMBER_MESSAGE;
import static telran.cars.api.ValidationConstants.MISSING_PERSON_ID_MESSAGE;
import static telran.cars.api.ValidationConstants.WRONG_CAR_NUMBER_MESSAGE;
import static telran.cars.api.ValidationConstants.WRONG_MAX_PERSON_ID_VALUE;
import static telran.cars.api.ValidationConstants.WRONG_MIN_PERSON_ID_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.cars.service.CarsService;
import telran.cars.dto.*;

@RestController
@RequestMapping("cars")
@RequiredArgsConstructor
@Slf4j
public class CarsController {
	final CarsService carsService;
	@PostMapping
	CarDto addCar(@RequestBody @Valid CarDto carDto) {
		log.debug("add: car {}", carDto);
		return carsService.addCar(carDto);
	}
	@PostMapping("person")
	PersonDto addPerson(@RequestBody @Valid PersonDto personDto) {
		log.debug("add: person {}", personDto);
		return carsService.addPerson(personDto);
	}
	@PutMapping("person")
	PersonDto updatePerson(@RequestBody @Valid PersonDto personDto) {
		log.debug("update: person {}", personDto);
		return carsService.updatePerson(personDto);
	}
	@PutMapping("trade")
	TradeDealDto purchase(@RequestBody @Valid TradeDealDto tradeDealDto) {
		return carsService.purchase(tradeDealDto);
	}
	@DeleteMapping("person/{id}")
	PersonDto deletePerson(@PathVariable(name="id") @NotNull(message = MISSING_PERSON_ID_MESSAGE) @Min(value = MIN_PERSON_ID_VALUE, 
			message = WRONG_MIN_PERSON_ID_VALUE) @Max(value = MAX_PERSON_ID_VALUE, message = WRONG_MAX_PERSON_ID_VALUE) long id) {
		log.debug("delete: person with ID {}", id);
		return carsService.deletePerson(id);
	}
	@DeleteMapping("{carNumber}")
	CarDto deleteCar(@PathVariable @NotEmpty(message=MISSING_CAR_NUMBER_MESSAGE) @Pattern(regexp = CAR_NUMBER_REGEXP, 
			message = WRONG_CAR_NUMBER_MESSAGE) String carNumber) {
		log.debug("delete car: car with number {}", carNumber);
		return carsService.deleteCar(carNumber);
	}
	@GetMapping("person/{id}")
	List<CarDto> getOwnerCars(@PathVariable @NotNull(message = MISSING_PERSON_ID_MESSAGE) @Min(value = MIN_PERSON_ID_VALUE, 
			message = WRONG_MIN_PERSON_ID_VALUE) @Max(value = MAX_PERSON_ID_VALUE, message = WRONG_MAX_PERSON_ID_VALUE) long id){
		List<CarDto> res = carsService.getOwnerCars(id);
		if(res.isEmpty()) {
			log.warn("getOwnerCars: no cars for person with id {}", id);
		} else {
			log.trace("getOwnerCars: cars of person eith id {}", id);
		}
		return res;
	}
	@GetMapping("{carNumber}")
	PersonDto getCarOwner(@PathVariable @NotEmpty(message=MISSING_CAR_NUMBER_MESSAGE) @Pattern(regexp = CAR_NUMBER_REGEXP, 
			message = WRONG_CAR_NUMBER_MESSAGE) String carNumber) {
		log.debug("getCarOwner: receuved cer number {}", carNumber);
		return carsService.getCarOwner(carNumber);
	}

}

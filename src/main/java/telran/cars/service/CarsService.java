package telran.cars.service;

import java.util.List;

import telran.cars.dto.*;

public interface CarsService {
	PersonDto addPerson(PersonDto personDto);
	CarDto addCar(CarDto carDto);
	ModelDto addModel(ModelDto modelDto);
	PersonDto updatePerson(PersonDto personDto);
	PersonDto deletePerson(long id);
	CarDto deleteCar(String carNumber);
	TradeDealDto purchase(TradeDealDto tradeDealDto);
	List<CarDto> getOwnerCars(long id);
	PersonDto getCarOwner(String carNumber);
	List<String> mostPopularModels();

}

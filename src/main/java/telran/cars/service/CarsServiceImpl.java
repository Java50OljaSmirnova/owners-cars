package telran.cars.service;

import java.util.*;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.service.model.*;

@Service
@Slf4j
public class CarsServiceImpl implements CarsService {
	HashMap<Long, CarOwner> owners = new HashMap<>();
	HashMap<String, Car> cars = new HashMap<>();

	@Override
	public PersonDto addPerson(PersonDto personDto) {
		// TODO Auto-generated method stub
				return null;
	}

	@Override
	public CarDto addCar(CarDto carDto) {
		// TODO Auto-generated method stub
				return null;
	}

	@Override
	public PersonDto updatePerson(PersonDto personDto) {
		// TODO Auto-generated method stub
				return null;
	}

	@Override
	public PersonDto deletePerson(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarDto deleteCar(String carNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradeDealDto purchase(TradeDealDto tradeDealDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CarDto> getOwnerCars(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto getCarOwner(String carNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}

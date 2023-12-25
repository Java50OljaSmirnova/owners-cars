package telran.cars.service;

import java.util.*;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.model.*;

@Service
@Slf4j
public class CarsServiceImpl implements CarsService {
	HashMap<Long, CarOwner> owners = new HashMap<>();
	HashMap<String, Car> cars = new HashMap<>();

	@Override
	public PersonDto addPerson(PersonDto personDto) {
		long id = personDto.id();
		if(owners.containsKey(id)) {
			log.error("addPerson: owner with id {} already exist", id);
			throw new IllegalStateException("Person with id " + id + " already exist");
		}
		owners.put(id, new CarOwner(personDto));
		log.debug("addPerson: received person data: {}", personDto);
		return owners.get(id).build();
	}

	@Override
	public CarDto addCar(CarDto carDto) {
		String number = carDto.number();
		if(cars.containsKey(number)) {
			log.error("addCar: car with number {} already exist", number);
			throw new IllegalStateException("Car with number " + number + " already exist");
		}
		cars.put(number, new Car(carDto));
		log.debug("addCar: received car data: {}", carDto);
		return cars.get(number).build();
	}

	@Override
	public PersonDto updatePerson(PersonDto personDto) {
		long id = personDto.id();
		CarOwner owner = owners.putIfAbsent(id, new CarOwner(personDto));
		if(owner == null) {
			log.error("updatePerson: owner with id {} doesn't exist", id);
			throw new IllegalStateException("Person with id " + id + " doesn't exist");
		} else {
			log.debug("updatePerson: received person data: {}", personDto);
		}
		return owners.get(id).build();
	}

	@Override
	public PersonDto deletePerson(long id) {
		CarOwner owner = owners.get(id);
		if(owner == null) {
			log.error("deletePerson: owner with id {} doesn't exist", id);
			throw new NotFoundException("Person with id " + id + " doesn't exist");
		}
		owners.remove(id);
		log.debug("deletePerson: person with ID {}", id);
		return owner.build();
	}

	@Override
	public CarDto deleteCar(String number) {
		Car car = cars.get(number);
		if(car == null) {
			log.error("deleteCar: car with number {} doesn't exist", number);
			throw new NotFoundException("Person with number " + number + " doesn't exist");
		}
		cars.remove(number);
		log.debug("deleteCar: car with number {}", number);
		return car.build();
	}

	@Override
	public TradeDealDto purchase(TradeDealDto tradeDeal) {
		String number = tradeDeal.carNumber();
		long id = tradeDeal.personId();
		if(!cars.containsKey(number) || !owners.containsKey(id)) {
			log.error("purchase: owner with id {} or car with number {} doesn't exist", id, number);
			throw new IllegalStateException("Person with id " + id + "or car with number " + number + " doesn't exist");
		}
		cars.get(number).setOwner(owners.get(id));
		owners.get(id).getCars().add(cars.get(number));
		log.debug("purchase: received trade deal data: {}", tradeDeal);
		return tradeDeal;
	}

	@Override
	public List<CarDto> getOwnerCars(long id) {
		CarOwner owner = owners.get(id);
		if(owner == null) {
			log.error("getOwnerCars: owner with id {} doesn't exist", id);
			throw new IllegalStateException("Person with id " + id + " doesn't exist");
		} 
		List<Car> res = owner.getCars();
		if(res.isEmpty()) {
			log.warn("getOwnerCars: no cars for person with id {}", id);
		} else {
			log.trace("getOwnerCars: cars of person with id {} {}", id, res);
		}
		return res.stream().map(car -> new CarDto(car.getNumber(), car.getModel())).toList();
	}

	@Override
	public PersonDto getCarOwner(String number) {
		Car car = cars.get(number);
		if(car == null) {
			log.error("deleteCar: car with number {} doesn't exist", number);
			throw new IllegalStateException("Car with number " + number + " doesn't exist");
		} 
		CarOwner res = car.getOwner();
		if(res == null) {
			log.warn("getCarOwner: no owner of car with number {}", number);
		} else {
			log.debug("getCarOwner: received car number {}", number);
		}
		return res.build();
	}

}

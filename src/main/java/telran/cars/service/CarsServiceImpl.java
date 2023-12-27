package telran.cars.service;

import java.util.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.model.*;

@Service("carsService")
@Slf4j
@Scope("prototype")
public class CarsServiceImpl implements CarsService {
	HashMap<Long, CarOwner> owners = new HashMap<>();
	HashMap<String, Car> cars = new HashMap<>();

	@Override
	public PersonDto addPerson(PersonDto personDto) {
		log.debug("addPerson: received person data: {}", personDto);
		long id = personDto.id();
		CarOwner res = owners.putIfAbsent(id, new CarOwner(personDto));
		if(res != null) {
			log.error("addPerson: owner with id {} already exist", id);
			throw new IllegalStateException(String.format("person  %d already exists", id));
		}
		return personDto;
	}

	@Override
	public CarDto addCar(CarDto carDto) {
		log.debug("addCar: received car data: {}", carDto);
		String number = carDto.number();
		Car res = cars.putIfAbsent(number, new Car(carDto));
		if(res != null) {
			log.error("addCar: car with number {} already exist", number);
			throw new IllegalStateException(String.format("car %s already exists", number));
		}
		return carDto;
	}

	@Override
	public PersonDto updatePerson(PersonDto personDto) {
		log.debug("updatePerson: received person data: {}", personDto);
		long id = personDto.id();
		CarOwner owner = owners.computeIfPresent(id, (k, v) -> {
			v.setEmail(personDto.email());
			return v;
		});
		hasCarOwner(owner, id);
		return owner.build();
	}

	@Override
	public PersonDto deletePerson(long id) {
		log.debug("deletePerson: person with ID {}", id);
		CarOwner carOwner = owners.get(id);
		hasCarOwner(carOwner, id);
		List<Car> cars = carOwner.getCars();
		cars.forEach(c -> c.setOwner(null));
		owners.remove(id);
		return carOwner.build();
	}
	private void hasCarOwner(CarOwner owner, long id) {
		if(owner == null) {
			throw new NotFoundException(String.format("person %d doesn't exists", id));
		}
	}
	private void hasCar(Car car, String carNumber) {
		if(car == null) {
			throw new NotFoundException(String.format("car %s doesn't exists", carNumber));
		}
	}

	@Override
	public CarDto deleteCar(String number) {
		
		log.debug("deleteCar: car with number {}", number);
		Car car = cars.get(number);
		hasCar(car, number);
		CarOwner carOwner = car.getOwner();
		carOwner.getCars().remove(car);
		cars.remove(number);
		return car.build();
	}

	@Override
	public TradeDealDto purchase(TradeDealDto tradeDeal) {
		log.debug("purchase: received car {}, owner {}", tradeDeal.carNumber(), tradeDeal.personId());
		String number = tradeDeal.carNumber();
		CarOwner carOwner = null;
		Long personId = tradeDeal.personId();
		Car car = cars.get(number);
		hasCar(car, number);
		CarOwner oldOwner = car.getOwner();
		checkSameOwner(personId, oldOwner);
		if(oldOwner != null) {
			oldOwner.getCars().remove(car);
		}
		if(personId != null) {

			log.debug("new owner exists");
			carOwner = owners.get(personId);
			hasCarOwner(carOwner, personId);
			carOwner.getCars().add(car);
		}
		car.setOwner(carOwner);
		return tradeDeal;
	}

	private void checkSameOwner(Long personId, CarOwner oldOwner) {
		if((oldOwner == null && personId == null) ||
				(oldOwner != null && personId == oldOwner.getId())) {
			throw new IllegalStateException("trade deal with same owner");
		}
		
	}

	@Override
	public List<CarDto> getOwnerCars(long id) {
		CarOwner owner = owners.get(id);
		hasCarOwner(owner, id);
		List<Car> res = owner.getCars();
		if(res.isEmpty()) {
			log.warn("getOwnerCars: no cars for person with id {}", id);
		} else {
			log.trace("getOwnerCars: cars of person with id {} {}", id, res);
		}
		return res.stream().map(Car::build).toList();
	}

	@Override
	public PersonDto getCarOwner(String number) {
		Car car = cars.get(number);
		hasCar(car, number);
		CarOwner carOwner = car.getOwner();
		if(carOwner == null) {
			log.warn("getCarOwner: no owner of car with number {}", number);
		} else {
			log.debug("getCarOwner: received car number {}", number);
		}
		return carOwner != null ? carOwner.build() : null;
	}

}

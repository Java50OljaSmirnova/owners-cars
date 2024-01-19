package telran.cars.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.CarNotFoundeException;
import telran.cars.exceptions.IllegalCarStateException;
import telran.cars.exceptions.IllegalPersonStateException;
import telran.cars.exceptions.ModelNotFoundException;
import telran.cars.exceptions.PersonNotFoundException;
import telran.cars.exceptions.TradeDealIllegalStateException;
import telran.cars.repo.*;
import telran.cars.service.model.Car;
import telran.cars.service.model.CarOwner;
import telran.cars.service.model.Model;
import telran.cars.service.model.ModelYear;
import telran.cars.service.model.TradeDeal;
@Service
@RequiredArgsConstructor
@Slf4j
public class CarsServiceImpl implements CarsService {
	final CarRepo carRepo;
	final CarOwnerRepo carOwnerRepo;
	final ModelRepo modelRepo;
	final TradeDealRepo tradeDealRepo;

	@Override
	@Transactional
	public PersonDto addPerson(PersonDto personDto) {
		if(carOwnerRepo.existsById(personDto.id())){
			throw new IllegalPersonStateException();
		}
		CarOwner carOwner = CarOwner.of(personDto);
		carOwnerRepo.save(carOwner);
		log.debug("person {} has been saved", personDto);
		return personDto;
	}

	@Override
	@Transactional
	public CarDto addCar(CarDto carDto) {
		if(carRepo.existsById(carDto.number())) {
			throw new IllegalCarStateException();
		}
		Model model = modelRepo.findById(new ModelYear(carDto.model(), carDto.year())).
				orElseThrow(() -> new ModelNotFoundException());
		Car car = Car.of(carDto);
		car.setModel(model);
		carRepo.save(car);
		log.debug("car {} has been saved", carDto);
		return carDto;
	}

	@Override
	@Transactional
	public PersonDto updatePerson(PersonDto personDto) {
		CarOwner carOwner = carOwnerRepo.findById(personDto.id()).orElseThrow(() -> new PersonNotFoundException());
		carOwner.setEmail(personDto.email());
		return personDto;
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
		Car car = carRepo.findById(tradeDealDto.carNumber()).orElseThrow(() -> new CarNotFoundeException());
		CarOwner carOwner = null;
		Long personId = tradeDealDto.personId();
		if(personId != null) {
			carOwner = carOwnerRepo.findById(personId).orElseThrow(() -> new PersonNotFoundException());
			if(car.getCarOwner().getId() == personId) {
				throw new TradeDealIllegalStateException();
			}
		}
		TradeDeal tradeDeal = new TradeDeal();
		tradeDeal.setCar(car);
		tradeDeal.setCarOwner(carOwner);
		tradeDeal.setDate(LocalDate.parse(tradeDealDto.date()));
		return tradeDealDto;
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

	@Override
	public List<String> mostPopularModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelDto addModel(ModelDto modelDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
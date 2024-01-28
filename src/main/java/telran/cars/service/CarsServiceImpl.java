package telran.cars.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.*;
import telran.cars.repo.*;
import telran.cars.service.model.*;
@Service
@RequiredArgsConstructor
@Slf4j
public class CarsServiceImpl implements CarsService {
	final CarRepo carRepo;
	final CarOwnerRepo carOwnerRepo;
	final ModelRepo modelRepo;
	final TradeDealRepo tradeDealRepo;
	final EntityManager em;

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
	@Transactional
	public PersonDto deletePerson(long id) {
		CarOwner carOwner = carOwnerRepo.findById(id).orElseThrow(() -> new PersonNotFoundException());
		carOwnerRepo.deleteById(id);
		return carOwner.build();
	}

	@Override
	@Transactional
	public CarDto deleteCar(String carNumber) {
		Car car = carRepo.findById(carNumber).orElseThrow(() -> new CarNotFoundException());
		carRepo.deleteById(carNumber);
		return car.build();
	}

	@Override
	@Transactional
	public TradeDealDto purchase(TradeDealDto tradeDealDto) {
		Car car = carRepo.findById(tradeDealDto.carNumber()).orElseThrow(() -> new CarNotFoundException());
		CarOwner oldCarOwner = car.getCarOwner();
		CarOwner newCarOwner = null;
		Long personId = tradeDealDto.personId();
		if(personId != null) {
			log.debug("ID of new car's owner is {}", personId);
			newCarOwner = carOwnerRepo.findById(personId).orElseThrow(() -> new PersonNotFoundException());
			if(oldCarOwner != null && oldCarOwner.getId() == personId) {
				throw new TradeDealIllegalStateException();
			}
		} else if (oldCarOwner == null) {
			throw new TradeDealIllegalStateException();
		}
		TradeDeal tradeDeal = new TradeDeal();
		tradeDeal.setCar(car);
		tradeDeal.setCarOwner(newCarOwner);
		tradeDeal.setDate(LocalDate.parse(tradeDealDto.date()));
		car.setCarOwner(newCarOwner);
		tradeDealRepo.save(tradeDeal);
		log.debug("trade: {} has been saved", tradeDealDto);
		return tradeDealDto;
	}

	@Override
	@Transactional
	public List<CarDto> getOwnerCars(long id) {
		if(!carOwnerRepo.existsById(id)) {
			throw new PersonNotFoundException();
		}
		List<Car> cars = carRepo.findByCarOwnerId(id);
		if(cars.isEmpty()) {
			log.warn("person with id {} has no car", id);
		} else {
			log.debug("person with id {} has {} cars", id, cars.size());
		}
		return cars.stream().map(Car::build).toList();
	}

	@Override
	@Transactional
	public PersonDto getCarOwner(String carNumber) {
		Car car = carRepo.findById(carNumber).orElseThrow(()-> new CarNotFoundException());
		CarOwner carOwner = car.getCarOwner();
		
		return carOwner != null ? carOwner.build() : null;
	}

	@Override
	@Transactional
	public List<String> mostSoldModelNames() {
		List<String> res = modelRepo.findMostSoldModelNames();
		log.trace("most sold model names ar {}", res);
		return res;
	}

	@Override
	@Transactional
	public ModelDto addModel(ModelDto modelDto) {
		ModelYear modelYear = new ModelYear(modelDto.getModelName(), modelDto.getModelYear());
		if(modelRepo.existsById(modelYear)) {
			throw new ModelIllegalStateException();
		}
		Model model = Model.of(modelDto);
		modelRepo.save(model);
		return modelDto;
	}

	@Override
	public List<ModelNameAmount> mostPopularModelNames(int nModels) {
		List<ModelNameAmount> res = modelRepo.findMostPopularModelNames(nModels);
		logModelNameAmounts(res);
		return res;
	}

	private void logModelNameAmounts(List<ModelNameAmount> list) {
		list.forEach(mn -> log.debug("model name is {}, number of cars {}", mn.getName(), mn.getAmount()));
	}

	@Override
	/**
	 * returns count of trade deals for a given 'modelName'
	 * at a given year / month
	 * Try to apply only interface method name without @Query annotation
	 */
	public long countTradeDealAtMonthModel(String modelName, int month, int year) {
		LocalDate date1 = LocalDate.of(year, month, 1);
		LocalDate date2 = date1.with(TemporalAdjusters.lastDayOfMonth());
		long res = tradeDealRepo.countByCarModelModelYearNameAndDateBetween(modelName, date1, date2);
		log.debug("count of trade deals on year {}, month {}, of model {} is {}",
				year, month, modelName, res);
		return res;
	}

	@Override
	/**
	 * returns list of a given number of most popular (most cars amount)
	 *  model names and appropriate amounts of the cars,
	 * owners of which have an age in a given range
	 */
	public List<ModelNameAmount> mostPopularModelNameByOwnerAges(int nModels, int ageFrom, int ageTo) {
		LocalDate birthDate1 = getBirthDate(ageTo);
		LocalDate birthDate2 = getBirthDate(ageFrom);
		List<ModelNameAmount> res = modelRepo.findMostPopularModelNameByOwnerAges(nModels,
				birthDate1, birthDate2);
		logModelNameAmounts(res);
		
		return res;
		
	}

	@Override
	/**
	 * returns one most popular color of a given model
	 */
	public String oneMostPopularColorModel(String model) {
		String res = carRepo.findOneMostPopularColorModel(model);
		log.debug("most popular color of {} is {}", model, res);
		return res;
	}
	
	private LocalDate getBirthDate(int age) {

		return LocalDate.now().minusYears(age);
	}

	@Override
	/**
	 * returns minimal values of engine power and capacity
	 * of car owners having an age in a given range
	 */
	public EnginePowerCapacity minEnginePowerCapacityByOwnerAges(int ageFrom, int ageTo) {
		LocalDate birthDate1 = getBirthDate(ageTo);
		LocalDate birthDate2 = getBirthDate(ageFrom);
		EnginePowerCapacity res =
				carRepo.findMinEnginePowerCapacityByOwnerAges(birthDate1, birthDate2);
		log.debug("min engine capacity is {}, min power is {} of cars belonging to "
				+ "owners of ages {}-{}", res.getCapacity(), res.getPower(),
				ageFrom, ageTo);
		return res;
	}

	@Override
	public List<String> anyQuery(QueryDto queryDto) {
		try {
			Query query = queryDto.type() == QueryType.JPQL ? em.createQuery(queryDto.query())
					: em.createNativeQuery(queryDto.query());
			List<String> res = getResult(query);
			log.debug("Query result: {}", res);
			return res;
		} catch (Throwable e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<String> getResult(Query query) {
		List resultList = query.getResultList();
		List<String> res = Collections.emptyList();
		if(!resultList.isEmpty()) {
			res = resultList.get(0).getClass().isArray() ? miltiColumnsProjection((List<Object[]>)resultList) : 
				singleColumnsProjection(resultList);
		}
		log.debug("result: {}", res);
		return res;
	}

	private List<String> singleColumnsProjection(List<Object> resultList) {
		
		return resultList.stream().map(Object::toString).toList();
	}

	private List<String> miltiColumnsProjection(List<Object[]> resultList) {
		
		return resultList.stream().map(Arrays::deepToString).toList();
	}

}
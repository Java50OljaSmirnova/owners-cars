package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.cars.dto.*;
import telran.cars.repo.*;
import telran.cars.exceptions.CarNotFoundException;
import telran.cars.exceptions.IllegalCarStateException;
import telran.cars.exceptions.IllegalPersonStateException;
import telran.cars.exceptions.ModelNotFoundException;
import telran.cars.exceptions.PersonNotFoundException;
import telran.cars.exceptions.TradeDealIllegalStateException;
import telran.cars.service.CarsService;
import telran.cars.service.model.CarOwner;
import telran.cars.service.model.TradeDeal;
@SpringBootTest
@Sql(scripts = {"classpath:test_data.sql"})
class CarsServiceTest {
	private static final String CAR_NUMBER_1 = "111-11-111";
	private static final String CAR_NUMBER_2 = "222-11-111";
	private static final String CAR_NUMBER_3 = "333-11-111";
	private static final String CAR_NUMBER_4 = "444-44-444";
	private static final String CAR_NUMBER_5 = "555-55-555";
	private static final String MODEL_1 = "model1";
	private static final String MODEL_2 = "model2";
	private static final String MODEL_3 = "model3";
	private static final String MODEL_4 = "model4";
	private static final long PERSON_ID_1 = 123l;
	private static final long PERSON_ID_2 = 124l;
	private static final long PERSON_ID_3 = 125l;
	private static final String PERSON_NAME_1=  "name1";
	private static final String PERSON_NAME_2=  "name2";
	private static final String PERSON_NAME_3=  "name3";
	private static final String PERSON_BIRTHDATE_1 = "2000-10-10";
	private static final String PERSON_BIRTHDATE_2 = "1990-12-20";
	private static final String PERSON_BIRTHDATE_3 = "1975-12-10";
	private static final String PERSON_EMAIL_1 = "name1@gmail.com";
	private static final String PERSON_EMAIL_2 = "name2@gmail.com";
	private static final String PERSON_EMAIL_3 = "name3@gmail.com";
	private static final int MODEL_YEAR_1 = 2020;
	private static final int MODEL_YEAR_2 = 2022;
	private static final int MODEL_YEAR_3 = 2023;
	private static final  String COLOR_1 = "red";
	private static final  String COLOR_2 = "silver";
	private static final  String COLOR_3 = "white";
	private static final int KILOMETERS_1 = 1000;
	private static final int KILOMETERS_2 = 10000;
	private static final  CarState CAR_STATE_1 = CarState.GOOD;
	private static final  CarState CAR_STATE_2 = CarState.OLD;
	private static final  CarState CAR_STATE_3 = CarState.MIDDLE;
	private static final  CarState CAR_STATE_4 = CarState.NEW;
	private static final  String DATE_1 = "2023-03-10";
	private static final long PERSON_ID_NOT_EXIST = 111111111l;
	private static final  String NEW_EMAIL = "name1@tel-ran.co.il";
	
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRTHDATE_1, PERSON_EMAIL_1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, PERSON_NAME_2, PERSON_BIRTHDATE_2, PERSON_EMAIL_2);
	PersonDto personDto3 = new PersonDto(PERSON_ID_3, PERSON_NAME_3, PERSON_BIRTHDATE_3, PERSON_EMAIL_3);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXIST, PERSON_NAME_1, PERSON_BIRTHDATE_1, PERSON_EMAIL_1);
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL_1, MODEL_YEAR_1, COLOR_1, KILOMETERS_1, CAR_STATE_1);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL_1, MODEL_YEAR_1, COLOR_2, KILOMETERS_2, CAR_STATE_2);
	CarDto car3 = new CarDto(CAR_NUMBER_3 , MODEL_4, MODEL_YEAR_3, COLOR_3, 0, CAR_STATE_4);
	CarDto car4 = new CarDto(CAR_NUMBER_4, MODEL_4, 2023, COLOR_3, 0, CAR_STATE_4);
	CarDto car5 = new CarDto(CAR_NUMBER_5, MODEL_3, 2021, COLOR_2, 5000, CAR_STATE_3);
	
	@Autowired
	CarOwnerRepo carOwnerRepo;
	@Autowired
	CarRepo carRepo;
	@Autowired
	TradeDealRepo tradeDealRepo;
	@Autowired
	CarsService carsService;
		
	@Test
	void testAddPerson() {
		assertEquals(personDto, carsService.addPerson(personDto));
		assertThrowsExactly(IllegalPersonStateException.class,
				()->carsService.addPerson(personDto1));
		CarOwner carOwner = carOwnerRepo.findById(personDto.id()).orElse(null);
		assertEquals(personDto, carOwner.build());
	}

	@Test
	void testAddCar() {
		assertEquals(car4, carsService.addCar(car4));
		CarDto carNoModel = new CarDto("11111111111", MODEL_1, 2018, "green", 100000, CarState.OLD);
		assertThrowsExactly(IllegalCarStateException.class, ()-> carsService.addCar(car3));
		assertThrowsExactly(ModelNotFoundException.class, ()-> carsService.addCar(carNoModel));
	}
	@Test
	void testAddModel() {
		ModelDto modelDtoNew = new ModelDto(MODEL_1, MODEL_YEAR_2, "Company1", 500, 5000);
		assertEquals(modelDtoNew, carsService.addModel(modelDtoNew));
	}

	@Test
	void testUpdatePerson() {
		PersonDto personUpdated = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRTHDATE_1, NEW_EMAIL);
		assertEquals(personUpdated, carsService.updatePerson(personUpdated));
		assertEquals(NEW_EMAIL, carOwnerRepo.findById(PERSON_ID_1).get().getEmail());
		assertThrowsExactly(PersonNotFoundException.class,
				() -> carsService.updatePerson(personDto));
	}

	@Test
	void testDeletePerson() {
		assertEquals(personDto1, carsService.deletePerson(PERSON_ID_1));
		assertThrowsExactly(PersonNotFoundException.class, () -> carsService.deletePerson(PERSON_ID_1));
	}

	@Test
	void testDeleteCar() {
		assertEquals(car1, carsService.deleteCar(CAR_NUMBER_1));
		assertThrowsExactly(CarNotFoundException.class, () -> carsService.deleteCar(CAR_NUMBER_1));
	}

	@Test
	void testPurchaseNewCarOwnerWithOldOwner() {
		int countDeals = (int)tradeDealRepo.count(); 
		TradeDealDto tradeDealDto = new TradeDealDto(CAR_NUMBER_1, PERSON_ID_2, DATE_1);
		assertEquals(tradeDealDto, carsService.purchase(tradeDealDto));
		assertEquals(PERSON_ID_2, carRepo.findById(CAR_NUMBER_1).get().getCarOwner().getId());
		TradeDeal tradeDeal = tradeDealRepo.findAll().get(countDeals);
		assertEquals(CAR_NUMBER_1, tradeDeal.getCar().getNumber());
		assertEquals(PERSON_ID_2, tradeDeal.getCarOwner().getId());
		assertEquals(DATE_1, tradeDeal.getDate().toString());

	}
	@Test
	void testPurchaseNewCarOwnerWithoutOldOwner() {
		int countDeals = (int)tradeDealRepo.count(); 
		carsService.addCar(car4);
		TradeDealDto tradeDealDto = new TradeDealDto(CAR_NUMBER_4, PERSON_ID_2, DATE_1);
		assertEquals(tradeDealDto, carsService.purchase(tradeDealDto));
		assertEquals(PERSON_ID_2, carRepo.findById(CAR_NUMBER_4).get().getCarOwner().getId());
		TradeDeal tradeDeal = tradeDealRepo.findAll().get(countDeals);
		assertEquals(CAR_NUMBER_4, tradeDeal.getCar().getNumber());
		assertEquals(PERSON_ID_2, tradeDeal.getCarOwner().getId());
		assertEquals(DATE_1, tradeDeal.getDate().toString());



	}
	@Test
	void testPurchaseNotFound() {
		TradeDealDto tradeDealCarNotFound = new TradeDealDto(CAR_NUMBER_4, PERSON_ID_1, DATE_1);
		TradeDealDto tradeDealOwnerNotFound = new TradeDealDto(CAR_NUMBER_1,
				PERSON_ID_NOT_EXIST, DATE_1);
		assertThrowsExactly(PersonNotFoundException.class, () -> carsService.purchase(tradeDealOwnerNotFound));
		assertThrowsExactly(CarNotFoundException.class, () -> carsService.purchase(tradeDealCarNotFound));

	}
	void testPurchaseNoNewCarOwner() {
		int countDeals = (int)tradeDealRepo.count(); 
		TradeDealDto tradeDealDto = new TradeDealDto(CAR_NUMBER_1,null, DATE_1);
		assertEquals(tradeDealDto, carsService.purchase(tradeDealDto));
		assertNull(carRepo.findById(CAR_NUMBER_1).get().getCarOwner());
		TradeDeal tradeDeal = tradeDealRepo.findAll().get(countDeals);
		assertEquals(CAR_NUMBER_1, tradeDeal.getCar().getNumber());
		assertNull(tradeDeal.getCarOwner());
		assertEquals(DATE_1, tradeDeal.getDate().toString());
	}
	@Test
	void testPurchaseSameOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,PERSON_ID_1, DATE_1);
		assertThrowsExactly(TradeDealIllegalStateException.class,
				() -> carsService.purchase(tradeDeal));
		carsService.addCar(car4);
		TradeDealDto tradeDealNoOwners = new TradeDealDto(CAR_NUMBER_4,null, DATE_1);
		assertThrowsExactly(TradeDealIllegalStateException.class,
				() -> carsService.purchase(tradeDealNoOwners));
	}


	@Test
	/**
	 * test of the method getOwnerCars
	 * the method has been written at CW #64
	 */
	void testGetOwnerCars() {
		carsService.addCar(car4);
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_4, PERSON_ID_2, DATE_1);
		carsService.purchase(tradeDeal);
		List<CarDto> actual = carsService.getOwnerCars(PERSON_ID_2);
		assertEquals(2, actual.size());
		assertEquals(car4, actual.get(1));
		assertThrowsExactly(PersonNotFoundException.class, ()-> carsService.getOwnerCars(126l));
	}
	@Test
	/**
	 * test of the method getCarOwner
	 * the method has been written at CW #64
	 */
	void testGetCarOwner() {
		PersonDto actual = carsService.getCarOwner(CAR_NUMBER_2);
		assertEquals(personDto2, actual);
		assertThrowsExactly(CarNotFoundException.class, ()-> carsService.getCarOwner(CAR_NUMBER_4));
	}
	@Test
	/**
	 * test of the method mostSoldModelNames
	 * the method has been written at CW #64
	 */
	void testMostSoldModelNames() {
		List<String> actual = carsService.mostSoldModelNames();
		assertEquals(1, actual.size());

	}
	@Test
	/**
	 * test of the method mostPopularModelNames
	 * the method has been written at CW #64
	 */
	void testMostPopularModelNames() {
		carsService.addCar(car4);
		carsService.addCar(car5);
		List<ModelNameAmount> actual = carsService.mostPopularModelNames(4);
		assertEquals(3, actual.size());
		assertEquals(MODEL_1, actual.get(0).getModelName());
		assertEquals(2, actual.get(1).getAmount());
	}
	//tests for the methods of the HW #64
	@Test
	void testCountTradeDealAtMonthModel() {
		long actual1 = carsService.countTradeDealAtMonthModel(MODEL_1, 3, 2023);
		assertEquals(2, actual1);
		long actual2 = carsService.countTradeDealAtMonthModel(MODEL_1, 5, 2023);
		assertEquals(0, actual2);
	}
	@Test
	void testMostPopularModelNameByOwnerAges() {
		List<ModelNameAmount> actual = carsService.mostPopularModelNameByOwnerAges(2, 20, 55);
		assertEquals(2, actual.size());
		assertEquals(MODEL_1, actual.get(0).getModelName());
	}
	@Test
	void testOneMostPopularColorModel() {
		carsService.addCar(car4);
		carsService.addCar(car5);
		String actual1 = carsService.oneMostPopularColorModel(MODEL_4);
		assertEquals(COLOR_3, actual1);
		String actual2 = carsService.oneMostPopularColorModel(MODEL_1);
		assertEquals(COLOR_1, actual2);
		String actual3 = carsService.oneMostPopularColorModel(MODEL_2);
		assertEquals(null, actual3);
		
		
		
	}
	@Test
	void testMinEnginePowerCapacityByOwnerAges() {
		EnginePowerCapacity actual1 = carsService.minEnginePowerCapacityByOwnerAges(20, 35);
		assertEquals(84, actual1.getEnginePower());
		assertEquals(1300, actual1.getEngineCapacity());
	}



}

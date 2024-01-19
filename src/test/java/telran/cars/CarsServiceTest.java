package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.jdbc.Sql;

import telran.cars.dto.*;
import telran.cars.exceptions.IllegalPersonStateException;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.CarsService;
@SpringBootTest
@Sql(scripts = {"classpath:test_data.sql"})
class CarsServiceTest {
	private static final String CAR_NUMBER_1 = "123-45-56";
	private static final String CAR_NUMBER_2 = "123-45-67";
	private static final String CAR_NUMBER_3 = "132-54-76";
	private static final String CAR_NUMBER_4 = "765-43-21";
	private static final String CAR_NUMBER_5 = "174-62-53";
	private static final String MODEL_1 = "honda";
	private static final String MODEL_2 = "mazda";
	private static final String MODEL_3 = "toyota";
	private static final Long PERSON_ID_1 = 123456l;
	private static final String PERSON_NAME_1=  "Vasya";
	private static final String PERSON_BIRTHDATE_1 = "1990-02-10";
	private static final String PERSON_EMAIL_1 = "vasya1@gmail.com";
	private static final Long PERSON_ID_2 = 234567l;
	private static final String PERSON_NAME_2=  "Kolya";
	private static final String PERSON_BIRTHDATE_2 = "1990-03-20";
	private static final String PERSON_EMAIL_2 = "kolya2@gmail.com";
	private static final Long PERSON_ID_NOT_EXIST = 111111111l;
	private static final  String NEW_EMAIL = "name1@tel-ran.co.il";
	private static final int MODEL_YEAR_1 = 2019;
	private static final int MODEL_YEAR_2 = 2023;
	private static final int MODEL_YEAR_3 = 2010;
	private static final  String COLOR_1 = "red";
	private static final  String COLOR_2 = "black";
	private static final  String COLOR_3 = "white";
	private static final int KILOMETERS_1 = 2010;
	private static final int KILOMETERS_2 = 2010;
	private static final  CarState CAR_STATE_1 = CarState.GOOD;
	private static final  CarState CAR_STATE_2 = CarState.OLD;
	private static final  CarState CAR_STATE_3 = CarState.MIDDLE;
	private static final  CarState CAR_STATE_4 = CarState.NEW;
	private static final  String DATE_1 = "2022-10-10";
	private static final  String DATE_2 = "2023-01-11";
	private static final  String DATE_3 = "2020-05-21";
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRTHDATE_1, PERSON_EMAIL_1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, PERSON_NAME_2, PERSON_BIRTHDATE_2, PERSON_EMAIL_2);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXIST, PERSON_NAME_1, PERSON_BIRTHDATE_1, PERSON_EMAIL_1);
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL_1, MODEL_YEAR_1, COLOR_1, KILOMETERS_1, CAR_STATE_3);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL_2, MODEL_YEAR_2, COLOR_2, 0, CAR_STATE_4);
	CarDto car3 = new CarDto(CAR_NUMBER_3 , MODEL_2, MODEL_YEAR_3, COLOR_1, KILOMETERS_2, CAR_STATE_2);
	CarDto car4 = new CarDto(CAR_NUMBER_4 , MODEL_3, MODEL_YEAR_1, COLOR_3, KILOMETERS_1, CAR_STATE_3);
	CarDto car5 = new CarDto(CAR_NUMBER_5 , MODEL_3, MODEL_YEAR_2, COLOR_2, KILOMETERS_2, CAR_STATE_1);
	
	@Autowired
	CarsService carsService;
		
	@Test
	void scriptTest() {
		assertThrowsExactly(IllegalPersonStateException.class,
				()->carsService.addPerson(personDto1));
	}

	@Test
	@Disabled
	void testAddPerson() {
		assertEquals(personDto, carsService.addPerson(personDto));
		assertThrowsExactly(IllegalStateException.class,
				()->carsService.addPerson(personDto1));
		List<CarDto> cars = carsService.getOwnerCars(personDto.id());
		assertTrue(cars.isEmpty());
		assertEquals(personDto, carsService.deletePerson(personDto.id()));
	}

	@Test
	void testAddCar() {
		assertEquals(car3, carsService.addCar(car3));
		assertThrowsExactly(IllegalStateException.class,
				()->carsService.addCar(car1));
		PersonDto person = carsService.getCarOwner(CAR_NUMBER_3);
		assertNull(person);
	}

	@Test
	void testUpdatePerson() {
		PersonDto personUpdated = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRTHDATE_1, NEW_EMAIL);
		assertEquals(personUpdated, carsService.updatePerson(personUpdated));
		assertEquals(personUpdated, carsService.getCarOwner(CAR_NUMBER_1));
		assertThrowsExactly(NotFoundException.class,
				() -> carsService.updatePerson(personDto));
	}

	@Test
	void testDeletePerson() {
		List<CarDto> cars = carsService.getOwnerCars(PERSON_ID_1);
		assertEquals(personDto1, carsService.deletePerson(PERSON_ID_1));
		assertThrowsExactly(NotFoundException.class, () -> carsService.deletePerson(PERSON_ID_1));
		cars.forEach(c -> assertNull(carsService.getCarOwner(c.number())));
	}

	@Test
	void testDeleteCar() {
		Long id = carsService.getCarOwner(CAR_NUMBER_1).id();
		assertEquals(car1, carsService.deleteCar(CAR_NUMBER_1));
		assertThrowsExactly(NotFoundException.class, () -> carsService.deleteCar(CAR_NUMBER_1));
		assertFalse(carsService.getOwnerCars(id).contains(car1));
	}

	@Test
	void testPurchaseNewCarOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1, PERSON_ID_2, DATE_3);
		assertEquals(tradeDeal, carsService.purchase(tradeDeal));
		assertEquals(personDto2, carsService.getCarOwner(CAR_NUMBER_1));
		assertFalse(carsService.getOwnerCars(PERSON_ID_1).contains(car1));
		assertTrue(carsService.getOwnerCars(PERSON_ID_2).contains(car1));

	}
	@Test
	void testPurchaseNotFound() {
		TradeDealDto tradeDealCarNotFound = new TradeDealDto(CAR_NUMBER_3, PERSON_ID_1, DATE_2);
		TradeDealDto tradeDealOwnerNotFound = new TradeDealDto(CAR_NUMBER_1,
				PERSON_ID_NOT_EXIST, DATE_1);
		assertThrowsExactly(NotFoundException.class, () -> carsService.purchase(tradeDealOwnerNotFound));
		assertThrowsExactly(NotFoundException.class, () -> carsService.purchase(tradeDealCarNotFound));

	}
	@Test
	void testPurchaseNoCarOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,null, DATE_3);
		assertEquals(tradeDeal, carsService.purchase(tradeDeal));
		assertFalse(carsService.getOwnerCars(PERSON_ID_1).contains(car1));
		assertNull(carsService.getCarOwner(CAR_NUMBER_1));
	}
	@Test
	void testPurchaseSameOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,PERSON_ID_1, DATE_2);
		assertThrowsExactly(IllegalStateException.class, () -> carsService.purchase(tradeDeal));
	}


	@Test
	void testGetOwnerCars() {
		List<CarDto> ownerCars = carsService.getOwnerCars(PERSON_ID_1);
		assertNotNull(ownerCars);
	}

	@Test
	void testGetCarOwner() {

		assertEquals(personDto1, carsService.getCarOwner(CAR_NUMBER_1));
	}
	@Test
	void testMostPopularModels() {
		carsService.addCar(car3);
		carsService.addCar(car4);
		carsService.addCar(car5);
		carsService.purchase(new TradeDealDto(CAR_NUMBER_3, PERSON_ID_2, DATE_1));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_4, PERSON_ID_1, DATE_2));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_5, PERSON_ID_2, DATE_3));
		List<String> mostPopularModels = carsService.mostPopularModels();
		String[] actual = mostPopularModels.toArray(String[]::new);
		Arrays.sort(actual);
		String[] expected = {
				MODEL_2, MODEL_3
		};
		Arrays.sort(expected);
		assertArrayEquals(expected, actual);
				
	}

}

package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import telran.cars.dto.*;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.CarsService;
@SpringBootTest
class CarsServiceTest {
	private static final String CAR_NUMBER_1 = "123-45-56";
	private static final String MODEL_1 = "honda";
	private static final String CAR_NUMBER_2 = "123-45-67";
	private static final String MODEL_2 = "mazda";
	private static final Long PERSON_ID_1 = 123456l;
	private static final String PERSON_NAME_1=  "Vasya";
	private static final String PERSON_BIRTHDATE_1 = "1990-02-10";
	private static final String PERSON_EMAIL_1 = "vasya1@gmail.com";
	private static final Long PERSON_ID_2 = 234567l;
	private static final String PERSON_NAME_2=  "Kolya";
	private static final String PERSON_BIRTHDATE_2 = "1990-03-20";
	private static final String PERSON_EMAIL_2 = "kolya2@gmail.com";
	private static final Long PERSON_ID_NOT_EXIST = 111111111l;
	private static final String CAR_NUMBER_3 = "120-45-50";
	private static final  String NEW_EMAIL = "name1@tel-ran.co.il";
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRTHDATE_1, PERSON_EMAIL_1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, PERSON_NAME_2, PERSON_BIRTHDATE_2, PERSON_EMAIL_2);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXIST, PERSON_NAME_1, PERSON_BIRTHDATE_1, PERSON_EMAIL_1);
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL_1);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL_2);
	CarDto car3 = new CarDto(CAR_NUMBER_3 , MODEL_2);
	
	@Autowired
	ApplicationContext ctx;
	CarsService carsService;
	@BeforeEach
	void setUp() {
		carsService = ctx.getBean("carsService", CarsService.class);
		carsService.addCar(car1);
		carsService.addCar(car2);
		carsService.addPerson(personDto1);
		carsService.addPerson(personDto2);
		carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_1));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_2, PERSON_ID_2));
	
	}

	@Test
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
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1, PERSON_ID_2);
		assertEquals(tradeDeal, carsService.purchase(tradeDeal));
		assertEquals(personDto2, carsService.getCarOwner(CAR_NUMBER_1));
		assertFalse(carsService.getOwnerCars(PERSON_ID_1).contains(car1));
		assertTrue(carsService.getOwnerCars(PERSON_ID_2).contains(car1));

	}
	@Test
	void testPurchaseNotFound() {
		TradeDealDto tradeDealCarNotFound = new TradeDealDto(CAR_NUMBER_3, PERSON_ID_1);
		TradeDealDto tradeDealOwnerNotFound = new TradeDealDto(CAR_NUMBER_1,
				PERSON_ID_NOT_EXIST);
		assertThrowsExactly(NotFoundException.class, () -> carsService.purchase(tradeDealOwnerNotFound));
		assertThrowsExactly(NotFoundException.class, () -> carsService.purchase(tradeDealCarNotFound));

	}
	@Test
	void testPurchaseNoCarOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,null);
		assertEquals(tradeDeal, carsService.purchase(tradeDeal));
		assertFalse(carsService.getOwnerCars(PERSON_ID_1).contains(car1));
		assertNull(carsService.getCarOwner(CAR_NUMBER_1));
	}
	@Test
	void testPurchaseSameOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,PERSON_ID_1);
		assertThrowsExactly(IllegalStateException.class, () -> carsService.purchase(tradeDeal));
	}


	@Test
	void testGetOwnerCars() {
//		carsService.addPerson(personDto1);
//		List<CarDto> ownerCars = carsService.getOwnerCars(PERSON_ID_1);
//		assertNotNull(ownerCars);
	}

	@Test
	void testGetCarOwner() {
//		carsService.addCar(car1);
//		carsService.purchase(tradeDeal1);
//		assertEquals(personDto1, carsService.getCarOwner(CAR_NUMBER_1));
	}

}

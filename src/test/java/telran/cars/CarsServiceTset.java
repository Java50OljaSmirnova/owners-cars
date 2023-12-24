package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.cars.dto.*;
import telran.cars.service.CarsService;
@SpringBootTest
class CarsServiceTset {
	private static final String CAR_NUMBER_1 = "123-45-56";
	private static final String MODEL_1 = "honda";
	private static final String CAR_NUMBER_2 = "123-45-67";
	private static final String MODEL_2 = "mazda";
	private static final Long PERSON_ID_1 = 123456l;
	private static final String PERSON_NAME_1=  "Vasya";
	private static final String PERSON_BIRHTDATE_1 = "1990-02-10";
	private static final String PERSON_EMAIL_1 = "vasya1@gmail.com";
	private static final Long PERSON_ID_2 = 234567l;
	private static final String PERSON_NAME_2=  "Kolya";
	private static final String PERSON_BIRHTDATE_2 = "1990-03-20";
	private static final String PERSON_EMAIL_2 = "kolya2@gmail.com";
	private static final Long PERSON_ID_NOT_EXIST = 111111l;
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRHTDATE_1, PERSON_EMAIL_1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, PERSON_NAME_2, PERSON_BIRHTDATE_2, PERSON_EMAIL_2);
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL_1);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL_2);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXIST, PERSON_NAME_1, PERSON_BIRHTDATE_1, PERSON_EMAIL_1);
	
	@Autowired
	CarsService carsService;
	@BeforeEach
	void setUp() {
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
		assertThrowsExactly(IllegalStateException.class, () -> carsService.addPerson(personDto1));
	}

	@Test
	void testAddCar() {
		//TODO
	}

	@Test
	void testUpdatePerson() {
		//TODO
	}

	@Test
	void testDeletePerson() {
		//TODO
	}

	@Test
	void testDeleteCar() {
		//TODO
	}

	@Test
	void testPurchase() {
		//TODO
	}

	@Test
	void testGetOwnerCars() {
		//TODO
	}

	@Test
	void testGetCarOwner() {
		//TODO
	}

}

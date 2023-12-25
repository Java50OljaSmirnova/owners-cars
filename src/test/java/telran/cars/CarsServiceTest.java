package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
	private static final String PERSON_BIRHTDATE_1 = "1990-02-10";
	private static final String PERSON_EMAIL_1 = "vasya1@gmail.com";
	private static final Long PERSON_ID_2 = 234567l;
	private static final String PERSON_NAME_2=  "Kolya";
	private static final String PERSON_BIRHTDATE_2 = "1990-03-20";
	private static final String PERSON_EMAIL_2 = "kolya2@gmail.com";
	private static final Long PERSON_ID_NOT_EXIST = 111111l;
	private static final String CAR_NUMBER_NOT_EXIST = "120-45-50";
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRHTDATE_1, PERSON_EMAIL_1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, PERSON_NAME_2, PERSON_BIRHTDATE_2, PERSON_EMAIL_2);
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL_1);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL_2);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXIST, PERSON_NAME_1, PERSON_BIRHTDATE_1, PERSON_EMAIL_1);
	CarDto carDto = new CarDto(CAR_NUMBER_NOT_EXIST , MODEL_2);
	TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_NOT_EXIST, PERSON_ID_1);
	TradeDealDto tradeDeal1 = new TradeDealDto(CAR_NUMBER_1, PERSON_ID_1);
	TradeDealDto tradeDeal2 = new TradeDealDto(CAR_NUMBER_2, PERSON_ID_2);
	@Autowired
	CarsService carsService;
//	@BeforeEach
//	void setUp() {
//		carsService.addCar(car1);
//		carsService.addPerson(personDto1);
//		carsService.purchase(tradeDeal1);
//	
//	}

	@Test
	void testAddPerson() {
		assertEquals(personDto, carsService.addPerson(personDto));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.addPerson(personDto));
	}

	@Test
	void testAddCar() {
		assertEquals(carDto, carsService.addCar(carDto));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.addCar(carDto));
	}

	@Test
	void testUpdatePerson() {
		carsService.addPerson(personDto1);
		PersonDto personDtoUpdate = new PersonDto(PERSON_ID_1, PERSON_NAME_1, PERSON_BIRHTDATE_1, PERSON_EMAIL_2);
		
		carsService.updatePerson(personDtoUpdate);
	}

	@Test
	void testDeletePerson() {
		assertEquals(personDto1, carsService.deletePerson(PERSON_ID_1));
		assertThrowsExactly(NotFoundException.class, () -> carsService.deletePerson(PERSON_ID_NOT_EXIST));
	}

	@Test
	void testDeleteCar() {
		assertEquals(car2, carsService.deleteCar(CAR_NUMBER_2));
		assertThrowsExactly(NotFoundException.class, () -> carsService.deleteCar(CAR_NUMBER_NOT_EXIST));
	}

	@Test
	void testPurchase() {
		carsService.addCar(car2);
		carsService.addPerson(personDto2);
		TradeDealDto actualy = carsService.purchase(tradeDeal2);
		assertEquals(tradeDeal2, actualy);
	}

	@Test
	void testGetOwnerCars() {
		carsService.addPerson(personDto1);
		List<CarDto> ownerCars = carsService.getOwnerCars(PERSON_ID_1);
		assertNotNull(ownerCars);
	}

	@Test
	void testGetCarOwner() {
		carsService.addCar(car1);
		carsService.purchase(tradeDeal1);
		assertEquals(personDto1, carsService.getCarOwner(CAR_NUMBER_1));
	}

}

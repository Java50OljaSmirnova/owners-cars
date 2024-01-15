package telran.cars;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.cars.api.ValidationConstants.MISSING_BIRTH_DATE_MESSAGE;
import static telran.cars.api.ValidationConstants.MISSING_CAR_MODEL_MESSAGE;
import static telran.cars.api.ValidationConstants.MISSING_CAR_NUMBER_MESSAGE;
import static telran.cars.api.ValidationConstants.MISSING_PERSON_EMAIL;
import static telran.cars.api.ValidationConstants.MISSING_PERSON_ID_MESSAGE;
import static telran.cars.api.ValidationConstants.MISSING_PERSON_NAME_MESSAGE;
import static telran.cars.api.ValidationConstants.WRONG_CAR_NUMBER_MESSAGE;
import static telran.cars.api.ValidationConstants.WRONG_DATE_FORMAT;
import static telran.cars.api.ValidationConstants.WRONG_EMAIL_FORMAT;
import static telran.cars.api.ValidationConstants.WRONG_MAX_PERSON_ID_VALUE;
import static telran.cars.api.ValidationConstants.WRONG_MIN_PERSON_ID_VALUE;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.*;
import telran.cars.exceptions.NotFoundException;
import telran.cars.exceptions.controller.CarsExceptionsController;
import telran.cars.service.CarsService;

record PersonDtoIdString(String id, String name, String birthDate, String email) {
	
}
@WebMvcTest
class CarsControllerTest {
	private static final String CORRECT_BIRTH_DATE = "1999-12-12";
	private static final String WRONG_BIRTH_DATE = "03-03-93";
	private static final String WRONG_EMAIL_ADRESS = "kuku";
	private static final String CORRECT_EMAIL_ADRESS = "vasya@gmail.com";
	private static final long PERSON_ID = 123000l;
	private static final String CAR_NUMBER = "123-01-002";
	private static final String CAR_NUMBER_2 = "123-01-003";
	private static final String WRONG_CAR_NUMBER = "1-01-2";
	private static final String PERSON_NOT_FOUND_MESSAGE = "person not found message";
	private static final String PERSON_ALREADY_EXISTS = "person is already exists";
	private static final String CAR_NOT_FOUND_MESSAGE = "car not found";
	private static final String CAR_ALREADY_EXISTS = "car is already exists";
	private static final long WRONG_PERSON_ID = 123l;
	private static final String WRONG_PERSON_ID_TYPE = "abc";
	private static final String MODEL_NAME = "model";
	private static final String MODEL_NAME_2 = "model123";
	private static final int MODEL_YEAR = 1995;
	private static final String CAR_COLOR = "red";
	private static final CarState CAR_STATE = CarState.GOOD;
	private static final CarState CAR_STATE2 = CarState.NEW;
	private static final int CAR_KILOMETERS = 1000;
	private static final String TRADE_DEAL_DATE = "2023-12-12";
	
	@MockBean
	CarsService carsService;
	@Autowired
	MockMvc mockMvc;
	CarDto carDto = new CarDto(CAR_NUMBER, MODEL_NAME, MODEL_YEAR, PERSON_ID, CAR_COLOR, CAR_KILOMETERS, CAR_STATE);
	CarDto carDto1 = new CarDto(CAR_NUMBER_2, MODEL_NAME_2, MODEL_YEAR, 123001l, "white", 0, CAR_STATE2);
	CarDto carMissingFields = new CarDto(null, null, 0, null, null, 0, null);
	
	@Autowired
	ObjectMapper mapper;
	private PersonDto personDto = new PersonDto(PERSON_ID, "Vasya", CORRECT_BIRTH_DATE, CORRECT_EMAIL_ADRESS);
	PersonDto personDtoUpdated = new PersonDto(PERSON_ID, "Vasya", CORRECT_BIRTH_DATE, "vasya@tel-ran.com");
	PersonDto personWrongEmail = new PersonDto(PERSON_ID, "Vasya", CORRECT_BIRTH_DATE, WRONG_EMAIL_ADRESS);
	PersonDto personNoId = new PersonDto(null, "Vasya", CORRECT_BIRTH_DATE, CORRECT_EMAIL_ADRESS);
	PersonDto personWrongId = new PersonDto(111111111111l, "Vasya", CORRECT_BIRTH_DATE, CORRECT_EMAIL_ADRESS);
	PersonDto personWrongBirthDate = new PersonDto(PERSON_ID, "Vasya", WRONG_BIRTH_DATE, "vasya@tel-ran.com");
	PersonDto personNoBirthDate = new PersonDto(PERSON_ID, "Vasya", null, "vasya@tel-ran.com");
	PersonDto personMissingFields = new PersonDto(null, null, null, null);
	PersonDtoIdString personDtoWrongIdType = new PersonDtoIdString(WRONG_PERSON_ID_TYPE, "Vasya", CORRECT_BIRTH_DATE, CORRECT_EMAIL_ADRESS);
	TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER, PERSON_ID, TRADE_DEAL_DATE);
	TradeDealDto tradeDealWrongCarNumber = new TradeDealDto(WRONG_CAR_NUMBER, PERSON_ID, TRADE_DEAL_DATE);
	TradeDealDto tradeDealWrongPersonId = new TradeDealDto(CAR_NUMBER, WRONG_PERSON_ID, TRADE_DEAL_DATE);
	private String[] expectedCarMissingFieldsMessage = {
			MISSING_CAR_MODEL_MESSAGE,
			MISSING_CAR_NUMBER_MESSAGE
	};
	private String[] expectedPersonMissingFieldsMessage = {
			MISSING_BIRTH_DATE_MESSAGE,
			MISSING_PERSON_EMAIL,
			MISSING_PERSON_ID_MESSAGE,
			MISSING_PERSON_NAME_MESSAGE
	};
	

	@Test
	void testAddCar() throws Exception {
		when(carsService.addCar(carDto)).thenReturn(carDto);
		String jsonCarDto = mapper.writeValueAsString(carDto); //conversion from carDto object to string JSON
		String actualJSON = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isOk()).andReturn().getResponse()
		.getContentAsString();
		assertEquals(jsonCarDto, actualJSON );
		
	}

	@Test
	void testAddPerson() throws Exception {
		when(carsService.addPerson(personDto)).thenReturn(personDto);
		String jsonPersonDto = mapper.writeValueAsString(personDto);
		String actualJSON = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonPersonDto, actualJSON);
	}

	@Test
	void testUpdatePerson() throws Exception {
		when(carsService.updatePerson(personDtoUpdated)).thenReturn(personDtoUpdated);
		String jsonPersonDto = mapper.writeValueAsString(personDtoUpdated);
		String actualJSON = mockMvc.perform(put("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonPersonDto, actualJSON);
	}

	@Test
	void testPurchase() throws Exception {
		when(carsService.purchase(tradeDeal)).thenReturn(tradeDeal);
		String jsonTradeDeal = mapper.writeValueAsString(tradeDeal);
		String actualJSON = mockMvc.perform(put("http://localhost:8080/cars/trade").contentType(MediaType.APPLICATION_JSON)
				.content(jsonTradeDeal)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonTradeDeal, actualJSON);
	}

	@Test
	void testDeletePerson() throws Exception {
		when(carsService.deletePerson(PERSON_ID)).thenReturn(personDto);
		String jsonExpected = mapper.writeValueAsString(personDto);
		String actualJSON = mockMvc.perform(delete("http://localhost:8080/cars/person/" + PERSON_ID))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJSON);
	}

	@Test
	void testDeleteCar() throws Exception {
		when(carsService.deleteCar(CAR_NUMBER)).thenReturn(carDto);
		String jsonExpected = mapper.writeValueAsString(carDto);
		String actualJSON = mockMvc.perform(delete("http://localhost:8080/cars/" + CAR_NUMBER))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJSON);
	}

	@Test
	void testGetOwnerCars() throws Exception {
		
		List<CarDto> expectedCars = Arrays.asList(new CarDto("ABC123", "Toyota", 2020, null, CAR_COLOR, 1000, CAR_STATE), 
				new CarDto("XYZ789", "Honda", 2021, null, CAR_COLOR, 1000, CAR_STATE));
		String jsonExpected = mapper.writeValueAsString(expectedCars);
		when(carsService.getOwnerCars(PERSON_ID)).thenReturn(expectedCars);
		String actualJSON = mockMvc.perform(get("http://localhost:8080/cars/person/" + PERSON_ID))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJSON);
	}

	@Test
	void testGetCarOwner() throws Exception {
		when(carsService.getCarOwner(CAR_NUMBER)).thenReturn(personDto);
		String jsonExpected = mapper.writeValueAsString(personDto);
		String actualJSON = mockMvc.perform(get("http://localhost:8080/cars/" + CAR_NUMBER))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJSON);	
	}
	
	@Test
	void testDeletePersonNotFound() throws Exception {
		when(carsService.deletePerson(PERSON_ID)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		String actualJSON = mockMvc.perform(delete("http://localhost:8080/cars/person/" + PERSON_ID))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, actualJSON);
	}
	@Test
	void testDeleteCarNotFound() throws Exception {
		when(carsService.deleteCar(CAR_NUMBER)).thenThrow(new NotFoundException(CAR_NOT_FOUND_MESSAGE));
		String actualJSON = mockMvc.perform(delete("http://localhost:8080/cars/" + CAR_NUMBER))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(CAR_NOT_FOUND_MESSAGE, actualJSON);
	}
	@Test
	void testAddPersonAlreadyExists() throws Exception{
		when(carsService.addPerson(personDto)).thenThrow(new IllegalStateException(PERSON_ALREADY_EXISTS));
		String jsonPersonDto = mapper.writeValueAsString(personDto);
		String actualJSON = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_ALREADY_EXISTS, actualJSON);
	}
	@Test
	void testAddCarAlreadyExists() throws Exception{
		when(carsService.addCar(carDto)).thenThrow(new IllegalStateException(CAR_ALREADY_EXISTS));
		String jsonPersonDto = mapper.writeValueAsString(carDto);
		String actualJSON = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(CAR_ALREADY_EXISTS, actualJSON);
	}
	@Test
	void testUpdatePersonNotFound() throws Exception {
		when(carsService.updatePerson(personDtoUpdated)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		String jsonPersonDto = mapper.writeValueAsString(personDtoUpdated);
		String actualJSON = mockMvc.perform(put("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, actualJSON);
	}
	@Test
	void testPurchaseCarNotFound() throws Exception{
		testPurchaseNotFound(CAR_NOT_FOUND_MESSAGE);
	}
	@Test
	void testPurchasePersonNotFound() throws Exception{
		testPurchaseNotFound(PERSON_NOT_FOUND_MESSAGE);
	}

	private void testPurchaseNotFound(String message)
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(carsService.purchase(tradeDeal)).thenThrow(new NotFoundException(message));
		String jsonTradeDeal = mapper.writeValueAsString(tradeDeal);
		String response = mockMvc.perform(put("http://localhost:8080/cars/trade")
				.contentType(MediaType.APPLICATION_JSON).content(jsonTradeDeal))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(message, response);
	}
	@Test
	void testGetOwnerCarsPersonNotFound() throws Exception {
		
		when(carsService.getOwnerCars(PERSON_ID)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		String response = mockMvc.perform(get("http://localhost:8080/cars/person/" + PERSON_ID))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, response);
	}

	@Test
	void testGetCarOwnerCarNotFound() throws Exception{
		when(carsService.getCarOwner(CAR_NUMBER)).thenThrow(new NotFoundException(CAR_NOT_FOUND_MESSAGE));
		String response = mockMvc.perform(get("http://localhost:8080/cars/" + CAR_NUMBER)).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(CAR_NOT_FOUND_MESSAGE, response);
	}
	
	/******************************************************************************************/
	/* Alternative flows - Validation exceptions handling *************************/
	@Test
	void addPersonWrongEmailTest() throws Exception {
		wrongPersonDataRequest(personWrongEmail, WRONG_EMAIL_FORMAT);
	}
	@Test
	void addPersonWrongBirthDateTest() throws Exception {
		wrongPersonDataRequest(personWrongBirthDate, WRONG_DATE_FORMAT);
	}
	@Test
	void addPersonWrongIdTest() throws Exception {
		wrongPersonDataRequest(personWrongId, WRONG_MAX_PERSON_ID_VALUE);
	}
	@Test
	void addPersonWrongIdTypeTest() throws Exception {
		wrongPersonDataRequest(personDtoWrongIdType, CarsExceptionsController.JSON_TYPE_MISMATCH_MESSAGE);
	}
	@Test
	void purchaseWrongCarNumberTest() throws Exception {
		purchaseWrongData(tradeDealWrongCarNumber, WRONG_CAR_NUMBER_MESSAGE);
	}
	private void purchaseWrongData(TradeDealDto tradeDeal, String expectedMessage) throws Exception {
		String jsonData = mapper.writeValueAsString(tradeDeal);
		String response = mockMvc.perform(put("http://localhost:8080/cars/trade").contentType(MediaType.APPLICATION_JSON)
		.content(jsonData)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedMessage, response);
		
	}

	@Test
	void purchaseWrongPersonIdTest() throws Exception {
		purchaseWrongData(tradeDealWrongPersonId, WRONG_MIN_PERSON_ID_VALUE);
	}
	
	@Test
	void deletePersonWrongIdTest() throws Exception {
		String actualJSON = mockMvc.perform(delete("http://localhost:8080/cars/person/" + WRONG_PERSON_ID))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(WRONG_MIN_PERSON_ID_VALUE, actualJSON);
	}

	private void wrongPersonDataRequest(Object objectWrongData, String expectedMessage) throws Exception {
		String jsonData = mapper.writeValueAsString(objectWrongData);
		String response = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
		.content(jsonData)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedMessage, response);
	}
	@Test
	void addPersonMissingFieldsTest() throws Exception {
		String jsonPersonDto = mapper.writeValueAsString(personMissingFields);
		String response = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
		.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		allFieldsMissingTest(expectedPersonMissingFieldsMessage, response);
	}
	private void allFieldsMissingTest(String[] expectedMessage, String response) {
		Arrays.sort(expectedMessage);
		String[] actualMessage = response.split(";");
		Arrays.sort(actualMessage);
		assertArrayEquals(expectedMessage, actualMessage);
		
	}

	@Test
	void addCarMissingFieldsTest() throws Exception {
		String jsonPersonDto = mapper.writeValueAsString(carMissingFields);
		String response = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
		.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		allFieldsMissingTest(expectedCarMissingFieldsMessage, response);
	}
	@Test
	void testGetOwnerCarsMismatch() throws Exception{
		String response = mockMvc.perform(get("http://localhost:8080/cars/person/" + WRONG_PERSON_ID_TYPE))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(CarsExceptionsController.TYPE_MISMATCH_MESSAGE, response);
	}
	

}

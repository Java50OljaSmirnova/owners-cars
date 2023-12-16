package telran.cars;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.*;
import telran.cars.exceptions.*;
import telran.cars.exceptions.IllegalStateException;
import telran.cars.service.CarsService;

@WebMvcTest
class CarsControllerTest {
	private static final long PERSON_ID = 123l;
	private static final String CAR_NUMBER = "123-01-002";
	private static final String PERSON_NOT_FOUND_MESSAGE = "person not found message";
	private static final String PERSON_ALREADY_EXISTS = "person is already exists";
	private static final String CAR_NOT_FOUND_MESSAGE = "car not found message";
	private static final String CAR_ALREADY_EXISTS = "car is already exists";
	@MockBean
	CarsService carsService;
	@Autowired
	MockMvc mockMvc;
	CarDto carDto = new CarDto(CAR_NUMBER, "model");
	CarDto carDto1 = new CarDto("car122", "model123");
	@Autowired
	ObjectMapper mapper;
	private PersonDto personDto = new PersonDto(PERSON_ID, "Vasya", "1999-12-12", "vasya@gmail.com");
	PersonDto personDtoUpdated = new PersonDto(PERSON_ID, "Vasya", "1999-12-12", "vasya@tel-ran.com");
	PersonDto personWromgEmail = new PersonDto(PERSON_ID, "Vasya", "1999-12-12", "kuku");
	TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER, PERSON_ID);

	@Test
	void testAddCar() throws Exception {
		when(carsService.addCar(carDto)).thenReturn(carDto);
		String jsonCarDto = mapper.writeValueAsString(carDto);
		String actualJSON = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonCarDto, actualJSON);
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
		
		List<CarDto> expectedCars = Arrays.asList(new CarDto("ABC123", "Toyota"), new CarDto("XYZ789", "Honda"));
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

}

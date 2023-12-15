package telran.cars;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.*;
import telran.cars.service.CarsService;

@WebMvcTest
class CarsControllerTest {
	@MockBean
	CarsService carsService;
	@Autowired
	MockMvc mockMvc;
	CarDto carDto = new CarDto("car", "model");
	PersonDto personDto = new PersonDto(124, "name", "12-12-1999", "email@.com");
	@Autowired
	ObjectMapper mapper;

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
	void testUpdatePerson() {
		//TODO
	}

	@Test
	void testPurchase() {
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
	void testGetOwnerCars() {
		//TODO
	}

	@Test
	void testGetCarOwner() {
		//TODO
	}

}

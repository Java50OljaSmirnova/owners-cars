package telran.cars.service.model;

import jakarta.persistence.*;
import lombok.Getter;
import telran.cars.dto.CarState;
@Entity
@Getter
@Table(name = "car")
public class Car {
	@Id
	String number;
	@ManyToOne
	@JoinColumns({@JoinColumn(name="model_name", nullable = false), @JoinColumn(name="model_year", nullable = false)})
	Model model;
	@ManyToOne
	@JoinColumn(name="owner_id", nullable = true)
	CarOwner carOwner;
	String color;
	int kilometers;
	@Enumerated(EnumType.STRING) // value in the table will be a string(by default a number)
	CarState state;
	
	

}

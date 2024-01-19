package telran.cars.service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import telran.cars.dto.CarDto;
import telran.cars.dto.CarState;
@Entity
@Getter
@Table(name = "cars")
public class Car {
	@Id
	@Column(name="car_number")
	String number;
	@ManyToOne
	@JoinColumns({@JoinColumn(name="model_name", nullable = false), @JoinColumn(name="model_year", nullable = false)})
	@Setter
	Model model;
	@ManyToOne
	@JoinColumn(name="owner_id", nullable = true)
	@Setter
	CarOwner carOwner;
	String color;
	@Setter
	int kilometers;
	@Enumerated(EnumType.STRING) // value in the table will be a string(by default a number)
	@Column(name="car_state")
	CarState state;
	public Car(String number, String color, int kilometers, CarState state) {
		super();
		this.number = number;
		this.color = color;
		this.kilometers = kilometers;
		this.state = state;
	}
	public static Car of(CarDto carDto) {
		
		return new Car(carDto.number(), carDto.color(), carDto.kilometrs(), carDto.state());
	}
	public CarDto build() {
		return new CarDto(number, model.modelYear.getName(), model.modelYear.getYear(), color, kilometers, state);
	}

}

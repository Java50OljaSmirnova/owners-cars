package telran.cars.service.model;

import jakarta.persistence.*;
import lombok.Getter;
import telran.cars.dto.CarDto;
import telran.cars.dto.CarState;
@Entity
@Getter
@Table(name = "cars")
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
	
	public static Car of(CarDto carDto) {
		Car car = new Car();
		car.number = carDto.number();
		car.model = new Model();
		car.model.modelYear = new ModelYear(carDto.model(), carDto.year());
		car.carOwner.id = carDto.id();
		car.color = carDto.color();
		car.kilometers = carDto.kilometrs();
		car.state = carDto.state();
		return car;	
	}
	public CarDto build(Car car) {
		return new CarDto(car.number, car.model.modelYear.getName(), car.model.modelYear.getYear(), 
				car.carOwner.id, car.color, car.kilometers, car.state);
	}

}

package telran.cars.service.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Setter;
import telran.cars.dto.TradeDealDto;

@Entity
@Table(name = "trade_deals")
public class TradeDeal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@ManyToOne
	@JoinColumn(name="car_number", nullable = false)
	@Setter
	Car car;
	@ManyToOne
	@JoinColumn(name="owner_id")
	@Setter
	CarOwner carOwner;
	@Temporal(TemporalType.DATE)
	@Setter
	LocalDate date;
	
	public static TradeDeal of(TradeDealDto tradeDealDto) {
		TradeDeal tradeDeal = new TradeDeal();
		tradeDeal.car.number = tradeDealDto.carNumber();
		tradeDeal.carOwner.id = tradeDealDto.personId();
		tradeDeal.date = LocalDate.parse(tradeDealDto.date());
		return tradeDeal;
	}
	public TradeDealDto build(TradeDeal tradeDeal) {
		
		return new TradeDealDto(tradeDeal.car.number, tradeDeal.carOwner.id, tradeDeal.date.toString());
	}

}

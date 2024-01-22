package telran.cars.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.EnginePowerCapacity;
import telran.cars.service.model.*;

public interface CarRepo extends JpaRepository<Car, String> {
	
	List<Car> findByCarOwnerId(long id);
	@Query(value="select min(m.engine_power) as enginePower, min(m.engine_capacity) as engineCapacity "
			+ "from models m join cars c on m.model_name = c.model_name and m.model_year = c.model_year "
			+ "join car_owners co on c.owner_id = co.id "
			+ "where datediff('YEAR', birth_date, current_date()) between :ageFrom and :ageTo ", nativeQuery=true)
	EnginePowerCapacity findMinEnginePowerCapacityByOwnerAges(int ageFrom, int ageTo);
}

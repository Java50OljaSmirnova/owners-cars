package telran.cars.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.ModelNameAmount;
import telran.cars.service.model.*;

public interface ModelRepo extends JpaRepository<Model, ModelYear> {
	@Query(value = "select model_name from cars join trade_deals td"
			+ " on cars.car_number=td.car_number group by model_name "
			+ " having count(*) = (select max(count) from "
			+ "(select count(*) as count from cars join trade_deals "
			+ " on cars.car_number = trade_deals.car_number group by model_name)) ", nativeQuery = true)
	List<String> findMostSoldModelNames();
	@Query(value = "select c.model_name as modelName, count(*) as amount "
			+ "from cars c group by c.model_name order by count(*) desc limit :nModels", nativeQuery = true)
	List<ModelNameAmount> findMostPopularModelNames(int nModels);
	@Query(value = "select m.model_name as modelName, count(*) as amount "
	        + "from models m join cars c on m.model_name = c.model_name join car_owners co on c.owner_id = co.id "
	        + "where datediff('YEAR', birth_date, current_date()) between :ageFrom and :ageTo "
	        + "group by m.model_name order by count(*) desc limit :nModels", nativeQuery = true)
	List<ModelNameAmount> findMostPopularModelNameByOwnerAges(int nModels, int ageFrom, int ageTo);
	@Query(value="select color from cars c "
			+ "join models m on c.model_name = m.model_name "
			+ "where c.model_name= :model " 
			+ "group by color "
			+ "order by count(color) desc limit 1" , nativeQuery=true)
	String findOneMostPopularColorModel(String model);

}

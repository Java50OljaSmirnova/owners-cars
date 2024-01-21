package telran.cars.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.ModelNameAmount;
import telran.cars.service.model.*;

public interface ModelRepo extends JpaRepository<Model, ModelYear> {
	@Query(value="selct model_name from cars c join trade_deals td "
			+ "on c.car_number=td.car_number"
			+ "group by model_name "
			+ "having count(*) = (select max(count) from "
			+ "(select count(*) as count from c join td "
			+ "on c.car_number=td.car_number)", nativeQuery=true)
	List<String> findMostSoldModelNames();
	@Query(value="select c.model_name as model_name, count(*) as amount "
			+ "from cars c  group by c.model_name order by count(*) desc limit :nModels", nativeQuery=true)
	List<ModelNameAmount> findMostPopularModelNames(int nModels);

}

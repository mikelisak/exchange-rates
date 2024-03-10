package task.exchangerates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.exchangerates.model.entity.Rate;

//unused at this moment - it will be whem we decide to add entries to db
public interface RateRepository extends JpaRepository<Rate, Long> {
}

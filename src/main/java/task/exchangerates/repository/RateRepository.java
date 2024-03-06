package task.exchangerates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.exchangerates.model.entity.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {
}

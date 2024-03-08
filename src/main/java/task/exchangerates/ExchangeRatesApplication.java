package task.exchangerates;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import task.exchangerates.model.entity.Rate;
import task.exchangerates.service.NbpApiService;

import java.util.List;

@SpringBootApplication(exclude =  {DataSourceAutoConfiguration.class })
@EnableCaching
@EnableScheduling
public class ExchangeRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRatesApplication.class, args);
    }

    @Bean
    CommandLineRunner run(NbpApiService nbpApiService){
        return args -> {

            List<Rate> rateList = nbpApiService.getRatesForToday();
            List<Rate> rateList2 = nbpApiService.getRatesForToday();
            List<Rate> rateList3 = nbpApiService.getRatesForToday();

            rateList.forEach(e -> System.out.println(e.toString()));
        };
    }
}

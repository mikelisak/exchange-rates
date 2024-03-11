package task.exchangerates;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = ExchangeRatesApplication.class)
class RateControllerIT {
    @Autowired
    private MockMvc postman;

    @Test
    void shouldReturnRatesByDate() throws Exception {

        //given
        LocalDate date = LocalDate.of(2024, 3, 8);

        //when
        postman.perform(get("/api/v1/rates?date=" + date))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currency").value("bat (Tajlandia)"))
                .andExpect(jsonPath("$[0].code").value("THB"))
                .andExpect(jsonPath("$[0].mid").value(0.1112))
                .andExpect(jsonPath("$[0].date").value("2024-03-08"));
    }

    @Test
    void shouldReturnRateByCodeAndDate() throws Exception {

        //given
        String code = "USD";
        LocalDate date = LocalDate.of(2024, 3, 8);

        //when
        postman.perform(get("/api/v1/rates/code/" + code + "?date=" + date))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("dolar amerykański"))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.mid").value(3.9392))
                .andExpect(jsonPath("$.date").value("2024-03-08"));
    }

    @Test
    void shouldReturnRateByCodeForToday() throws Exception {

        //given
        String code = "USD";

        //when
        postman.perform(get("/api/v1/rates/code/" + code))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("dolar amerykański"))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.mid").value(3.9392))
                .andExpect(jsonPath("$.date").value("2024-03-08"));
    }
}

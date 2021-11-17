package onedrop.test.demo.models.response;

import lombok.Data;

import java.util.List;

@Data
public class CurrentWeatherAndForecast {
    private String currentConditions;
    private String location; //zip or city name
    private List<Forecast> forecasts;
}

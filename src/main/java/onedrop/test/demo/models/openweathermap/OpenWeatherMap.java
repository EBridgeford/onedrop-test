package onedrop.test.demo.models.openweathermap;

import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherMap {
    private String cod;
    private Integer message;
    private Integer cnt;
    private List<Forecasts> list;
}

package onedrop.test.demo.models.response;

import lombok.Data;

@Data
public class Forecast {
    private String expectedCondition;
    private Double expectedTemperature;
    private String date;
}

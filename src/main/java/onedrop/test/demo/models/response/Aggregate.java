package onedrop.test.demo.models.response;

import lombok.Data;

import java.util.List;

@Data
public class Aggregate {
    private List<String> locations; //zip code or city name
    private Double averageTemp;
    private Double minimumTemp;
    private Double maximumTemp;
    private Double averageHumidity;
}

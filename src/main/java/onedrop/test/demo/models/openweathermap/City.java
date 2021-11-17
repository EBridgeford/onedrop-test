package onedrop.test.demo.models.openweathermap;

import lombok.Data;

@Data
public class City {
    private Integer id;
    private String name;
    private Coord coord;
    private String country;
    private Integer population;
    private Integer timezone;
    private Integer sunrise;
    private Integer sunset;
}

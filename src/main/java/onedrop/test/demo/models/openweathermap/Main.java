package onedrop.test.demo.models.openweathermap;

import lombok.Data;

@Data
public class Main {
    private Double temp;
    private Double feels_like;
    private Double temp_min;
    private Double temp_max;
    private Integer pressure;
    private Integer sea_level;
    private Integer grnd_level;
    private Integer humidity;
    private Double temp_kf;
}

package onedrop.test.demo.models.openweathermap;

import lombok.Data;

@Data
public class Wind {
    private Double speed;
    private Integer deg;
    private Double gust;
}

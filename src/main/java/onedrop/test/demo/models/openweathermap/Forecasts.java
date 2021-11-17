package onedrop.test.demo.models.openweathermap;

import lombok.Data;

import java.util.List;

@Data
public class Forecasts {
    private Long dt;
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private Integer visibility;
    private Integer pop;
    private Sys sys;
    private String dt_txt;
}

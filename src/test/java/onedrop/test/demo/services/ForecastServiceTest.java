package onedrop.test.demo.services;

import onedrop.test.demo.config.OpenWeatherMapConfig;
import onedrop.test.demo.models.openweathermap.Forecasts;
import onedrop.test.demo.models.openweathermap.Main;
import onedrop.test.demo.models.openweathermap.OpenWeatherMap;
import onedrop.test.demo.models.openweathermap.Weather;
import onedrop.test.demo.models.response.Aggregate;
import onedrop.test.demo.models.response.CurrentWeatherAndForecast;
import onedrop.test.demo.models.response.Forecast;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForecastServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OpenWeatherMapConfig openWeatherMapConfig;

    @InjectMocks
    private ForecastService forecastService;

    @Test
    public void testForecastZipCodeHappyPath() {
        Map<String, OpenWeatherMap> data = new HashMap<>();

        List<Forecasts> forecastsList = new ArrayList<>();
        Forecasts forecast = new Forecasts();
        forecast.setDt_txt("TEST DATE TEXT");

        List<Weather> weatherList = new ArrayList<>();
        Weather weather = new Weather();
        weather.setDescription("TEST WEATHER DESCRIPTION");
        weatherList.add(weather);


        Main main = new Main();
        main.setTemp(75.01);

        forecast.setMain(main);
        forecastsList.add(forecast);

        forecast.setWeather(weatherList);

        OpenWeatherMap owm = new OpenWeatherMap();

        owm.setList(forecastsList);



        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(owm));

        CurrentWeatherAndForecast retValue = forecastService.forecast("45011", true);

        Assert.assertTrue(retValue.getForecasts().isEmpty());
    }

    @Test
    public void testForecastZipCodeBadRequest() {
        Map<String, OpenWeatherMap> data = new HashMap<>();

        List<Forecasts> forecastsList = new ArrayList<>();
        Forecasts forecast = new Forecasts();
        forecast.setDt_txt("TEST DATE TEXT");

        List<Weather> weatherList = new ArrayList<>();
        Weather weather = new Weather();
        weather.setDescription("TEST WEATHER DESCRIPTION");
        weatherList.add(weather);


        Main main = new Main();
        main.setTemp(75.01);

        forecast.setMain(main);
        forecastsList.add(forecast);

        forecast.setWeather(weatherList);

        OpenWeatherMap owm = new OpenWeatherMap();

        owm.setList(forecastsList);



        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().body(owm));

        CurrentWeatherAndForecast retValue = forecastService.forecast("45011", true);

        Assert.assertNull(retValue.getCurrentConditions());
        Assert.assertNull(retValue.getForecasts());
    }

    @Test
    public void testAggregateHappyPath() {
        Map<String, OpenWeatherMap> data = new HashMap<>();

        List<Forecasts> forecastsList = new ArrayList<>();
        Forecasts forecast = new Forecasts();
        forecast.setDt_txt("TEST DATE TEXT");

        List<Weather> weatherList = new ArrayList<>();
        Weather weather = new Weather();
        weather.setDescription("TEST WEATHER DESCRIPTION");
        weatherList.add(weather);


        Main main = new Main();
        main.setTemp(75.01);
        main.setHumidity(60);

        forecast.setMain(main);
        forecastsList.add(forecast);

        forecast.setWeather(weatherList);

        OpenWeatherMap owm = new OpenWeatherMap();

        owm.setList(forecastsList);



        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(owm));

        Aggregate retValue = forecastService.aggregate(Arrays.asList("45011"), true);

        Assert.assertEquals(new Double(75.01), retValue.getAverageTemp());
        Assert.assertEquals(new Double(75.01), retValue.getMaximumTemp());
        Assert.assertEquals(new Double(75.01), retValue.getMinimumTemp());
        Assert.assertEquals(new Double(60.0), retValue.getAverageHumidity());
    }

}

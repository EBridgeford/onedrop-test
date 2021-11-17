package onedrop.test.demo.controllers;

import lombok.AllArgsConstructor;
import onedrop.test.demo.models.response.Aggregate;
import onedrop.test.demo.models.response.CurrentWeatherAndForecast;
import onedrop.test.demo.services.ForecastService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ForecastController {

    private ForecastService forecastService;

    @GetMapping("forecast/zipCodes/{zipCode}")
    CurrentWeatherAndForecast currentWeatherAndForecastByZipCode(@PathVariable Integer zipCode) {
        return forecastService.forecast(zipCode.toString(), true);
    }

    @GetMapping("aggregate/zipCodes/{zipCodes}")
    Aggregate aggregateDataByZipCodes(@PathVariable List<Integer> zipCodes) {
        return forecastService.aggregate(zipCodes.stream().map(Object::toString).collect(Collectors.toList()), true);
    }

    @GetMapping("forecast/cityName/{cityName}")
    CurrentWeatherAndForecast currentWeatherAndForecastByCityName(@PathVariable String cityName) {
        return forecastService.forecast(cityName, false);
    }

    @GetMapping("aggregate/cityName/{cityNames}")
    Aggregate aggregateDataByCityName(@PathVariable List<String> cityNames) {
        return forecastService.aggregate(cityNames, false);
    }
}

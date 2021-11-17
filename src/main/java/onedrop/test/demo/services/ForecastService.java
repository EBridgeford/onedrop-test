package onedrop.test.demo.services;

import lombok.AllArgsConstructor;
import onedrop.test.demo.config.OpenWeatherMapConfig;
import onedrop.test.demo.models.openweathermap.Forecasts;
import onedrop.test.demo.models.openweathermap.Main;
import onedrop.test.demo.models.openweathermap.OpenWeatherMap;
import onedrop.test.demo.models.openweathermap.Weather;
import onedrop.test.demo.models.response.Aggregate;
import onedrop.test.demo.models.response.CurrentWeatherAndForecast;
import onedrop.test.demo.models.response.Forecast;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class ForecastService {

    private RestTemplate restTemplate;
    private OpenWeatherMapConfig openWeatherMapConfig;

    public CurrentWeatherAndForecast forecast(String searchTerm, boolean byZipCode) {
        Map<String, OpenWeatherMap> data = getData(List.of(searchTerm), byZipCode);

        CurrentWeatherAndForecast retValue = new CurrentWeatherAndForecast();
        retValue.setLocation(searchTerm);

        if (!data.containsKey(searchTerm)) {
            return retValue;
        }

        OpenWeatherMap owm = data.get(searchTerm);

        if (owm.getList().isEmpty()) {
            return retValue;
        }

        boolean runOnce = false;
        List<Forecast> forecasts = new ArrayList<>();
        for (Forecasts forecast: owm.getList()) {
            if (!runOnce) {
                retValue.setCurrentConditions(forecast.getWeather().get(0).getDescription());
                runOnce = true;
            } else {
                Forecast tmpForecast = new Forecast();
                tmpForecast.setDate(forecast.getDt_txt());
                tmpForecast.setExpectedTemperature(forecast.getMain().getTemp());
                tmpForecast.setExpectedCondition(forecast.getWeather().get(0).getDescription());
                forecasts.add(tmpForecast);
            }
        }

        retValue.setForecasts(forecasts);
        return retValue;

    }

    public Aggregate aggregate(List<String> searchTerms, boolean byZipCode) {
        Map<String, OpenWeatherMap> data = getData(searchTerms, byZipCode);
        Aggregate retValue = new Aggregate();
        retValue.setLocations(searchTerms);
        retValue.setMaximumTemp(Double.MIN_VALUE);
        retValue.setMinimumTemp(Double.MAX_VALUE);


        OptionalDouble averageHumidity = data.values().stream()
                .map(OpenWeatherMap::getList)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElse(new ArrayList<>())
                .stream()
                .map(Forecasts::getMain)
                .collect(Collectors.toList())
                .stream()
                .mapToInt(Main::getHumidity)
                .average();

        if (averageHumidity.isPresent()) {
            retValue.setAverageHumidity(averageHumidity.getAsDouble());
        }

        OptionalDouble averageTemperature = data.values().stream()
                .map(OpenWeatherMap::getList)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElse(new ArrayList<>())
                .stream()
                .map(Forecasts::getMain)
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(Main::getTemp)
                .average();

        if (averageTemperature.isPresent()) {
            retValue.setAverageTemp(averageTemperature.getAsDouble());
        }

        OptionalDouble maxTemp = data.values().stream()
                .map(OpenWeatherMap::getList)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElse(new ArrayList<>())
                .stream()
                .map(Forecasts::getMain)
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(Main::getTemp)
                .max();

        if (maxTemp.isPresent()) {
            retValue.setMaximumTemp(maxTemp.getAsDouble());
        }

        OptionalDouble minTemp = data.values().stream()
                .map(OpenWeatherMap::getList)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElse(new ArrayList<>())
                .stream()
                .map(Forecasts::getMain)
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(Main::getTemp)
                .min();

        if (minTemp.isPresent()) {
            retValue.setMinimumTemp(minTemp.getAsDouble());
        }

        return retValue;
    }

    @Cacheable("OpenWeatherMap")
    private Map<String, OpenWeatherMap> getData(List<String> searchTerms, boolean byZipCode) {
        Map<String, OpenWeatherMap> retValue = new HashMap<>();

        for (String searchTerm : searchTerms) {
            String finalUrl;
            if (byZipCode) {
                finalUrl = openWeatherMapConfig.getBaseUrl()+openWeatherMapConfig.getQueryZipCode()+searchTerm;
            } else {
                finalUrl = openWeatherMapConfig.getBaseUrl()+openWeatherMapConfig.getQueryCityName()+searchTerm;
            }

            ResponseEntity<OpenWeatherMap> owm = restTemplate.getForEntity(finalUrl, OpenWeatherMap.class);

            if (owm.getStatusCode() == HttpStatus.OK) {
                retValue.put(searchTerm, owm.getBody());
            }
        }

        return retValue;
    }


}

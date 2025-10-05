package com.ddevuss.weather.oracle.location.client.internal;

import com.ddevuss.weather.oracle.common.client.config.OpenWeatherApiProperties;
import com.ddevuss.weather.oracle.common.util.OpenWeatherUrlBuilder;
import com.ddevuss.weather.oracle.location.client.LocationSearchClient;
import com.ddevuss.weather.oracle.location.dto.LocationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@AllArgsConstructor
@Service
class LocationSearchClientImp implements LocationSearchClient {

    private final RestClient openWeatherRestClient;
    private final OpenWeatherApiProperties apiProperties;

    @Override
    public List<LocationDto> searchLocationByName(String locationName) {
        locationName = locationName.trim();
        String uri = OpenWeatherUrlBuilder.buildUrlForGeoApi(locationName, apiProperties.key());

        var locations = openWeatherRestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(LocationDto[].class);

        return LocationDeduplicator.deduplicate(locations);
    }
}

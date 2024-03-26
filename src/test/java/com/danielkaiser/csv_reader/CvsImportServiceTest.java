package com.danielkaiser.csv_reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.danielkaiser.csv_reader.domain.Location;
import com.danielkaiser.csv_reader.repository.LocationRepository;

import lombok.val;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CvsImportServiceTest {

    @Autowired
    private CsvImportService importService;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void shouldLoadCsvFile() {
        val locations = importService.loadObjectList(Location.class, "postcode.csv");
        locationRepository.saveAll(locations);
        locationRepository.flush();

        val loadedLocations = locationRepository.findAll();
        assertThat(loadedLocations).hasSize(22898); // rows in our file
    }

    @Test
    void shouldThrowErrorOnEmptyFile() {
        val exception = assertThrows(IllegalStateException.class, () -> importService.loadObjectList(Location.class, "empty.csv"));

        String expectedMessage = "Error occurred while loading object list from stream.";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void shouldThrowErrorOnBrokenFile() {
        val exception = assertThrows(IllegalStateException.class, () -> importService.loadObjectList(Location.class, "broken.csv"));

        String expectedMessage = "Error occurred while loading object list from file";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }
}

package com.danielkaiser.csv_reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.danielkaiser.csv_reader.domain.Location;
import com.danielkaiser.csv_reader.repository.LocationRepository;

import lombok.val;

@SpringBootTest
public class CvsImportUtilTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void shouldLoadCsvFile() {
        final val locations = CsvImportUtil.loadObjectList(Location.class, "postcode.csv");
        locationRepository.saveAll(locations);
        locationRepository.flush();
    }
}

package com.danielkaiser.csv_reader;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import com.danielkaiser.csv_reader.util.ReplacingInputStream;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Service to read CSV files into the application
 */
@Service
@Log4j2
public class CsvImportService {

    public <T> List<T> loadObjectList(Class<T> type, final String fileName) {
        log.debug("Loading file {}", fileName);

        // replace the two chars separator ', ' with ',' as otherwise the quotes would still be read and persisted
        try (InputStream inputStream = new ClassPathResource(fileName, type.getClassLoader()).getInputStream();
                        FilterInputStream filterInputStream = new ReplacingInputStream(inputStream, ", ".getBytes(), ",".getBytes())) {

            return loadObjectList(type, filterInputStream);

        } catch (final IOException e) {
            log.error(String.format("Error occurred while loading object list from file %s", fileName), e);
            throw new IllegalStateException("Error occurred while loading object list from file " + fileName, e);
        }
    }

    private <T> List<T> loadObjectList(final Class<T> type, final InputStream inputStream) {
        final CsvSchema schema = CsvSchema.emptySchema().withHeader().withQuoteChar('"').withColumnSeparator(',');
        final ObjectReader objectReader = new CsvMapper().readerFor(type).with(schema);
        try (MappingIterator<T> values = objectReader.readValues(inputStream)) {
            return values.readAll();
        } catch (IOException e) {
            log.error("Error occurred while loading object list from stream.", e);
            throw new IllegalStateException("Error occurred while loading object list from stream.", e);
        }
    }
}

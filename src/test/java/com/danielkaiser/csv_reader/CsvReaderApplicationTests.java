package com.danielkaiser.csv_reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CsvReaderApplicationTests {

	@Autowired
	private CsvImportService importService;

	@Test
	void contextLoads() {
		assertThat(importService).isNotNull();
	}

}

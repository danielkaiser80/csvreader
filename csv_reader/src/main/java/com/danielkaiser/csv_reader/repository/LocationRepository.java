package com.danielkaiser.csv_reader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.danielkaiser.csv_reader.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

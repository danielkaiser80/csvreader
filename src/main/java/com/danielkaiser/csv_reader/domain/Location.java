package com.danielkaiser.csv_reader.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Table
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Nullable
    private Long id;

    @JsonProperty("REGION1")
    private String state; // Bundesland

    @JsonProperty("REGION3")
    private String county; // Landkreis

    @JsonProperty("REGION4")
    private String city; // Stadt

    @JsonProperty("POSTLEITZAHL")
    private String postCode;

    @JsonProperty("ORT")
    private String locality; // Ort/Ortsteil

}

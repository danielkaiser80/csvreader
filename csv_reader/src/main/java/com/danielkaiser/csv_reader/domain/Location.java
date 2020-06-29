package com.danielkaiser.csv_reader.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Table
@Entity
@Data
public class Location {

    @Id
    @GeneratedValue
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

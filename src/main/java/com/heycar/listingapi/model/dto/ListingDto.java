package com.heycar.listingapi.model.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingDto {
    @NotBlank(message = "code cannot be empty")
    @CsvBindByName(column = "code")
    private String code;
    @NotBlank(message = "make cannot be empty")
    @CsvBindByName(column = "make")
    private String make;
    @NotBlank(message = "model cannot be empty")
    @CsvBindByName(column = "model")
    private String model;
    @NotNull(message = "kw cannot be empty")
    @CsvBindByName(column = "kw")
    private Long kW;
    @Positive(message = "invalid year")
    @Digits(fraction = 0, integer = 4, message = "invalid year")
    @CsvBindByName(column = "year")
    private Integer year;
    @NotBlank(message = "color cannot be empty")
    @CsvBindByName(column = "color")
    private String color;
    @NotNull(message = "price cannot be empty")
    @CsvBindByName(column = "price")
    private Double price;
}

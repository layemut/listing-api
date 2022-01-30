package com.heycar.listingapi.model.entity;

import com.heycar.listingapi.model.dto.ListingDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LISTING")
@Getter
@Setter
@NoArgsConstructor
public class Listing {

    @EmbeddedId
    private ListingId id;
    @Column(name = "MAKE")
    private String make;
    @Column(name = "MODEL")
    private String model;
    @Column(name = "KW")
    private Long kW;
    @Column(name = "YEAR")
    private Integer year;
    @Column(name = "COLOR")
    private String color;
    @Column(name = "PRICE")
    private Double price;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListingId implements Serializable {
        @Column(name = "DEALER_ID")
        private Long dealerId;
        @Column(name = "CODE")
        private String code;
    }

    public Listing(Long dealerId, ListingDto listingDto) {
        this.id = new ListingId(dealerId, listingDto.getCode());
        this.make = listingDto.getMake();
        this.model = listingDto.getModel();
        this.kW = listingDto.getKW();
        this.year = listingDto.getYear();
        this.color = listingDto.getColor();
        this.price = listingDto.getPrice();
    }

    public ListingDto toDto() {
        return ListingDto.builder()
                .code(this.id.code)
                .make(this.make)
                .model(this.model)
                .kW(this.kW)
                .year(this.year)
                .color(this.color)
                .price(this.price)
                .build();
    }
}

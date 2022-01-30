package com.heycar.listingapi.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "DEALER")
@Getter
@Setter
public class Dealer {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "dealer_id_seq",
            sequenceName = "dealerIdSeq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "dealerIdSeq")
    private Long id;

    @Column(name = "NAME")
    private String name;
}

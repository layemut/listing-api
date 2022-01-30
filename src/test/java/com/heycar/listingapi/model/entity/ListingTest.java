package com.heycar.listingapi.model.entity;

import com.heycar.listingapi.model.dto.ListingDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ListingTest {

    @Test
    public void shouldMapDtoToEntity() {
        var dealerId = 1L;
        var dto = new ListingDto();
        dto.setCode("1");
        dto.setMake("bmw");
        dto.setModel("m3");
        dto.setKW(1L);
        dto.setYear(2000);
        dto.setColor("Red");
        dto.setPrice(10000.0);


        var entity = new Listing(dealerId, dto);

        assertEquals(dealerId, entity.getId().getDealerId().longValue());
        assertEquals(dto.getCode(), entity.getId().getCode());
        assertEquals(dto.getMake(), entity.getMake());
        assertEquals(dto.getModel(), entity.getModel());
        assertEquals(dto.getKW(), entity.getKW());
        assertEquals(dto.getYear(), entity.getYear());
        assertEquals(dto.getColor(), entity.getColor());
        assertEquals(dto.getPrice(), entity.getPrice());
    }

    @Test
    public void shouldMapEntityToDto() {
        var entity = new Listing();
        entity.setId(new Listing.ListingId(1L, "1"));
        entity.setMake("bmw");
        entity.setModel("m3");
        entity.setKW(1L);
        entity.setYear(2000);
        entity.setColor("Red");
        entity.setPrice(10000.0);

        var dto = entity.toDto();

        assertEquals(entity.getId().getCode(), dto.getCode());
        assertEquals(entity.getMake(), dto.getMake());
        assertEquals(entity.getModel(), dto.getModel());
        assertEquals(entity.getKW(), dto.getKW());
        assertEquals(entity.getYear(), dto.getYear());
        assertEquals(entity.getColor(), dto.getColor());
        assertEquals(entity.getPrice(), dto.getPrice());
    }
}

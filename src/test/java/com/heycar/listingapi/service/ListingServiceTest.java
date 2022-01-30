package com.heycar.listingapi.service;

import com.heycar.listingapi.exception.InvalidCSVException;
import com.heycar.listingapi.helper.CSVProviderHelper;
import com.heycar.listingapi.model.dto.ListingDto;
import com.heycar.listingapi.model.entity.Listing;
import com.heycar.listingapi.model.request.SearchListingRequest;
import com.heycar.listingapi.repository.ListingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ListingServiceTest {

    @Autowired
    private ListingService listingService;

    @MockBean
    private ListingRepository listingRepository;

    @SpyBean
    private CSVProviderHelper csvProviderHelper;

    @Test
    public void whenNoError_shouldCallSaveAsListSize() {
        var dealerId = 1L;
        var listings = List.of(
                ListingDto.builder().make("BMW").build(),
                ListingDto.builder().make("AMG").build()
        );

        listingService.upsertListing(dealerId, listings);

        verify(listingRepository, times(2))
                .save(any(Listing.class));
    }

    @Test
    public void whenValidCSV_shouldCallSaveAsCSVSize() throws Exception {
        var dealerId = 2L;

        var validCSVPath = "src/test/resources/valid_listings.csv";

        var valid = new MockMultipartFile("file", new FileInputStream(validCSVPath));

        listingService.upsertListingCSV(dealerId, valid);

        verify(listingRepository, times(5))
                .save(any(Listing.class));
    }

    @Test
    public void whenInvalidCSV_shouldThrowExceptionWhenInvalidCSV() throws Exception {
        var dealerId = 3L;

        var invalidCSVPath = "src/test/resources/invalid_listings.csv";

        var invalid = new MockMultipartFile("file", new FileInputStream(invalidCSVPath));

        assertThrows(InvalidCSVException.class, () -> listingService.upsertListingCSV(dealerId, invalid));
    }


    @Test
    public void whenFoundByDealerId_shouldReturnListingDtoList() {
        var dealerId = 4L;

        when(listingRepository.findAllById_DealerId(dealerId))
                .thenReturn(List.of(mockListing(dealerId), mockListing(dealerId), mockListing(dealerId)));

        var listings = listingService.getListings(dealerId);

        assertFalse(listings.isEmpty());
        assertEquals(3, listings.size());
    }

    @Test
    public void whenNotFoundByDealerId_shouldReturnEmptyList() {
        var dealerId = 5L;

        when(listingRepository.findAllById_DealerId(dealerId))
                .thenReturn(Collections.emptyList());

        var listings = listingService.getListings(dealerId);
        assertTrue(listings.isEmpty());
    }

    @Test
    public void whenFoundBySearch_shouldReturnDealerMappedListingResponse() {
        var request = new SearchListingRequest();
        request.setMake("BMW");
        request.setModel("M3");

        when(listingRepository.findAll(request))
                .thenReturn(List.of(mockListing(1L), mockListing(2L), mockListing(3L), mockListing(2L)));

        var listings = listingService.searchListings(request);

        assertFalse(listings.isEmpty());
        assertEquals(1, listings.stream().filter(l -> l.getDealerId().equals(1L)).findFirst().get().getListings().size());
        assertEquals(2, listings.stream().filter(l -> l.getDealerId().equals(2L)).findFirst().get().getListings().size());
        assertEquals(1, listings.stream().filter(l -> l.getDealerId().equals(3L)).findFirst().get().getListings().size());
    }

    private Listing mockListing(Long dealerId) {
        Listing listing = new Listing();
        listing.setMake("mockMake");
        listing.setModel("mockmodel");
        listing.setId(new Listing.ListingId(dealerId, "mockCode"));
        return listing;
    }
}

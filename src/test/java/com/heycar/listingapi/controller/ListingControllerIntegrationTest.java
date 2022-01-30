package com.heycar.listingapi.controller;

import com.heycar.listingapi.helper.CSVProviderHelper;
import com.heycar.listingapi.model.dto.ListingDto;
import com.heycar.listingapi.model.entity.Listing;
import com.heycar.listingapi.model.request.SearchListingRequest;
import com.heycar.listingapi.repository.ListingRepository;
import com.heycar.listingapi.service.ListingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ListingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ListingService listingService;

    @SpyBean
    private CSVProviderHelper csvProviderHelper;

    @MockBean
    private ListingRepository listingRepository;

    @Test
    public void whenListingFoundByDealer_shouldReturnListingResponse() throws Exception {
        Long dealerId = 1L;
        when(listingService.getListings(dealerId))
                .thenReturn(List.of(ListingDto.builder().make("BMW").build(),
                        ListingDto.builder().make("AMG").build()));

        this.mockMvc.perform(get("/listing/{dealerId}", dealerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("BMW"))
                .andExpect(jsonPath("$[1].make").value("AMG"));
    }

    @Test
    public void whenListingNotFoundByDealer_shouldReturnEmptyArrayResponse() throws Exception {
        Long dealerId = 2L;
        when(listingService.getListings(dealerId)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/listing/{dealerId}", dealerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void whenValidJSONNewListings_shouldReturnHttpOKWithEmptyBody() throws Exception {
        Long dealerId = 3L;

        doNothing().when(listingService).upsertListing(eq(dealerId), anyList());

        this.mockMvc.perform(post("/listing/{dealerId}", dealerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\n" +
                                "    {\n" +
                                "        \"code\": \"1\",\n" +
                                "        \"make\": \"vw\",\n" +
                                "        \"model\": \"polo\",\n" +
                                "        \"year\": 20,\n" +
                                "        \"color\": \"black\",\n" +
                                "        \"price\": 15950.0,\n" +
                                "        \"kw\": 123\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"code\": \"2\",\n" +
                                "        \"make\": \"mercedes\",\n" +
                                "        \"model\": \"a181\",\n" +
                                "        \"year\": 20,\n" +
                                "        \"color\": \"black\",\n" +
                                "        \"price\": 15950.0,\n" +
                                "        \"kw\": 123\n" +
                                "    }\n" +
                                "]"))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    @Test
    public void whenInvalidJSONNewListings_shouldReturnHttpBadRequestWithViolationsResponse() throws Exception {
        Long dealerId = 4L;

        this.mockMvc.perform(post("/listing/{dealerId}", dealerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\n" +
                                "    {\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"code\": \"2\",\n" +
                                "        \"make\": \"mercedes\",\n" +
                                "        \"model\": \"a181\",\n" +
                                "        \"year\": 20,\n" +
                                "        \"color\": \"black\",\n" +
                                "        \"price\": 15950.0,\n" +
                                "        \"kW\": 123\n" +
                                "    }\n" +
                                "]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[?(@.field == \"upsertListing.listings[0].code\")][?(@.message == \"code cannot be empty\")]").exists())
                .andExpect(jsonPath("$.violations[?(@.field == \"upsertListing.listings[0].make\")][?(@.message == \"make cannot be empty\")]").exists())
                .andExpect(jsonPath("$.violations[?(@.field == \"upsertListing.listings[0].model\")][?(@.message == \"model cannot be empty\")]").exists())
                .andExpect(jsonPath("$.violations[?(@.field == \"upsertListing.listings[0].color\")][?(@.message == \"color cannot be empty\")]").exists())
                .andExpect(jsonPath("$.violations[?(@.field == \"upsertListing.listings[0].kW\")][?(@.message == \"kw cannot be empty\")]").exists())
                .andExpect(jsonPath("$.violations[?(@.field == \"upsertListing.listings[0].price\")][?(@.message == \"price cannot be empty\")]").exists());
    }


    @Test
    public void whenValidCSVNewListings_shouldReturnHttpOKWithEmptyBody() throws Exception {
        Long dealerId = 5L;

        String validCSVPath = "src/test/resources/valid_listings.csv";

        MockMultipartFile valid = new MockMultipartFile("file", new FileInputStream(validCSVPath));

        this.mockMvc.perform(multipart("/listing/csv/{dealerId}", dealerId)
                        .file(valid))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    @Test
    public void whenInvalidCSVNewListings_shouldReturnHttpBadRequestWithViolationResponse() throws Exception {
        Long dealerId = 6L;

        String invalidCSVPath = "src/test/resources/invalid_listings.csv";

        MockMultipartFile invalid = new MockMultipartFile("file", new FileInputStream(invalidCSVPath));

        doCallRealMethod().when(csvProviderHelper).getListingsFromCSV(invalid);

        this.mockMvc.perform(multipart("/listing/csv/{dealerId}", dealerId)
                        .file(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[?(@.field == \"file\")][?(@.message == \"Invalid or corrupt csv file\")]").exists());
    }

    @Test
    public void whenSearchFound_shouldReturnHttpOKWithDealerMappedListingResponse() throws Exception {

        SearchListingRequest request = new SearchListingRequest();
        request.setMake("BMW");
        request.setModel("M3");

        when(listingRepository.findAll(request))
                .thenReturn(List.of(mockListing(1L), mockListing(2L), mockListing(1L)));

        this.mockMvc.perform(get("/listing/search")
                        .param("make", request.getMake())
                        .param("model", request.getModel()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].listings.length()", is(2)))
                .andExpect(jsonPath("$[1].listings.length()", is(1)));

    }

    private Listing mockListing(Long dealerId) {
        Listing listing = new Listing();
        listing.setMake("mockMake");
        listing.setModel("mockmodel");
        listing.setId(new Listing.ListingId(dealerId, "mockCode"));
        return listing;
    }
}

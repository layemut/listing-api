package com.heycar.listingapi.controller;

import com.heycar.listingapi.model.dto.ListingDto;
import com.heycar.listingapi.model.request.SearchListingRequest;
import com.heycar.listingapi.model.response.ListingResponse;
import com.heycar.listingapi.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/listing")
public class ListingController {

    private final ListingService listingService;

    @Operation(summary = "Upload new listings for a dealer in JSON array format")
    @PostMapping("/{dealerId}")
    @ApiResponse(responseCode = "201", description = "Successful upload")
    public ResponseEntity upsertListing(@PathVariable Long dealerId, @Valid @NotNull @RequestBody List<ListingDto> listings) {
        listingService.upsertListing(dealerId, listings);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Upload new listings for a dealer in CSV format")
    @PostMapping(value = "/csv/{dealerId}", consumes = {"multipart/form-data"})
    @ApiResponse(responseCode = "201", description = "Successful upload")
    public ResponseEntity upsertListingCSV(@PathVariable Long dealerId, @RequestParam("file") MultipartFile file) {
        listingService.upsertListingCSV(dealerId, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get listings by dealer")
    @GetMapping("/{dealerId}")
    public ResponseEntity<List<ListingDto>> getListings(@PathVariable Long dealerId) {
        var listings = listingService.getListings(dealerId);
        return ResponseEntity.ok(listings);
    }

    @Operation(summary = "Search listings with make, model, year, color")
    @GetMapping("/search")
    public ResponseEntity<List<ListingResponse>> searchListing(@Valid SearchListingRequest request) {
        var listings = listingService.searchListings(request);
        return ResponseEntity.ok(listings);
    }
}

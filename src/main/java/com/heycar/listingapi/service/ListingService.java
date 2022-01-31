package com.heycar.listingapi.service;

import com.heycar.listingapi.helper.CSVProviderHelper;
import com.heycar.listingapi.model.dto.ListingDto;
import com.heycar.listingapi.model.entity.Listing;
import com.heycar.listingapi.model.request.SearchListingRequest;
import com.heycar.listingapi.model.response.ListingResponse;
import com.heycar.listingapi.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final CSVProviderHelper csvProviderHelper;

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "listings", key = "#dealerId"),
                    @CacheEvict(cacheNames = "search_listing", key = "#dealerId"),
            }
    )
    public void upsertListing(Long dealerId, List<ListingDto> listings) {
        listings.stream()
                .map(listingDto -> new Listing(dealerId, listingDto))
                .forEach(listingRepository::save);
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "listings", key = "#dealerId"),
                    @CacheEvict(cacheNames = "search_listing", allEntries = true),
            }
    )
    public void upsertListingCSV(Long dealerId, MultipartFile file) {
        var listings = csvProviderHelper.getListingsFromCSV(file);
        upsertListing(dealerId, listings);
    }

    @Cacheable(cacheNames = "listings", key = "#dealerId")
    public List<ListingDto> getListings(Long dealerId) {
        return listingRepository.findAllById_DealerId(dealerId)
                .stream()
                .map(Listing::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "search_listing")
    public List<ListingResponse> searchListings(SearchListingRequest request) {
        return listingRepository.findAll(request)
                .stream()
                .collect(Collectors.groupingBy(l -> l.getId().getDealerId(),
                        Collectors.mapping(Listing::toDto, Collectors.toList())))
                .entrySet()
                .stream()
                .map(kv -> new ListingResponse(kv.getKey(), kv.getValue()))
                .collect(Collectors.toList());
    }
}

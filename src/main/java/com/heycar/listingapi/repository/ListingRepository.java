package com.heycar.listingapi.repository;

import com.heycar.listingapi.model.entity.Listing;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListingRepository extends CrudRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {
    List<Listing> findAllById_DealerId(Long dealerId);
}

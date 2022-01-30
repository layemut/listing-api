package com.heycar.listingapi.model.response;

import com.heycar.listingapi.model.dto.ListingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingResponse {
    private Long dealerId;
    private List<ListingDto> listings;
}

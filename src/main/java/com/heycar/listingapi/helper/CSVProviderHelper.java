package com.heycar.listingapi.helper;

import com.heycar.listingapi.exception.InvalidCSVException;
import com.heycar.listingapi.model.dto.ListingDto;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CSVProviderHelper {

    public List<ListingDto> getListingsFromCSV(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<ListingDto> cb = new CsvToBeanBuilder(reader)
                    .withType(ListingDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return cb.parse();
        } catch (Exception e) {
            log.error("Error parsing csv file", e);
            throw new InvalidCSVException();
        }
    }
}

package com.heycar.listingapi.model.request;

import com.heycar.listingapi.model.entity.Listing;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchListingRequest implements Specification<Listing> {
    private String make;
    private String model;
    @Positive
    @Digits(fraction = 0, integer = 4, message = "invalid year")
    private Integer year;
    private String color;

    @Override
    public Predicate toPredicate(Root<Listing> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (make != null) {
            predicates.add(cb.equal(root.get("make"), make));
        }
        if (model != null) {
            predicates.add(cb.equal(root.get("model"), model));
        }
        if (year != null) {
            predicates.add(cb.ge(root.get("year"), year));
        }
        if (color != null) {
            predicates.add(cb.equal(root.get("color"), color));
        }

        return query.where(predicates.toArray(new Predicate[0])).getRestriction();
    }
}

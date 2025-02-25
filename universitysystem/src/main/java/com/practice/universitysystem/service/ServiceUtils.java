package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.PageInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ServiceUtils<T, I, R extends JpaRepository<T, I>> {

    R thingRepository;

    private Page<T> getAllT(int page, int pageSize) {
        return thingRepository.findAll(PageRequest.of(page, pageSize));
    }

    /**
     * @param page Desired page number, starts at 1
     * @param size total amount of pages
     * @return list containing current page info and the paginated list
     */
    public List<Object> getPaginatedList(int page, int size) {
        page--;

        if (page < 0) {
            throw new IllegalArgumentException("Page number must not be less than 1");
        }

        List<T> thingList = getAllT(page, size).toList();
        int responseSize = thingList.size();
        List<Object> responseList = new ArrayList<>(responseSize +1);

        PageInfoDto pageInfo = new PageInfoDto(page+1L, responseSize, maxPageNumberGiven(size), thingRepository.count());

        responseList.add(pageInfo);
        responseList.addAll(thingList);
        return responseList;
    }

    public void validate(T thing) throws ConstraintViolationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(thing);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    private long maxPageNumberGiven(long pageSize) {
        long totalCount = thingRepository.count();
        return (long) Math.ceil((double) totalCount / pageSize);
    }

}

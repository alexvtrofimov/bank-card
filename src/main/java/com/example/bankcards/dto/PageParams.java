package com.example.bankcards.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageParams(Integer pageNumber, Integer pageSize, String sortDirection, String sortField) {
    public PageParams {
        if (pageNumber == null || pageNumber == 0) pageNumber = 0;
        if (pageSize == null || pageSize == 0) pageSize = 10;
        sortDirection = Sort.Direction.ASC.toString();
        if (sortDirection != null && !sortDirection.isBlank()) {
            try {
                Sort.Direction direction = Sort.Direction.valueOf(sortDirection.toUpperCase().trim());
                sortDirection = direction.toString();
            } catch (IllegalArgumentException e) {
                // sortDirection = Sort.Direction.ASC.toString();
            }
        }
    }

    public Pageable getPageable() {
        if (sortField != null && !sortField.isBlank()) {
            return PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.Direction.valueOf(sortDirection),
                    sortField.trim().toLowerCase()
            );
        } else {
            return PageRequest.of(
                    pageNumber,
                    pageSize
            );
        }
    }
}

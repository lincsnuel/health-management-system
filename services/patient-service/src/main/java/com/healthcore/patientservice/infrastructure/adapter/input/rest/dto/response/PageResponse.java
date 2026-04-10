package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(
        List<T> data,
        int pageNumber,
        int pageSize,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious,
        boolean sorted,
        Long totalElements,
        Integer totalPages
) {
    /**
     * Static factory method to create a PageResponse.
     */
    public static <T> PageResponse<T> of(
            List<T> data,
            int pageNumber,
            int pageSize,
            boolean first,
            boolean last,
            boolean hasNext,
            boolean hasPrevious,
            boolean sorted,
            Long totalElements,
            Integer totalPages
    ) {
        return new PageResponse<>(
                data, pageNumber, pageSize, first, last,
                hasNext, hasPrevious, sorted, totalElements, totalPages
        );
    }
}
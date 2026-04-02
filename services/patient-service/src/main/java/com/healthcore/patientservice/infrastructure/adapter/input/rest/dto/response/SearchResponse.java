package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthcore.patientservice.application.query.pagination.PageResult;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchResponse<T>(
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
     * Maps a Domain PageResult to a REST SearchResponse.
     * Note: accessing PageResult fields via record accessor methods (no 'get' prefix).
     */
    public static <T> SearchResponse<T> of(PageResult<T> result) {
        return new SearchResponse<>(
                result.content(),
                result.pageNumber(),
                result.pageSize(),
                result.first(),
                result.last(),
                result.hasNext(),
                result.hasPrevious(),
                result.sorted(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
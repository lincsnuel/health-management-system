package com.healthcore.patientservice.application.query.model;

import java.util.List;

public record PageResult<T>(
        List<T> content,
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
     * Compact constructor to handle defensive copying and null safety.
     */
    public PageResult {
        content = (content == null) ? List.of() : List.copyOf(content);
    }

    // ---------- Factory for full Page ----------
    public static <T> PageResult<T> ofPage(
            List<T> content,
            int pageNumber,
            int pageSize,
            boolean first,
            boolean last,
            boolean hasNext,
            boolean hasPrevious,
            boolean sorted,
            long totalElements,
            int totalPages
    ) {
        return new PageResult<>(
                content, pageNumber, pageSize, first, last,
                hasNext, hasPrevious, sorted, totalElements, totalPages
        );
    }

    // ---------- Factory for Slice (no total count) ----------
    public static <T> PageResult<T> ofSlice(
            List<T> content,
            int pageNumber,
            int pageSize,
            boolean first,
            boolean last,
            boolean hasNext,
            boolean hasPrevious,
            boolean sorted
    ) {
        return new PageResult<>(
                content, pageNumber, pageSize, first, last,
                hasNext, hasPrevious, sorted, null, null
        );
    }
}
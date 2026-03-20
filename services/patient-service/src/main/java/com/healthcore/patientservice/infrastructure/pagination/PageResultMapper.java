package com.healthcore.patientservice.infrastructure.pagination;

import com.healthcore.patientservice.application.pagination.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

public final class PageResultMapper {

    private PageResultMapper() {}

    public static <T, R> PageResult<R> fromPage(
            Page<T> page,
            Function<T, R> mapper
    ) {
        List<R> mapped = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return PageResult.ofPage(
                mapped,
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                page.getSort().isSorted(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public static <T, R> PageResult<R> fromSlice(
            Slice<T> slice,
            Function<T, R> mapper
    ) {
        List<R> mapped = slice.getContent()
                .stream()
                .map(mapper)
                .toList();

        return PageResult.ofSlice(
                mapped,
                slice.getNumber(),
                slice.getSize(),
                slice.isFirst(),
                slice.isLast(),
                slice.hasNext(),
                slice.hasPrevious(),
                slice.getSort().isSorted()
        );
    }
}

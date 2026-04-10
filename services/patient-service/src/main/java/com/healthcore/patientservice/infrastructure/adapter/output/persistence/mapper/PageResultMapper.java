package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.application.query.model.PageResult;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.PageResponse;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

public final class PageResultMapper {

    private PageResultMapper() {}

    public static <T, R> PageResponse<R> toPage(
            PageResult<T> page,
            List<R> mapped
    ) {
        return PageResponse.of(
                mapped,
                page.pageNumber(),
                page.pageSize(),
                page.first(),
                page.last(),
                page.hasNext(),
                page.hasPrevious(),
                page.sorted(),
                page.totalElements(),
                page.totalPages()
        );
    }

    public static <T, R> PageResult<R> toSlice(
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

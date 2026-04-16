package com.healthcore.workforceservice.staff.infrastructure.adapter.input.rest.controller;

import com.healthcore.workforceservice.staff.application.query.model.StaffView;
import com.healthcore.workforceservice.staff.application.query.pagination.PageResult;
import com.healthcore.workforceservice.staff.application.query.service.StaffReadService;
import com.healthcore.workforceservice.staff.infrastructure.adapter.input.rest.dto.response.SearchResponse;
import com.healthcore.workforceservice.staff.infrastructure.adapter.input.rest.dto.response.StaffListItemResponse;
import com.healthcore.workforceservice.staff.infrastructure.adapter.input.rest.mapper.StaffReadRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffQueryController {

    private final StaffReadService readService;
    private final StaffReadRestMapper readMapper;

    @GetMapping
    public ResponseEntity<SearchResponse<StaffListItemResponse>> getAllStaff(
            @RequestParam String tenantId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        PageResult<StaffView> pageResult =
                readService.getAllStaff(
                        tenantId,
                        pageNo,
                        size,
                        sortBy,
                        direction
                );

        List<StaffListItemResponse> content =
                pageResult.content()
                        .stream()
                        .map(readMapper::toStaffListItemResponse)
                        .toList();

        PageResult<StaffListItemResponse> mappedPageResult = PageResult.ofPage(
                content,
                pageResult.pageNumber(),
                pageResult.pageSize(),
                pageResult.first(),
                pageResult.last(),
                pageResult.hasNext(),
                pageResult.hasPrevious(),
                pageResult.sorted(),
                pageResult.totalElements(),
                pageResult.totalPages()
        );

        return ResponseEntity.ok(SearchResponse.of(mappedPageResult));
    }
}
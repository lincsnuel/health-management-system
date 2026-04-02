package com.healthcore.workforceservice.application.query.service;

import com.healthcore.workforceservice.application.query.model.StaffView;
import com.healthcore.workforceservice.application.query.pagination.PageResult;
import com.healthcore.workforceservice.application.query.usecase.StaffReadUseCase;
import com.healthcore.workforceservice.application.query.repository.StaffReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffReadService implements StaffReadUseCase {

    private final StaffReadRepository staffRepository;

    @Override
    public PageResult<StaffView> getAllStaff(String tenantId,
                                       int pageNo,
                                       int size,
                                       String sortBy,
                                       String direction
    ) {

        String safeSort = resolveSortProperty(sortBy);
        Sort sort = Sort.by(direction == null ?
                        Sort.Direction.ASC : Sort.Direction.valueOf(direction.toUpperCase()),
                safeSort);
        Pageable pageable = PageRequest.of(pageNo, size, sort);

        Page<StaffView> page =
                staffRepository.findByTenant(tenantId, pageable);

        return PageResult.ofPage(
                page.getContent(),
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

    /* =========================================================
       INTERNAL SORT WHITELIST
       ========================================================= */
    private String resolveSortProperty(String sortBy) {

        if (sortBy == null || sortBy.isBlank()) {
            return "createdAt";
        }

        return switch (sortBy) {
            case "firstName" -> "firstName";
            case "lastName" -> "lastName";
            case "dateOfBirth" -> "dateOfBirth";
            case "hospitalPatientNumber" -> "hospitalPatientNumber";
            default -> "createdAt";
        };
    }
}
//package com.healthcore.patientservice.infrastructure.adapter.input.rest.controller;
//
//import com.healthcore.patientservice.application.pagination.PageResult;
//import com.healthcore.patientservice.application.query.model.PatientDetails;
//import com.healthcore.patientservice.application.query.model.PatientListItem;
//import com.healthcore.patientservice.application.query.model.PatientSummary;
//import com.healthcore.patientservice.application.usecase.PatientQueryUseCase;
//import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.*;
//import com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper.PatientQueryRestMapper;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/patients")
//public class PatientQueryController {
//
//    private final PatientQueryUseCase patientQueryService;
////    private final PatientQueryRestMapper queryMapper;
//
//    /* =========================================================
//       GET PAGINATED PATIENT SUMMARY
//       ========================================================= */
//
//    @GetMapping
//    public ResponseEntity<SearchResponse<PatientSummaryResponse>> getAllPatients(
//            @RequestParam String tenantId,
//            @RequestParam(defaultValue = "0") int pageNo,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "ASC") String direction
//    ) {
//
//        PageResult<PatientListItem> pageResult =
//                patientQueryService.getPatients(
//                        tenantId,
//                        pageNo,
//                        size,
//                        sortBy,
//                        direction
//                );
//
//        List<PatientSummaryResponse> content =
//                pageResult.content()
//                        .stream()
//                        .map(queryMapper::toSummaryResponse)
//                        .toList();
//
//        PageResult<PatientSummaryResponse> mappedPageResult = PageResult.ofPage(
//                content,
//                pageResult.pageNumber(),
//                pageResult.pageSize(),
//                pageResult.first(),
//                pageResult.last(),
//                pageResult.hasNext(),
//                pageResult.hasPrevious(),
//                pageResult.sorted(),
//                pageResult.totalElements(),
//                pageResult.totalPages()
//        );
//
//        return ResponseEntity.ok(SearchResponse.of(mappedPageResult));
//    }
//
//    /* =========================================================
//       SEARCH PATIENTS BY NAME
//       ========================================================= */
//
//    @GetMapping("/search")
//    public ResponseEntity<SearchResponse<PatientNameResponse>> searchPatients(
//            @RequestParam String tenantId,
//            @RequestParam String query,
//            @RequestParam(defaultValue = "0") int pageNo,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//
//        PageResult<PatientSummary> pageResult =
//                patientQueryService.searchPatientByName(query, tenantId, pageNo, size);
//
//        List<PatientNameResponse> content =
//                pageResult.content()
//                        .stream()
//                        .map(queryMapper::toNameResponse)
//                        .toList();
//
//        PageResult<PatientNameResponse> mappedPageResult = PageResult.ofPage(
//                content,
//                pageResult.pageNumber(),
//                pageResult.pageSize(),
//                pageResult.first(),
//                pageResult.last(),
//                pageResult.hasNext(),
//                pageResult.hasPrevious(),
//                pageResult.sorted(),
//                pageResult.totalElements(),
//                pageResult.totalPages()
//        );
//
//        return ResponseEntity.ok(SearchResponse.of(mappedPageResult));
//    }
//
//    /* =========================================================
//       GET PATIENT DETAILS
//       ========================================================= */
//
//    @GetMapping("/{patientId}")
//    public ResponseEntity<PatientDetailsResponse> getPatientById(
//            @PathVariable UUID patientId,
//            @RequestParam String tenantId
//    ) {
//
//        PatientDetails result =
//                patientQueryService.getPatientById(patientId, tenantId);
//
//        PatientDetailsResponse response =
//                queryMapper.toDetailsResponse(result);
//
//        return ResponseEntity.ok(response);
//    }
//}
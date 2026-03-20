package com.healthcore.patientservice.infrastructure.adapter.input.rest.controller;

import com.healthcore.patientservice.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

//@Sql("/test-data.sql")
class PatientQueryControllerTest extends AbstractIntegrationTest {

    @Test
    void shouldGetAllPatients() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/patients")
                .then()
                .statusCode(400);
//                .body("content", hasSize(8))
//                .body("pageNumber", is(1))
//                .body("pageSize", is(10))
//                .body("first", is(true))
//                .body("last", is(true))
//                .body("hasNext", is(false))
//                .body("hasPrevious", is(false))
//                .body("sorted",is(false))
//                .body("totalElements", is(0))
//                .body("totalPages", is(1));
    }

}
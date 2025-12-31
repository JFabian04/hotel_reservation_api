package com.demo.hotel_reservation_api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PagedReservationResponse(
        @JsonProperty("content")
        List<ReservationResponse> content,

        @JsonProperty("page_number")
        int pageNumber,

        @JsonProperty("page_size")
        int pageSize,

        @JsonProperty("total_elements")
        long totalElements,

        @JsonProperty("total_pages")
        int totalPages,

        @JsonProperty("has_next")
        boolean hasNext,

        @JsonProperty("has_previous")
        boolean hasPrevious
) {}
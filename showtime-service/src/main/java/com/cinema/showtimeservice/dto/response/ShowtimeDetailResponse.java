package com.cinema.showtimeservice.dto.response;

import com.cinema.common.constants.DateFormatConstants;
import com.cinema.showtimeservice.dto.request.ShowtimePriceRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShowtimeDetailResponse {
    private Long id;
    private Long roomId;
    private Long movieId;
    private Integer bufferTime; // in minutes
    private String status;
    @JsonFormat(pattern = DateFormatConstants.C_DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime startTime;
    @JsonFormat(pattern = DateFormatConstants.C_DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime endTime;

    private List<ShowtimePriceRequest> prices;
}

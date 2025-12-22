package com.cinema.showtimeservice.dto.request;

import com.cinema.common.constants.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateShowtimeRequest {
    private Long movieId;
    @JsonFormat(pattern = DateFormatConstants.C_YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime startTime;
    private Integer bufferMinutes;
    private List<ShowtimePriceRequest> prices;

    public Integer getBufferMinutes() {
        return bufferMinutes == null ? 0 : bufferMinutes;
    }
}

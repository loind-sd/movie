package com.cinema.showtimeservice.dto.request;

import com.cinema.showtimeservice.dto.response.ShowtimeDetailResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateShowtimeRequest extends ShowtimeDetailResponse {
}

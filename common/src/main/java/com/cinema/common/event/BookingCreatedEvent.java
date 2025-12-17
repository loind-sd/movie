package com.cinema.common.event;

import com.cinema.common.dto.MovieDto;
import com.cinema.common.dto.UserDto;
import com.cinema.common.enums.BookingStatus;

import java.time.Instant;
import java.util.List;

public class BookingCreatedEvent {
    private String bookingId;
    private String traceId;
    private UserDto user;
    private MovieDto movie;
    private List<String> seatCodes;
    private BookingStatus status;
    private Instant createdAt;

    // getters / setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }
    public MovieDto getMovie() { return movie; }
    public void setMovie(MovieDto movie) { this.movie = movie; }
    public List<String> getSeatCodes() { return seatCodes; }
    public void setSeatCodes(List<String> seatCodes) { this.seatCodes = seatCodes; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

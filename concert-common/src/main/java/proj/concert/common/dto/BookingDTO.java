package proj.concert.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;

/**
 * Represents a completed booking.
 * concertId   the id of the concert which was booked
 * date        the date on which that concert was booked
 * seats       the seats which were booked for that concert on that date
 */
public class BookingDTO {

    private long concertId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    private List<SeatDTO> seats = new ArrayList<>();

    public BookingDTO() {
    }

    public BookingDTO(long concertId, LocalDateTime date, List<SeatDTO> seats) {
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getDate() {
        return date;
    }

//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }
}

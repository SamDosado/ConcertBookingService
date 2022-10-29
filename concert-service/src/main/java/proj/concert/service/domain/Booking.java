package proj.concert.service.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Booking {
/**
 * Represents a booking Object
 * bookingID   the generated id of the booking object
 * user        the user object for which user created the booking
 * concert     the concert object for which the booking if for
 * date        the date on which that concert was booked
 * seats       the seats which were booked for that concert on that date
 */
    @Id
    @GeneratedValue
    @Column(name="bookingID")
    private long bookingID;

    @ManyToOne
    private User user;

    @ManyToOne
    private Concert concert;
    private LocalDateTime date;

    @OneToMany(mappedBy = "booking", cascade=CascadeType.PERSIST)

    private List<Seat> seats = new ArrayList<Seat>();

    public Booking(){}

    public Booking(Concert concert, LocalDateTime date, List<Seat> seats){
        this.concert = concert;
        this.date = date;
        this.seats = seats;
    }

    public Booking(User user, Concert concert, LocalDateTime date, List<Seat> seats){
        this.user = user;
        this.concert = concert;
        this.date = date;
        this.seats = seats;
    }

    public long getBookingID(){
        return bookingID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Booking))
            return false;
        if (other == this)
            return true;

        Booking rhs = (Booking) other;
        return new EqualsBuilder().
                append(concert, rhs.getConcert()).
                append(seats, rhs.getSeats()).
                // I got rid of the second equals case :shrug: not sure if we need it lmoa
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(getClass().getName()).
                append(concert).
                append(seats).
                // I also got rid of the second hash, not sure if we need it
                toHashCode();
    }
}

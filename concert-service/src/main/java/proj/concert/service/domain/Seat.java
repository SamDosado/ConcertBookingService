package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Seat Object
 * label   	   the label of the seat
 * isBooked    represents if the seat is booked or not
 * date        the date of the concert
 * price       the price of the seat
 */

@Entity
public class Seat{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String label;
	private boolean isBooked;
	private LocalDateTime date;
	private BigDecimal price;

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="bookingID")
	private Booking booking;

	public Seat() {}


	public Seat(String Label){
		this.label = label;
		setPriceByLabel(label);
	}

	// Added a new seat constructor for SeatMapper.java
	public Seat(String label, BigDecimal price){
		this.label = label;
		this.price = price;
	}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
	}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price, Booking booking){
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
		this.booking = booking;
	}

	public String getLabel() {
		return label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isBooked() { return isBooked; }
	public void setBooked(Boolean status) {	this.isBooked = status;	}

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return label;
	}

	public void setPriceByLabel(String label){
		char row = label.charAt(0);

		switch(row){
			case 'A':
			case 'B':
				price = new BigDecimal(150);
				break;
			case 'C':
			case 'D':
			case 'E':
				price = new BigDecimal(120);
				break;
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
				price = new BigDecimal(90);
				break;
		}
	}

	// We may need to change this because of seatmapper
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Seat seatDTO = (Seat) o;

		return new EqualsBuilder()
				.append(label, seatDTO.label)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(label)
				.toHashCode();
	}
}

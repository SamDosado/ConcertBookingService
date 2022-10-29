package proj.concert.service.mappers;

import proj.concert.service.domain.Booking;
import proj.concert.service.domain.Concert;
import proj.concert.common.dto.*;

import java.util.ArrayList;
import java.util.List;

/*
 * == Booking Mapper ==
 * Maps the Booking Objects between the DTO and the Domain Models
 */

/*
* Booking Mapper.
* Handles the mapping between BookingDTO & BookingRequestDTO to Booking domain model.
*  Methods:
* toDomainModel: Takes either a BookingRequestDTO or BookingDTO and converts it into the Booking domain model
* toRequestDto: Takes a Booking domain model and turns it into a BookingRequestDTO
* toDto: Takes a Booking domain model and turns it into a BookingDTO
* */
public class BookingMapper {
    /*maybe pass concert as well in concert resource?

    public static Booking toDomainModel(BookingRequestDTO dtoBookingRequest){
        Booking newBookingRequest = new Booking(
                dtoBookingRequest.getConcertId(),
                dtoBookingRequest.getDate(),
                SeatMapper.labelsToListDm(dtoBookingRequest.getSeatLabels())
        );
        return newBookingRequest;
    }

    public static Booking toDomainModel(BookingDTO dtoBookingRequest){
        Booking newBookingRequest = new Booking(
                dtoBookingRequest.getConcertId(),
                dtoBookingRequest.getDate(),
                SeatMapper.toListDm(dtoBookingRequest.getSeats())
        );

        return newBookingRequest;
    } */

    public static Booking toDomainModel(BookingRequestDTO dtoBookingRequest, Concert concert){
        Booking newBookingRequest = new Booking(
                concert,
                dtoBookingRequest.getDate(),
                SeatMapper.labelsToListDm(dtoBookingRequest.getSeatLabels())
        );
        return newBookingRequest;
    }

    public static Booking toDomainModel(BookingDTO dtoBookingRequest, Concert concert){
        return new Booking(
                concert,
                dtoBookingRequest.getDate(),
                SeatMapper.toListDm(dtoBookingRequest.getSeats())
        );
    }
    // So im not sure if this should be for booking or bookingRequest :pepega:
    public static BookingRequestDTO toRequestDto(Booking booking){
        BookingRequestDTO dtoBookingRequest =
                new BookingRequestDTO(
                        booking.getConcert().getId(),
                        booking.getDate(),
                        SeatMapper.toListString(booking.getSeats())

                );
        return dtoBookingRequest;
    }

    //finish this
    public static BookingDTO toDto(Booking booking){
        BookingDTO bookingDto = new BookingDTO(
                booking.getConcert().getId(),
                booking.getDate(),
                SeatMapper.toListDto(booking.getSeats())
        );
        return bookingDto;
    }

    public static List<BookingDTO> toListDto(List<Booking> bookings) {
        List<BookingDTO> bookingDtos = new ArrayList<BookingDTO>();
        for(Booking booking : bookings){
            bookingDtos.add(toDto(booking));
        }
        return bookingDtos;
    }


}
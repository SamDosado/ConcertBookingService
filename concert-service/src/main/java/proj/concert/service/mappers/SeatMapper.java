package proj.concert.service.mappers;

import proj.concert.service.domain.Seat;
import proj.concert.common.dto.SeatDTO;

import java.util.ArrayList;
import java.util.List;

/*
 * == Seat Mapper ==
 * Maps the Seat Objects between the DTO and the Domain Models
 */

public class SeatMapper {
    public static Seat toDomainModel(SeatDTO dtoSeat){
        return new Seat(
                dtoSeat.getLabel(),
                dtoSeat.getPrice()
        );
    }

    public static Seat toDomainModel(String seatLabel){
        return new Seat(
                seatLabel
        );
    }

    public static SeatDTO toDto(Seat seat){
        return
                new SeatDTO(
                        seat.getLabel(),
                        seat.getPrice()
                );
    }

    public static List<SeatDTO> toListDto(List<Seat> seats){
        List<SeatDTO> seatDtos = new ArrayList<SeatDTO>();

        for(Seat seat : seats){
            seatDtos.add(toDto(seat));
        }

        return seatDtos;
    }

    public static List<Seat> toListDm(List<SeatDTO> seats){
        List<Seat> seatsDm = new ArrayList<Seat>();

        for(SeatDTO seat: seats){
            seatsDm.add(toDomainModel(seat));
        }

        return seatsDm;
    }

    public static List<Seat> labelsToListDm(List<String> seatLabels){
        List<Seat> seatsDm = new ArrayList<Seat>();
        for(String seat: seatLabels){
            seatsDm.add(toDomainModel(seat));
        }
        return seatsDm;
    }

    public static List<String> toListString(List<Seat> seats){
        List<String> seatLabels = new ArrayList<String>();
        for(Seat seat: seats){
            seatLabels.add(seat.getLabel());
        }
        return seatLabels;
    }


}

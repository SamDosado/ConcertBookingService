package proj.concert.service.mappers;

import proj.concert.common.dto.*;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * == Concert Mapper ==
 * Maps the Concert Objects between the DTO and the Domain Models
 */

public class ConcertMapper {
    public static Concert toDomainModel(ConcertDTO dtoConcert){
        Concert newConcert = new Concert(
                dtoConcert.getId(),
                dtoConcert.getTitle(),
                dtoConcert.getImageName(),
                dtoConcert.getBlurb(),
                new HashSet<LocalDateTime>(dtoConcert.getDates()),
                (Set<Performer>) PerformerMapper.listToDm(dtoConcert.getPerformers())
        );
        return newConcert;
    }

    public static ConcertDTO toDto(Concert concert){
        ConcertDTO dtoConcert =
                new ConcertDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName(),
                        concert.getBlurb()
                );
        //dtoConcert.setDates(new ArrayList<>(concert.getDates()));
        //dtoConcert.setPerformers(PerformerMapper.listToDto(concert.getPerformers()));
        for (Performer p : concert.getPerformers()) {
            dtoConcert.getPerformers().add(PerformerMapper.toDto(p));
        }
        for (LocalDateTime d : concert.getDates()) {
            dtoConcert.getDates().add(d);
        }
        return dtoConcert;
    }

    public static List<ConcertDTO> toListDto(List<Concert> concerts){
        List<ConcertDTO> concertsDto = new ArrayList<ConcertDTO>();
        for(Concert concert : concerts){
            concertsDto.add(toDto(concert));
        }
        return concertsDto;
    }

    public static List<ConcertSummaryDTO> toListSummary(List<Concert> concerts){
        List<ConcertSummaryDTO> summaries = new ArrayList<ConcertSummaryDTO>();
        for(Concert concert : concerts){
            summaries.add(toSummary(concert));
        }
        return summaries;
    }

    //Need to get the number of seats somehow
    public static ConcertInfoNotificationDTO toNotification(Concert concert){
        ConcertInfoNotificationDTO concertNotification = new ConcertInfoNotificationDTO(
        );

        return concertNotification;
    }

    //Cannot finish as the relevant information is not provided to me.
    public static ConcertInfoSubscriptionDTO toSubscription(Concert concert){
        ConcertInfoSubscriptionDTO concertSubscription = new ConcertInfoSubscriptionDTO();

        return concertSubscription;
    }

    public static ConcertSummaryDTO toSummary(Concert concert){
        ConcertSummaryDTO concertSummary = new ConcertSummaryDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName()
        );
        return concertSummary;
    }
}

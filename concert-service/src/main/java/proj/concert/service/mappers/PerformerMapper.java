package proj.concert.service.mappers;

import proj.concert.service.domain.Performer;
import proj.concert.common.dto.PerformerDTO;

import java.util.ArrayList;
import java.util.List;

/*
 * == Performer Mapper ==
 * Maps the Performer Objects between the DTO and the Domain Models
 */

public class PerformerMapper {
    public static Performer toDomainModel(PerformerDTO dtoPerformer){
        Performer newPerformer = new Performer(
                dtoPerformer.getId(),
                dtoPerformer.getName(),
                dtoPerformer.getImageName(),
                dtoPerformer.getGenre(),
                dtoPerformer.getBlurb()
        );
        return newPerformer;
    }

    public static PerformerDTO toDto(Performer performer){
        PerformerDTO dtoPerformer =
                new PerformerDTO(
                        performer.getId(),
                        performer.getName(),
                        performer.getImageName(),
                        performer.getGenre(),
                        performer.getBlurb()
                );
        return dtoPerformer;
    }

    public static List<Performer> listToDm(List<PerformerDTO> performers){
        List<Performer> performerDM = new ArrayList<Performer>();
        for(PerformerDTO performer: performers){
            performerDM.add(toDomainModel(performer));
        }
        return performerDM;
    }

    public static List<PerformerDTO> listToDto (List<Performer> performers){
        List<PerformerDTO> performerDataTo = new ArrayList<PerformerDTO>();
        for(Performer performer: performers){
            performerDataTo.add(toDto(performer));
        }
        return performerDataTo;
    }
}

package proj.concert.service.mappers;

import proj.concert.service.domain.User;
import proj.concert.common.dto.UserDTO;

/*
 * == User Mapper ==
 * Maps the User Objects between the DTO and the Domain Models
 */

public class UserMapper {
    public static User toDomainModel(UserDTO dtoUser) {
        User newUser = new User(
                dtoUser.getUsername(),
                dtoUser.getPassword()
        );
        return newUser;
    }

    static UserDTO toDto(User user){
        UserDTO dtoUser =
                new proj.concert.common.dto.UserDTO(
                        user.getUsername(),
                        user.getPassword()
                );
        return dtoUser;
    }
}

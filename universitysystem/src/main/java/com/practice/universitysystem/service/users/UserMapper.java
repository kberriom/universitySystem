package com.practice.universitysystem.service.users;

import com.practice.universitysystem.dto.users.UserDto;
import com.practice.universitysystem.model.users.UniversityUser;

public interface UserMapper<U extends UniversityUser, D extends UserDto> {

    D userToDTO(U user);

    U dtoToUser(D userDto);

    U adminUpdate(U user, D userUpdateDto);

    U update(U user, D userUpdateDto);

}

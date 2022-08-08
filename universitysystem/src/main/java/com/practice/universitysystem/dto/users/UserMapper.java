package com.practice.universitysystem.dto.users;

import com.practice.universitysystem.model.users.UniversityUser;

public interface UserMapper<U extends UniversityUser, S extends UserUpdateDto, D extends UserDto> {

    D userToDTO(U user);

    U dtoToUser(D userDto);

    void update(U user, S userUpdateDto);

}

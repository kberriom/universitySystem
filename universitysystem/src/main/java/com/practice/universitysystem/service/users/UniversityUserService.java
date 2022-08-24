package com.practice.universitysystem.service.users;

import com.practice.universitysystem.dto.users.UserDto;
import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @param <U> Specific User repository type
 * @param <D> UserDto instance type
 * @param <R> Instance type repository
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class UniversityUserService<D extends UserDto,M extends UserMapper<U, D>, U extends UniversityUser, R extends JpaRepository<U, Long>>{

    private AuthService authService;

    private M mapper;

    /**
     * User instance patent repository.
     * All users must extend and use {@link UniversityUser}.
     */
    private UniversityUserRepository userRepository;

    /**
     * Concrete user instance repository.
     */
    private R instanceUserRepository;

    private ServiceUtils<U, Long, R> serviceUtils;

    public U createUser(D userDto) {
        String encodedPassword = authService.getEncodedPassword(userDto.getUserPassword());
        userDto.setUserPassword(encodedPassword);

        U user = mapper.dtoToUser(userDto);

        user.setEnrollmentDate(LocalDate.now());

        serviceUtils.validate(user);

        return instanceUserRepository.save(user);
    }

    public void deleteUser(String email) {
        U user = getUser(email);
        instanceUserRepository.delete(user);
    }

    public void deleteUser(long id) {
        U user = getUser(id);
        instanceUserRepository.delete(user);
    }

    public U updateUser(String email, D updateDto) {
        U user = mapper.update(getUser(email), updateDto);
        serviceUtils.validate(user);
        return instanceUserRepository.save(user);
    }

    /**
     * Admin update, can update birthdate and gov id
     */
    public U updateUser(long id, D updateDto) {
        U user = mapper.adminUpdate(getUser(id), updateDto);
        serviceUtils.validate(user);
        return instanceUserRepository.save(user);
    }

    public U getUser(String email) {
        Optional<U> user = instanceUserRepository.findById(userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Unable to find User with email: " + email)).getId());
        return user.orElseThrow(() -> new NoSuchElementException("Unable to find User"));
    }

    public U getUser(long id) {
        return instanceUserRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Unable to find User with id: " + id));
    }

    public List<U> getAllUsers() {
        return instanceUserRepository.findAll();
    }

    /**
     * @param page Desired page number, starts at 1
     * @param size total amount of pages
     * @return list containing current page info and the paginated list
     */
    public List<Object> getUserPaginatedList(int page, int size) {
        return serviceUtils.getPaginatedList(page, size);
    }

}

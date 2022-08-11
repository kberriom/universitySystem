package com.practice.universitysystem.service.users;

import com.practice.universitysystem.dto.PageInfoDto;
import com.practice.universitysystem.dto.users.UserDto;
import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.*;
import java.time.LocalDate;
import java.util.*;

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

    public U createUser(D userDto) {
        String encodedPassword = authService.getEncodedPassword(userDto.getUserPassword());
        userDto.setUserPassword(encodedPassword);

        U user = mapper.dtoToUser(userDto);

        user.setEnrollmentDate(LocalDate.now());

        validateUser(user);

        return instanceUserRepository.save(user);
    }

    public void validateUser(U user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<U>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
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
        validateUser(user);
        return instanceUserRepository.save(user);
    }

    /**
     * Admin update, can update birthdate and gov id
     */
    public U updateUser(long id, D updateDto) {
        U user = mapper.adminUpdate(getUser(id), updateDto);
        validateUser(user);
        return instanceUserRepository.save(user);
    }

    public U getUser(String email) {
        Optional<U> user = instanceUserRepository.findById(userRepository.findByEmail(email).orElseThrow().getId());
        return user.orElseThrow();
    }

    public U getUser(long id) {
        return instanceUserRepository.findById(id).orElseThrow();
    }

    public List<U> getAllUsers() {
        return instanceUserRepository.findAll();
    }

    public Page<U> getAllUsers(int page, int pageSize) {
        return instanceUserRepository.findAll(PageRequest.of(page, pageSize));
    }

    /**
     * @param page Desired page number, starts at 1
     * @param size total amount of pages
     * @return list containing current page info and the paginated list
     */
    public List<Object> getUserPaginatedList(int page, int size) {
        page--;

        if (page < 0) {
            throw new IllegalArgumentException("Page number must not be less than 1");
        }

        List<U> userList = getAllUsers(page, size).toList();
        int responseUserSize = userList.size();
        List<Object> responseList = new ArrayList<>(responseUserSize +1);

        PageInfoDto pageInfo = new PageInfoDto(page+1L, responseUserSize, maxPageNumberGiven(size));

        responseList.add(pageInfo);
        responseList.addAll(userList);
        return responseList;
    }

    private long maxPageNumberGiven(long pageSize) {
        long totalCount = instanceUserRepository.count();
        return (long) Math.ceil((double) totalCount / pageSize);
    }

}

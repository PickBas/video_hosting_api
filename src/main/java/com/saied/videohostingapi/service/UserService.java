package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.user.UpdateUserDto;
import com.saied.videohostingapi.models.User;

import jakarta.validation.ValidationException;
import java.util.List;

public interface UserService {

    /**
     * Registering user
     * @param user User entity
     */
    void register(User user);

    /**
     * Checking user's existence
     * @param userId User id
     */
    boolean checkIfUserExists(Long userId);

    /**
     * Updating user's password
     * @param userId User id
     * @param password New passowrd
     */
    void updatePassword(Long userId, String password);

    /**
     * Updating user data
     * @param userId User id
     * @param updateUserDto New user data
     * @throws ValidationException If provided data is invalid
     */
    void updateNames(Long userId, UpdateUserDto updateUserDto) throws ValidationException;

    /**
     * Listing users with pagination
     * @param page Number of the page
     * @return List of users
     */
    List<User> getUsersPaginated(int page);

    /**
     * Finding user by id
     * @param id User id
     * @return User entity
     */
    User findById(Long id);

    /**
     * Finding user by profile
     * @param profileId ProfileId
     * @return User entity
     */
    User findByProfile(Long profileId);

    /**
     * Finding user by username
     * @param username Username
     * @return User entity
     */
    User findByUsername(String username);

    /**
     * Deleting user
     * @param id User id
     */
    void delete(Long id);
}

package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.user.UpdateUserDto;
import com.saied.videohostingapi.dto.user.UserRegistrationDto;
import com.saied.videohostingapi.exceptions.profile.ProfileNotFoundException;
import com.saied.videohostingapi.exceptions.user.AppUserNotFoundException;
import com.saied.videohostingapi.exceptions.user.AppUserAlreadyExistsException;
import com.saied.videohostingapi.models.User;

import jakarta.validation.ValidationException;

import org.springframework.data.domain.Page;

public interface UserService {

    /**
     * Registering user
     * @param userRegDto UserRegistrationDto entity
     */
    void register(UserRegistrationDto userRegDto) throws AppUserAlreadyExistsException;

    /**
     * Setting profile for a user. Should be executed only once
     * @param userId User id
     * @param profileId Profile id
     * @throws ProfileNotFoundException if profile does not exist
     */
    void setProfile(Long userId, Long profileId) throws ProfileNotFoundException, AppUserNotFoundException;

    /**
     * Checking user's existence
     * @param username User id
     */
    boolean checkIfUserExistsByUsername(String username);

    /**
     * Updating user's password
     * @param userId User id
     * @param password New password
     */
    void updatePassword(Long userId, String password) throws AppUserNotFoundException;

    /**
     * Updating user data
     * @param userId User id
     * @param updateUserDto New user data
     * @throws ValidationException If provided data is invalid
     */
    void updateNames(Long userId, UpdateUserDto updateUserDto) throws AppUserNotFoundException;

    /**
     * Listing users with pagination
     * @param offset Offset
     * @param pageSize Page size
     * @return List of users
     */
    Page<User> getUsersPaginated(int offset, int pageSize);

    /**
     * Finding user by id
     * @param id User id
     * @return User entity
     */
    User findById(Long id) throws AppUserNotFoundException;

    /**
     * Finding user by profile
     * @param profileId ProfileId
     * @return User entity
     */
    User findByProfile(Long profileId) throws AppUserNotFoundException, ProfileNotFoundException;

    /**
     * Finding user by username
     * @param username Username
     * @return User entity
     */
    User findByUsername(String username) throws AppUserNotFoundException;

    /**
     * Deleting user
     * @param id User id
     */
    void delete(Long id);
}

package com.saied.videohostingapi.service.impl;

import com.saied.videohostingapi.dto.user.UpdateUserDto;
import com.saied.videohostingapi.dto.user.UserRegistrationDto;
import com.saied.videohostingapi.exceptions.profile.ProfileNotFoundException;
import com.saied.videohostingapi.exceptions.user.AppUserNotFoundException;
import com.saied.videohostingapi.exceptions.user.AppUserAlreadyExistsException;
import com.saied.videohostingapi.models.Role;
import com.saied.videohostingapi.models.User;
import com.saied.videohostingapi.repositories.RoleRepository;
import com.saied.videohostingapi.repositories.UserRepository;
import com.saied.videohostingapi.service.ProfileService;
import com.saied.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
        UserRepository userRepository,
        RoleRepository repository,
        ProfileService profileService,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = repository;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) throws AppUserNotFoundException {
        return this.userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> {
                    log.warn("User does not exist with username: {}", username);
                    return new AppUserNotFoundException(
                        String.format("Could not find user with username: %s", username)
                    );
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) throws AppUserNotFoundException {
        return this.userRepository
            .findById(id)
            .orElseThrow(
                () -> {
                    log.warn("User does not exist with id: {}", id);
                    return new AppUserNotFoundException(
                        String.format("Could not find user with id: %s", id)
                    );
                }
            );
    }

    @Override
    public User findByProfile(Long profileId) throws AppUserNotFoundException, ProfileNotFoundException {
        return this.userRepository
            .findByProfile(this.profileService.findById(profileId))
            .orElseThrow(
                () -> {
                    log.warn("User does not exist with profile_id: {}", profileId);
                    return new AppUserNotFoundException(
                        String.format("Could not find user with profile_id: %s", profileId)
                    );
                }
            );
    }

    @Override
    @Transactional(rollbackFor = AppUserAlreadyExistsException.class)
    public void register(UserRegistrationDto userRegDto) throws AppUserAlreadyExistsException {
        if (this.checkIfUserExistsByUsername(userRegDto.getUsername())) {
            log.warn("User already exists with username: {}", userRegDto.getUsername());
            throw new AppUserAlreadyExistsException(
                String.format(
                    "User already exists with username: %s", userRegDto.getUsername()
                )
            );
        }
        List<Role> userRoles = new ArrayList<>();
        Role roleUser = this.roleRepository.findByName("ROLE_USER");
        userRoles.add(roleUser);
        User.builder()
            .username(userRegDto.getUsername())
            .email(userRegDto.getEmail())
            .password(this.passwordEncoder.encode(userRegDto.getPassword()))
            .roles(userRoles)
            .build();
        log.info(
            "User was created with username: {}; email: {};",
            userRegDto.getUsername(),
            userRegDto.getEmail()
        );
    }

    @Override
    @Transactional(
        rollbackFor = {AppUserNotFoundException.class, ProfileNotFoundException.class}
    )
    public void setProfile(
        Long userId,
        Long profileId
    ) throws ProfileNotFoundException, AppUserNotFoundException {
        User user = this.findById(userId);
        user.setProfile(this.profileService.findById(profileId));
        log.info("Profile was set with id: {}; for user with id: {}", profileId, userId);
    }

    @Override
    public boolean checkIfUserExistsByUsername(String username) {
        try {
            this.findByUsername(username);
        } catch (AppUserNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String password) throws AppUserNotFoundException {
        User user = this.findById(userId);
        user.setPassword(this.passwordEncoder.encode(password));
        log.info("Password was updated for user with id: {}", userId);
    }

    @Override
    @Transactional(rollbackFor = AppUserNotFoundException.class)
    public void updateNames(
        Long userId,
        UpdateUserDto updateUserDto
    ) throws AppUserNotFoundException {
        User user = this.findById(userId);
        String updatedFirstName = updateUserDto.getFirstName();
        String updatedLastName = updateUserDto.getLastName();
        if (updatedFirstName != null && updatedFirstName.length() > 0) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updatedLastName != null && updatedLastName.length() > 0) {
            user.setLastName(updatedLastName);
        }
        log.info("Updated names for user with id: {}", userId);
    }

    @Override
    public Page<User> getUsersPaginated(int offset, int pageSize) {
        return this.userRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);

    }
}

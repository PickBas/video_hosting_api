package com.therearenotasksforus.videohostingapi.service.impl;

import com.therearenotasksforus.videohostingapi.dto.user.UpdateUserDto;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Role;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.repositories.RoleRepository;
import com.therearenotasksforus.videohostingapi.repositories.UserRepository;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository repository,
                           ProfileRepository profileRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = repository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(User user) {
        this.checkIfUserExists(user);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setUpdated(new Timestamp(System.currentTimeMillis()));
        Profile profile = new Profile();
        profile.setId(user.getId());
        profile.setCustomUrl(user.getUsername());
        profile.setUser(user);
        profileRepository.save(profile);
        user.setProfile(profile);
        userRepository.save(user);
    }

    @Override
    public void checkIfUserExists(User user) {
        List<User> users = this.getAll();
        for (User iterUser : users) {
            if (user.getEmail().equals(iterUser.getEmail()) ||
                    user.getUsername().equals(iterUser.getUsername())) {
                throw new IllegalStateException("User with such name or email already exists");
            }
        }
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void updateNames(User user, UpdateUserDto updateUserDto) throws ValidationException {
        if (updateUserDto.getFirstName() == null && updateUserDto.getLastName() == null) {
            throw new ValidationException("Failure: wrong data was provided");
        }
        user.setFirstName(updateUserDto.getFirstName() != null ? updateUserDto.getFirstName() : "");
        user.setLastName(updateUserDto.getLastName() != null ? updateUserDto.getLastName() : "");
        user.setUpdated(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByProfile(Profile profile) {
        return userRepository.findByProfile(profile);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}

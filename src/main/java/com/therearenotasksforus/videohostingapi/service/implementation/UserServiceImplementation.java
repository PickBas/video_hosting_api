package com.therearenotasksforus.videohostingapi.service.implementation;

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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, RoleRepository repository, ProfileRepository profileRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = repository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();

        userRoles.add(roleUser);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setUpdated(new Timestamp(System.currentTimeMillis()));
        user.setFirstName("Undecided");
        user.setLastName("Undecided");

        Profile profile = new Profile();

        profile.setCustomUrl(user.getUsername());
        profile.setUser(user);
        profileRepository.save(profile);

        user.setProfile(profile);

        return userRepository.save(user);
    }

    @Override
    public void updateUserToken(User user, String jwtToken) {
        user.setJwtToken(jwtToken);
        userRepository.save(user);
    }

    @Override
    public void updateNames(User user, String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);

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
    public User findByJwtToken(String jwtToken) {
        return userRepository.findByJwtToken(jwtToken);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}

package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.dto.profile.ProfileUpdateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface ProfileService {
    Profile findById(Long id);
    Profile findByCustomUrl(String customUrl);
    Profile findByUser(User user);

    void addOwnedChannel(Profile profile, Channel channel);

    List<Profile> getAll();

    void update(Profile profile, ProfileUpdateDto profileUpdateDto) throws ValidationException;
    void uploadProfileAvatar(Profile profile, MultipartFile file);

    byte[] downloadUserProfileImage(Profile profile);

    void delete(Long id);
    void deleteLikedVideoById(Profile profile, Long id);

}

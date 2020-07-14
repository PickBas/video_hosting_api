package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.dto.profile.ProfileUpdateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
public class ProfileServiceImplementation implements ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImplementation(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Override
    public Profile findById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    @Override
    public Profile findByCustomUrl(String customUrl) {
        return profileRepository.findByCustomUrl(customUrl);
    }

    @Override
    public Profile findByUser(User user) {
        return null;
    }

    @Override
    public void addOwnedChannel(Profile profile, Channel channel) {
        profile.addOwnedChannel(channel);
        profileRepository.save(profile);
    }

    @Override
    public List<Profile> getAll() {
        return profileRepository.findAll();
    }

    @Override
    public void update(Profile profile, ProfileUpdateDto profileUpdateDto) throws ValidationException {
        if (profileUpdateDto.getCustomUrl() == null) {
            throw new ValidationException("Failure: wrong data was provided");
        }

        String aboutProfileInfo = profileUpdateDto.getAboutProfileInfo() != null ? profileUpdateDto.getAboutProfileInfo() : "";
        char gender = profile.getGender() == 'M' || profile.getGender() == 'F' ? profile.getGender() : 'M';
        String country = profileUpdateDto.getCountry() != null ? profileUpdateDto.getCountry() : "";
        String customUrl = profileUpdateDto.getCustomUrl();
        boolean isPrivateSublist = profileUpdateDto.isPrivateSublist();

        profile.setAboutProfileInfo(aboutProfileInfo);
        profile.setGender(gender);
        profile.setCountry(country);
        profile.setCustomUrl(customUrl);
        profile.setIsPrivateSublist(isPrivateSublist);

        profileRepository.save(profile);

    }

    @Override
    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}

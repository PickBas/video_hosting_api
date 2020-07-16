package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.bucket.BucketName;
import com.therearenotasksforus.videohostingapi.dto.profile.ProfileUpdateDto;
import com.therearenotasksforus.videohostingapi.filestore.FileStore;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class ProfileServiceImplementation implements ProfileService {

    private final ProfileRepository profileRepository;
    private final FileStore fileStore;

    @Autowired
    public ProfileServiceImplementation(ProfileRepository profileRepository, FileStore fileStore) {
        this.profileRepository = profileRepository;
        this.fileStore = fileStore;
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
    public void uploadProfileAvatar(Profile profile, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Failure: cannot upload empty file [ " + file.getSize() + "]");
        }
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("Failure: file must be an image [" + file.getContentType() + "]");
        }

        String basicUrl = "https://therearenotasksforus-assets.s3.eu-north-1.amazonaws.com/";

        Map<String, String> metadata = new HashMap<>();

        metadata.put("Content-Type", file.getContentType());
        metadata.put("content-length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), profile.getId());
        String filename = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            profile.setAvatarUrl(basicUrl + profile.getId() + "/" + filename);
            profileRepository.save(profile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}

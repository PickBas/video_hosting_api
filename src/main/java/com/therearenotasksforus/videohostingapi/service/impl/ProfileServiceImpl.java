package com.therearenotasksforus.videohostingapi.service.impl;

import com.therearenotasksforus.videohostingapi.bucket.BucketName;
import com.therearenotasksforus.videohostingapi.dto.profile.ProfileUpdateDto;
import com.therearenotasksforus.videohostingapi.filestore.FileStore;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.models.marks.Like;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final FileStore fileStore;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository,
                              FileStore fileStore) {
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
        return profileRepository.findByUser(user);
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
        this.checkIfCustomUrlProvided(profileUpdateDto);
        String aboutProfileInfo = profileUpdateDto.getAboutProfileInfo() != null ?
                profileUpdateDto.getAboutProfileInfo() : "";
        char gender = profile.getGender() == 'M' || profile.getGender() == 'F' ?
                profile.getGender() : 'M';
        String country = profileUpdateDto.getCountry() != null ?
                profileUpdateDto.getCountry() : "";
        String customUrl = profileUpdateDto.getCustomUrl();
        boolean isPrivateSublist = profileUpdateDto.getPrivateSublist();
        profile.setAboutProfileInfo(aboutProfileInfo);
        profile.setGender(gender);
        profile.setCountry(country);
        profile.setCustomUrl(customUrl);
        profile.setPrivateSublist(isPrivateSublist);
        profileRepository.save(profile);

    }

    private void checkIfCustomUrlProvided(ProfileUpdateDto profileUpdateDto) throws ValidationException {
        if (profileUpdateDto.getCustomUrl() == null) {
            throw new ValidationException("Failure: wrong data was provided");
        }
    }

    @Override
    public void uploadProfileAvatar(Profile profile, MultipartFile file) {
        isEmptyFile(file);
        isImage(file);
        String basicUrl =
                "https://"
                + BucketName.BUCKET.getBucketName()
                + ".s3."
                + BucketName.BUCKET.getBucketRegion()
                + ".amazonaws.com/";
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "_");
        String path = String.format("%s/%s", BucketName.BUCKET.getBucketName(), profile.getId());
        String filename = String.format("%s-%s", UUID.randomUUID(), originalFileName);
        try {
            fileStore.save(path, filename, Optional.of(getMetadata(file)), file.getInputStream());
            profile.setAvatarUrl(basicUrl + profile.getId() + "/" + filename);
            profileRepository.save(profile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, String> getMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("content-length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("Failure: file must be an image [" + file.getContentType() + "]");
        }
    }

    private void isEmptyFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Failure: cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    @Override
    public byte[] downloadUserProfileImage(Profile profile) {
        String path = String.format("%s/%s",
                BucketName.BUCKET.getBucketName(),
                profile.getId());
        String[] pathArr = profile.getAvatarUrl().split("/");
        String filename = pathArr[pathArr.length - 1];
        return fileStore.download(path, filename);
    }

    @Override
    public void delete(Long id) {
        profileRepository.deleteById(id);
    }

    @Override
    public void deleteLikedVideoById(Profile profile, Long id) {
        List<Like> profileLikes = profile.getLikes();
        profileLikes.removeIf(like -> like.getId().equals(id));
        profile.setLikes(profileLikes);
        profileRepository.save(profile);
    }
}

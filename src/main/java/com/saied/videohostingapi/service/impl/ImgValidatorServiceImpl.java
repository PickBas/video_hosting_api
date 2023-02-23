package com.saied.videohostingapi.service.impl;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saied.videohostingapi.exceptions.img.ImageFileIsEmptyException;
import com.saied.videohostingapi.exceptions.img.InvalidImageFormatException;
import com.saied.videohostingapi.exceptions.img.InvalidProvidedImageException;
import com.saied.videohostingapi.service.ImgValidatorService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImgValidatorServiceImpl implements ImgValidatorService {


    @Override
    public void isEmptyFile(MultipartFile file) throws ImageFileIsEmptyException {
        if (file.isEmpty()) {
            throw new ImageFileIsEmptyException("Failure: cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    @Override
    public void isImage(MultipartFile file) throws InvalidImageFormatException {
        if (
            !Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType()
            ).contains(file.getContentType())
        ) {
            throw new InvalidImageFormatException("Failure: file must be an image [" + file.getContentType() + "]");
        }
    }

    @Override
    public void isValidImage(MultipartFile file) throws InvalidProvidedImageException {
        try {
            this.isEmptyFile(file);
            this.isImage(file);
        } catch (InvalidImageFormatException | ImageFileIsEmptyException e) {
            throw new InvalidProvidedImageException(e.getMessage());
        }
    }
}

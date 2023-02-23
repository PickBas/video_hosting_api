package com.saied.videohostingapi.service;

import org.springframework.web.multipart.MultipartFile;

import com.saied.videohostingapi.exceptions.img.ImageFileIsEmptyException;
import com.saied.videohostingapi.exceptions.img.InvalidImageFormatException;
import com.saied.videohostingapi.exceptions.img.InvalidProvidedImageException;

public interface ImgValidatorService {

    /**
     * Checking if img file is not empty
     * @param file MultipartFile
     * @throws ImageFileIsEmptyException if file is empty
     */
    void isEmptyFile(MultipartFile file) throws ImageFileIsEmptyException;

    /**
     * Checking if image is JPEG or PNG
     * @param file MultipartFile
     * @throws InvalidImageFormatException if invalid image was provided
     */
    void isImage(MultipartFile file) throws InvalidImageFormatException;

    /**
     * Performing isEmptyFile and isImage checks
     * @param file MultipartFile
     * @throws InvalidProvidedImageException if image is invalid
     */
    void isValidImage(MultipartFile file) throws InvalidProvidedImageException;
}

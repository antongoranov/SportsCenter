package com.sportscenter.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadUserProfilePicture(MultipartFile filePicture) throws IOException;

    void deleteUserOldProfilePicture(String publicId) throws IOException;
}

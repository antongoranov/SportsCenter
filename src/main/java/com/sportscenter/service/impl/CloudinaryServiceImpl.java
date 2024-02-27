package com.sportscenter.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sportscenter.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadUserProfilePicture(MultipartFile filePicture) throws IOException {

        String fileName =
                filePicture.getOriginalFilename().substring(0, filePicture.getOriginalFilename().lastIndexOf('.'));

        Map uploadResult =
                cloudinary.uploader()
                        .upload(
                                filePicture.getBytes(),
                                ObjectUtils.asMap("public_id", "sportscenter/users/" + fileName));

        return uploadResult.get("url").toString();
    }

    @Override
    public void deleteUserOldProfilePicture(String publicId) throws IOException {
            cloudinary.uploader()
                    .destroy(publicId, ObjectUtils.emptyMap());
    }
}

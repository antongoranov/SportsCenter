package com.sportscenter.model.binding;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserPictureBindingModel {

    private MultipartFile filePicture;
}

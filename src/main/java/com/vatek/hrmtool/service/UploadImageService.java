package com.vatek.hrmtool.service;

import com.vatek.hrmtool.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {

    String uploadAvatarImage(MultipartFile multipartFile, UserEntity userEntity);
}

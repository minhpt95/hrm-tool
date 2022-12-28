package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.CommonConstant;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.service.S3Service;
import com.vatek.hrmtool.service.UploadImageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Log4j2
public class UploadImageServiceImpl implements UploadImageService {
    private S3Service s3Service;

    @Override
    public String uploadAvatarImage(MultipartFile multipartFile, UserEntity userEntity){

        String avatarImagePath =
                CommonConstant.AWS_S3_HRM_TOOL_FOLDER +
                "/" +
                CommonConstant.AWS_S3_AVATAR_IMAGES_FOLDER +
                "/" +
                userEntity.getId() +
                "/" +
                multipartFile.getOriginalFilename();


        String objectUrl = s3Service.putData(avatarImagePath, multipartFile);

        return objectUrl;
    }}

package com.vatek.hrmtool.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String putData(String pathFile, MultipartFile file);
    void deleteData();
}
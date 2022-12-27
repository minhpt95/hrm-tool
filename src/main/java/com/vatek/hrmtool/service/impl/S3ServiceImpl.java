package com.vatek.hrmtool.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Region;
import com.vatek.hrmtool.constant.CommonConstant;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.service.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;


@Service
@AllArgsConstructor
@Log4j2
public class S3ServiceImpl implements S3Service {

    private AmazonS3 s3;
    private Environment env;

    @PostConstruct
    private void createBucket(){
        log.info("start createBucket()");
        var nameBucket = env.getProperty(CommonConstant.AWS_S3_BUCKET);

        if(s3.doesBucketExistV2(nameBucket)){
            log.info("bucket with name : {} has been created",() -> nameBucket);
            return;
        }

        CreateBucketRequest createBucketRequest = new CreateBucketRequest(nameBucket, Region.fromValue(env.getProperty(CommonConstant.AWS_S3_REGION)));
        var createdBucket = s3.createBucket(createBucketRequest);
        log.info("create bucket with name : {}",() -> createdBucket);
    }


    @Override
    public String putData(String pathFile, MultipartFile file) {
        try{
            log.info("start putData()");
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getResource().contentLength());

            var nameBucket = env.getProperty(CommonConstant.AWS_S3_BUCKET);

            var putS3Object = s3.putObject(nameBucket,pathFile,file.getInputStream(),objectMetadata);

            log.info("put data to S3 result : {}",() -> putS3Object);

            return env.getProperty("aws.s3.object.url") + pathFile;
        }catch (Exception e){
            log.error("Error when put object to S3",e);
            throw new ProductException(
                    ErrorResponse
                    .builder()
                    .code("")
                    .message("")
                    .type("")
                    .build()
            );
        }
    }

    @Override
    public void deleteData() {

    }
}

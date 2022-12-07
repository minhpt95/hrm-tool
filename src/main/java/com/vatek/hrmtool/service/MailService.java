package com.vatek.hrmtool.service;

import com.vatek.hrmtool.entity.UserEntity;
import org.springframework.scheduling.annotation.Async;

import java.io.File;

public interface MailService {
    void sendEmailAttach(String to, String subject, String body, String fileName, File file) throws Exception;

    void sendEmailAttach(String[] to, String subject, String body, String fileName, File file) throws Exception;

    void sendEmail(String to, String subject, String body) throws Exception;

    void sendEmail(String [] to, String subject, String body) throws Exception;

    void sendEmail(String [] to, String from, String personal, String subject, String body) throws Exception;

    @Async
    void sendActivationEmail(UserEntity to);

    void sendForgotEmail(UserEntity to,String newPassword);
}

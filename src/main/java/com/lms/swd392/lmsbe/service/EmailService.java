package com.lms.swd392.lmsbe.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}

package com.example.template.service;

import com.example.template.dto.request.VerifyNewPasswordRequest;
import com.example.template.entity.User;
import com.example.template.entity.VerificationCode;

public interface VerifyCodeService {

     void delete (VerificationCode verificationCode);

     VerificationCode findByEmail(String email);

     VerificationCode save(VerificationCode verificationCode);

     Boolean isTimeOutRequired(VerificationCode verificationCode, long ms);

     User verifyRegister(String code);

     User verifyForgotPassword(VerifyNewPasswordRequest request);
}
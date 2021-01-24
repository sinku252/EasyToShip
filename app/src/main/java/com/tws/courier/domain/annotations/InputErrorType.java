package com.tws.courier.domain.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({
        InputErrorType.EMAIL, InputErrorType.PASSWORD, InputErrorType.VRM_NUMBER, InputErrorType.NAME, InputErrorType.PHONE,
        InputErrorType.DATE_OF_BIRTH, InputErrorType.GENDER, InputErrorType.CONFIRM_PASSWORD, InputErrorType.ACCEPT_TERMS,
        InputErrorType.OTP_EMAIL
})
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface InputErrorType {
    int EMAIL = 1;
    int PASSWORD = 2;
    int VRM_NUMBER = 3;
    int NAME = 4;
    int PHONE = 5;
    int DATE_OF_BIRTH = 6;
    int GENDER = 7;
    int CONFIRM_PASSWORD = 8;
    int ACCEPT_TERMS = 9;
    int OTP_EMAIL = 10;
    int MESSAGE = 11;
    int SUBJECT = 12;

}

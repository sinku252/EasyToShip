package com.tws.courier.domain.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({DataRequestType.USER_LOGIN, DataRequestType.USER_REGISTER, DataRequestType.FORGOT_PASSWORD,
        DataRequestType.GET_ADDRESS_LIST,DataRequestType.ADD_ADDRESS,DataRequestType.GET_DASHDATA,DataRequestType.GET_ORDERS,
        DataRequestType.GET_GENERAL,DataRequestType.GET_BANK,DataRequestType.GET_HOME_SLIDER,DataRequestType.ADD_GENERAL_DETAILS,
        DataRequestType.ADD_BANK_DETAILS, DataRequestType.UPDATE_PROFILE_IMAGE,DataRequestType.VIEW_ORDER_PRICE,DataRequestType.PLACE_ORDER,DataRequestType.DELETE_ADDRESS,DataRequestType.UPDATE_PASSWORD,DataRequestType.GET_ORDER_CALCULATION_DETAILS,
        DataRequestType.ADD_TICKET,DataRequestType.GET_TICKET_LIST,DataRequestType.UPDATE_FCM_TOKEN,DataRequestType.SEND_MSG,DataRequestType.GET_CHAT_LIST

})
public @interface DataRequestType {

    int USER_LOGIN = 101;
    int USER_REGISTER = 102;
    int FORGOT_PASSWORD = 103;
    int GET_ADDRESS_LIST = 104;
    int ADD_ADDRESS = 105;
    int GET_DASHDATA = 106;
    int GET_ORDERS = 107;
    int GET_GENERAL= 108;
    int GET_BANK= 109;
    int GET_HOME_SLIDER= 110;
    int ADD_GENERAL_DETAILS= 111;
    int ADD_BANK_DETAILS= 112;
    int UPDATE_PROFILE_IMAGE= 113;
    int VIEW_ORDER_PRICE= 114;
    int PLACE_ORDER= 115;
    int DELETE_ADDRESS = 116;
    int UPDATE_PASSWORD = 117;
    int GET_ORDER_CALCULATION_DETAILS = 118;
    int STEP4_VALIDATION = 119;
    int ADD_TICKET = 120;
    int GET_TICKET_LIST = 121;
    int UPDATE_FCM_TOKEN = 122;
    int SEND_MSG = 123;
    int GET_CHAT_LIST = 124;
    int STEP1_COUNTRY_VALIDATION = 125;



}

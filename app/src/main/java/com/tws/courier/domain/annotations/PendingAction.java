package com.tws.courier.domain.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({PendingAction.NONE, PendingAction.TICKET_PHOTO, PendingAction.TICKET_PHOTO_LIST, PendingAction.TICKET_PHOTO_SUMMARY})
public @interface PendingAction {

    int NONE = -11;
    int TICKET_PHOTO = 1;
    int TICKET_PHOTO_LIST = 2;
    int TICKET_PHOTO_SUMMARY = 3;

}

package com.tws.courier.domain.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({PhotoPendingAction.NONE, PhotoPendingAction.TICKET_PHOTO, PhotoPendingAction.PHOTO_LIST})
public @interface PhotoPendingAction {

    int NONE = -11;
    int TICKET_PHOTO = 1;
    int PHOTO_LIST = 2;

}

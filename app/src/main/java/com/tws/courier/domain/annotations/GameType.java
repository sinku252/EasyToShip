package com.tws.courier.domain.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({GameType.ALL, GameType.NORMAL, GameType.JACKPOT, GameType.INSTANT})
public @interface GameType {

    int ALL = 1;
    int NORMAL = 2;
    int JACKPOT = 3;
    int INSTANT = 4;
}

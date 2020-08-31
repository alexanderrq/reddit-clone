package com.aramirez.redditclone.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String ACTIVATION_EMAIL = "http://localhost:8080/api/auth/accountVerification";
    public static final Long JWT_EXPIRATION_TIME = Long.valueOf(90000);
}

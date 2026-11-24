package com.bdms.backend.util;

public final class Constants {

    private Constants() {
    }

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_DONOR = "DONOR";
    public static final String ROLE_HOSPITAL = "HOSPITAL";

    public static final int DONOR_ELIGIBILITY_GAP_DAYS = 90;
}

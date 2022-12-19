package com.vatek.hrmtool.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN(Code.ADMIN),

    IT_ADMIN(Code.IT_ADMIN),
    PROJECT_MANAGER(Code.PROJECT_MANAGER),
    USER(Code.USER);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static class Code {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String IT_ADMIN = "IT_ADMIN";
        public static final String PROJECT_MANAGER = "ROLE_PM";
        public static final String USER = "ROLE_USER";


    }
}
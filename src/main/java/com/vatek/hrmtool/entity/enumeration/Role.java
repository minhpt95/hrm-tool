package com.vatek.hrmtool.entity.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN(Code.ADMIN),

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
        public static final String PROJECT_MANAGER = "ROLE_PM";
        public static final String USER = "ROLE_USER";


    }
}
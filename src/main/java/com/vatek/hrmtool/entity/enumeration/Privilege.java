package com.vatek.hrmtool.entity.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Privilege implements GrantedAuthority {
    READ(Privilege.Code.READ),

    WRITE(Privilege.Code.WRITE),
    DELETE(Privilege.Code.DELETE);

    private final String authority;
    Privilege(String code) {
        this.authority = code;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static class Code {
        public static final String READ = "READ";
        public static final String WRITE = "WRITE";
        public static final String DELETE = "DELETE";


    }
}

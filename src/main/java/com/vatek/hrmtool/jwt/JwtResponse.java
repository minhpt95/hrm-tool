package com.vatek.hrmtool.jwt;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private Long id;
    private String accessToken;
    private String type = "Bearer";
    private String name;
    private String email;
    private String refreshToken;
    private List<String> roles;
    private List<String> privileges;

    public JwtResponse(String accessToken, String refreshToken, Long id, String name, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles,List<String> privileges) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = username;
        this.email = email;
        this.roles = roles;
        this.privileges = privileges;
    }

}

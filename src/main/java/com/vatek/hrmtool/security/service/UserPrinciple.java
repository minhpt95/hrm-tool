package com.vatek.hrmtool.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vatek.hrmtool.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Builder(builderMethodName = "internalBuilder")
public class UserPrinciple implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String username;

    private String email;
    @JsonIgnore
    private String password;

    private boolean isEnabled;

    private Long remainTime;

    private String accessToken;

    private Collection<? extends GrantedAuthority> authorities;

    private Collection<String> roles;

    private Collection<String> privileges;


    public static UserPrincipleBuilder userPrincipleBuilder(UserEntity userEntity) {

        return internalBuilder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getEmail())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .isEnabled(userEntity.isEnabled())
                .accessToken(userEntity.getAccessToken());
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}

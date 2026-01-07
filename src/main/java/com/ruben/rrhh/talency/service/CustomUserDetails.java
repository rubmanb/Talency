package com.ruben.rrhh.talency.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

//    private final Long userId;
    private final String email;
    private final String companyName;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean active;

    public CustomUserDetails(
//            Long userId,
            String email,
            String companyName,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean active
    ) {
//        this.userId = userId;
        this.email = email;
        this.companyName = companyName;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
    }

    /** Spring sigue usando esto internamente */
    @Override
    public String getUsername() {
        return companyName + "|" + email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return active; }
}

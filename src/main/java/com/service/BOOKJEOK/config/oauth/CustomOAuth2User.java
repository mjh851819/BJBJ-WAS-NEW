package com.service.BOOKJEOK.config.oauth;

import com.service.BOOKJEOK.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User{

    private String registrationId;
    private User user;
    private Map<String, Object> attributes;

    public CustomOAuth2User() {
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String role = user.getRole().value();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        authorities.add(grantedAuthority);
        return authorities;
    }

    @Override
    public String getName() {
        return registrationId;
    }

    public User getUser() {
        return user;
    }

}

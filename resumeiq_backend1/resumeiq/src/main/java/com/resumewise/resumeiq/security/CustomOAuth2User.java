package com.resumewise.resumeiq.security;

import com.resumewise.resumeiq.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OidcUser {

    private final OidcUser delegate;
    private final User user;

    public CustomOAuth2User(OidcUser delegate, User user) {
        this.delegate = delegate;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }
}
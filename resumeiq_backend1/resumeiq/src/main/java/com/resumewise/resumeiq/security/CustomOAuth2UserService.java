package com.resumewise.resumeiq.security;

import com.resumewise.resumeiq.entity.User;
import com.resumewise.resumeiq.repository.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest)
            throws OAuth2AuthenticationException {

        // Let Spring handle the Google OIDC authentication first.
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String name = oidcUser.getAttribute("name");
        String picture = oidcUser.getAttribute("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException(
                    "Google account has no accessible email address."
            );
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {

            // First Google login.
            user = new User(
                    name,
                    email,
                    User.AuthProvider.GOOGLE,
                    picture
            );

            userRepository.save(user);

        } else if (user.getAuthProvider() == User.AuthProvider.LOCAL) {

            // Do not silently convert a local account into a Google account.
            throw new OAuth2AuthenticationException(
                    "An account with this email already exists. " +
                            "Please log in with your password instead."
            );
        }

        // Wrap Google's OIDC user with our application's database user.
        return new CustomOAuth2User(oidcUser, user);
    }
}
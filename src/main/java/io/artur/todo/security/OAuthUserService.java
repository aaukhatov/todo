package io.artur.todo.security;

import io.artur.todo.data.User;
import io.artur.todo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class OAuthUserService extends DefaultOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(OAuthUserService.class);

    private final UserRepository userRepository;
    private final Clock systemUTC = Clock.systemUTC();

    @Autowired
    public OAuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        try {
            User user = getUser(oauth2User);
            UserPrincipal userPrincipal = new UserPrincipal(user.getId(), oauth2User.getAttributes());
            userPrincipal.setAuthenticated(true);
            return userPrincipal;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private User getUser(OAuth2User oauth2User) {
        return userRepository.findByEmail((String) oauth2User.getAttributes().get("email"))
                .orElseGet(() -> {
                    User user = new User(
                            UUID.randomUUID().toString(),
                            (String) oauth2User.getAttributes().get("email"),
                            (String) oauth2User.getAttributes().get("name"),
                            Instant.now(systemUTC),
                            null
                    );
                    userRepository.save(user);
                    logger.info("New {} has been registered", user);
                    return user;
                });
    }
}

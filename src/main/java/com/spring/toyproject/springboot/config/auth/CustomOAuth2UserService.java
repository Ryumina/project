package com.spring.toyproject.springboot.config.auth;

import com.spring.toyproject.springboot.config.auth.dto.OAuthAttributes;
import com.spring.toyproject.springboot.config.auth.dto.SessionUser;
import com.spring.toyproject.springboot.domain.user.User;
import com.spring.toyproject.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인을 하는 서비스가 구글인지, 네이버인지 구분하기 위함
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 로그인 진행 시 key가 되는 필드값 pk와 같은 의미 (구글의 기본 코드는 sub)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        // 왜 user 클래스를 사용하지 않고 new를 통해 새로 만들어서 쓰는가? (실제로 user 그대로 사용하면 로그인시 에러가 난다.)
        // user 클래스를 그대로 사용하면 직렬화를 구현하지 않았다는 내용의 오류가 뜨게 되는데
        // 그렇다면 user 클래스에 직렬화 코드를 넣으면 되나? 하고 생각하겠지만
        // user 클래스는 entity이다. 언제 어떤 엔티티와 관계가 형성될 지 모른다. 만약 자식 엔티티를 가지게 된다면
        // 자식 엔티티 모두 직렬화 대상에 포함되어 성능 이슈, 부수 효과가 발생할 수 있다.
        // 따라서 직렬화 기능을 가진 새로운 세션 dto를 추가로 사용하여 진행하는 것이 추후 운영 및 유지보수 때 많은 도움이 된다.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}

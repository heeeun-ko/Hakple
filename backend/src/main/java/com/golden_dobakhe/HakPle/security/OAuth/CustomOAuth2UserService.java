package com.golden_dobakhe.HakPle.security.OAuth;


import com.golden_dobakhe.HakPle.domain.user.user.entity.User;
import com.golden_dobakhe.HakPle.global.Status;
import com.golden_dobakhe.HakPle.security.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

//requestResolver에서 로그인이 완료돠면 자동으로 이쪽으로 와서 처리하게 된다

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AuthService testUserService;


    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        //OAuth에서 가져온 유저의 객체를 가져오고
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //인증 아이디를 이름에서 가져온다
        String oauthId = oAuth2User.getName();

        //인증의 공급자를 가져온다
        String providerTypeCode = userRequest
                .getClientRegistration() // ClientRegistration
                .getRegistrationId()     // String
                .toUpperCase(Locale.getDefault());

        //유저의 정보를 가져온다
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, String> attributesProperties = (Map<String, String>) attributes.get("properties");

        //가져온 정보에서 닉네임과 프사를 가져온다
        String nickname = attributesProperties.get("nickname");
        String profileImgUrl = attributesProperties.get("profile_image");
        //유저명은 이렇게 설정을 해둔다
        String username = providerTypeCode + "__" + oauthId;
        System.out.println("이미지 : " + profileImgUrl);


        //그리고 가입
        User user = testUserService.modifyOrJoin(username, nickname, profileImgUrl);


        //그리고 시큐리티에게 알려줄 객체를 만든다

        return new SecurityUser(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getNickName(),
                Status.ACTIVE,
                List.of(new SimpleGrantedAuthority("ROLE_ACTIVE"))
        );
    }
}

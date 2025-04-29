package com.golden_dobakhe.HakPle.security;

import com.golden_dobakhe.HakPle.domain.user.user.entity.User;
import com.golden_dobakhe.HakPle.domain.user.user.repository.UserRepository;
import com.golden_dobakhe.HakPle.global.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 또는 TestRepository

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUserNameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new com.golden_dobakhe.HakPle.security.CustomUserDetails(user); // 이 시점에 roles가 null이면 안 됨!
    }

    //user객체만 가져오는 방식이랑 원하는 부분만 가져오는 방식에서 문제가 생겨서 이렇게 나누었습니다
    @Getter
    public static class CustomUserDetails implements UserDetails {
        private final String username;
        private final String nickname;
        private final Status status;
        private final List<GrantedAuthority> authorities;

        public CustomUserDetails(String username, String nickname, Status status, List<GrantedAuthority> roles) {
            this.username = username;
            this.nickname = nickname;
            this.status = status;
            this.authorities = roles;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.authorities;
        }

        @Override
        public String getPassword() {
            return "";

        }

        @Override
        public String getUsername() {

            return this.username; // 유저 이름

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
        public boolean isEnabled() {
            return true;
        }
    }
}

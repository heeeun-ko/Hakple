package com.golden_dobakhe.HakPle.domain.user.user.service;

import com.golden_dobakhe.HakPle.domain.user.exception.UserErrorCode;
import com.golden_dobakhe.HakPle.domain.user.exception.UserException;
import com.golden_dobakhe.HakPle.domain.user.user.entity.User;
import com.golden_dobakhe.HakPle.domain.user.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFindService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 아이디(Username) 찾기
    public String findUserNameByPhoneNum(String phoneNum) {
        return userRepository.findByPhoneNum(phoneNum)
                .map(User::getUserName)
                .orElseThrow(() -> new IllegalArgumentException("해당 전화번호로 등록된 아이디가 없습니다."));
    }

    //비밀번호 변경 (로그인한 사용자가 내 정보에서 비밀번호 변경)
    public void changePasswordWithOldPassword(Long userId, String currentPassword, String newPassword,
                                              String newPasswordConfirm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 현재 비밀번호 일치 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserException(UserErrorCode.WRONG_CURRENT_PASSWORD);
        }

        // 새 비밀번호 확인
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new UserException(UserErrorCode.PASSWORD_CONFIRM_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setModificationTime(LocalDateTime.now());
        userRepository.save(user);
    }

    //비밀번호 찾기 후 문자 인증이 완료된 경우 비밀번호 재설정
    public void resetPassword(Long userId, String newPassword, String newPasswordConfirm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 새 비밀번호 확인
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new UserException(UserErrorCode.PASSWORD_CONFIRM_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setModificationTime(LocalDateTime.now());
        userRepository.save(user);
    }
}
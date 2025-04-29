package com.golden_dobakhe.HakPle.security.utils;

import com.golden_dobakhe.HakPle.domain.user.user.entity.User;
import com.golden_dobakhe.HakPle.security.CustomUserDetails;
import com.golden_dobakhe.HakPle.security.CustomUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * 현재 로그인된 사용자의 Authentication 반환
     */
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 현재 로그인 여부 확인
     */
    public static boolean isLoggedIn() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"));
    }

    /**
     * 현재 로그인한 사용자의 username 반환
     */
    public static String getCurrentUsername() {
        if (!isLoggedIn()) return null;

        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetailsService.CustomUserDetails) {
            return ((CustomUserDetailsService.CustomUserDetails) principal).getUsername();
        }

        return principal.toString();
    }

    /**
     * 현재 로그인한 사용자의 userId 반환
     */
    public static Long getCurrentUserId() {
        if (!isLoggedIn()) return null;

        Object principal = getAuthentication().getPrincipal();


        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserId();
// =======
//         if (principal instanceof CustomUserDetails) {
//             return ((CustomUserDetails) principal).getUserId();
// >>>>>>> develop
        }

        return null;
    }

    /**
     * 현재 로그인한 사용자의 userId 반환 (Optional 사용)
     */
    public static java.util.Optional<Long> getOptionalCurrentUserId() {
        return java.util.Optional.ofNullable(getCurrentUserId());
    }

    /**
     * 현재 로그인한 User 엔티티 반환
     */
    public static User getCurrentUser() {
        if (!isLoggedIn()) return null;

        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
// =======
//         if (principal instanceof CustomUserDetails) {
//             return ((CustomUserDetails) principal).getUser();
// >>>>>>> develop
        }

        return null;
    }
}


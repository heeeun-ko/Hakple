package com.golden_dobakhe.HakPle.domain.user.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 시 입력하는 사용자 정보 DTO")
public class UserRegistRequestDTO {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Size(min = 4, max = 15, message = "아이디는 4~15자 사이여야 합니다.")
    @Schema(description = "사용자 아이디 (4~15자)", example = "hakpleUser1")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자 사이여야 합니다.")
    @Schema(description = "비밀번호 (8~15자)", example = "password123!")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9._-]{2,20}$",
            message = "닉네임은 한글/영문/숫자와 특수기호 _, -, .만 사용할 수 있으며 공백 없이 2~20자여야 합니다."
    )
    @Schema(description = "닉네임 (2~20자, 한글/영문/숫자/특수기호(_,-,.)조합 가능)", example = "golden_user_99")
    private String nickName;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^01[0-9]{1}-?[0-9]{3,4}-?[0-9]{4}$",
            message = "전화번호는 10~11자리 숫자만 입력 가능합니다. (하이픈 생략 가능)"
    )
    @Schema(description = "휴대폰 번호 (하이픈 - 생략)", example = "01012345678")
    private String phoneNum;
}

package com.golden_dobakhe.HakPle.domain.user.myInfo.controller;

import com.golden_dobakhe.HakPle.domain.user.myInfo.dto.AcademyCodeRequestDto;
import com.golden_dobakhe.HakPle.domain.user.myInfo.service.AcademyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/academies")
@Tag(name = "Academy Controller", description = "학원 등록 관리 API")
public class ApiV1AcademyController {
    private final AcademyService academyService;

    @Operation(
            summary = "학원 등록",
            description = "사용자가 학원 코드를 입력하여 학원을 등록됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "학원 등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 학원 코드"),
            @ApiResponse(responseCode = "404", description = "해당하는 학원을 찾을 수 없음")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerAcademy(
            @RequestBody AcademyCodeRequestDto dto, Authentication authentication
    ) {
        String userName = authentication.getName(); // JWT 필터에서 유저네임 꺼내옴
        String academyName = academyService.registerAcademy(userName, dto.getAcademyCode());
        return ResponseEntity.ok("학원이 등록되었습니다: " + academyName);
    }
}
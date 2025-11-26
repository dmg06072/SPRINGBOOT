package com.example.demo.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Swagger UI: http://localhost:8099/swagger-ui.html
OpenAPI JSON: http://localhost:8099/v3/api-docs

@Tag(name, description) : 컨트롤러 단위 그룹화
@Operation(summary, description, parameters, responses, requestBody) : 메소드 단위 API 설명
@Parameter : 파라미터 설명 (path, query 등)
@ApiResponse : 응답 코드별 설명과 예시
@Schema : DTO/VO 필드 스펙 정의
@ExampleObject : 실제 예시 JSON 제공
@RequestBody : 요청 바디 설명

 */
@Controller
@Slf4j
@RequestMapping("/test")
@Tag(name = "Test API", description = "Swagger 문서화 예시 컨트롤러")
@SecurityRequirement(name = "bearerAuth") // JWT 인증 헤더 전역 적용
@SecurityScheme(                         // 전역 보안 스키마 정의
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerAPITestController {
    // 1) 사용자 단건 조회
    @GetMapping("/{id}")
    @Operation(
            summary = "사용자 조회",
            description = "ID로 사용자 정보를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "id", description = "사용자 ID", required = true,
                            in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "101")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "사용자 없음")
            }
    )
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(new UserResponse(id, "홍길동", "hong@test.com"));
    }

    // 2) 사용자 전체 조회
    @GetMapping
    @Operation(
            summary = "사용자 전체 조회",
            description = "등록된 모든 사용자 목록을 반환합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    )
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(List.of(
                new UserResponse(1L, "홍길동", "hong@test.com"),
                new UserResponse(2L, "이몽룡", "lee@test.com")
        ));
    }

    // 3) 사용자 생성
    @PostMapping
    @Operation(
            summary = "사용자 생성",
            description = "새로운 사용자를 등록합니다.",
            // ⚠ Swagger 문서화를 위한 RequestBody는 io.swagger.v3.oas.annotations.parameters.RequestBody 사용
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성할 사용자 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "기본 예시",
                                            value = "{ \"name\": \"성춘향\", \"email\": \"sung@test.com\" }"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    public ResponseEntity<String> createUser(@RequestBody UserRequest req) {
        return ResponseEntity.status(201).body("생성 완료");
    }

    // 4) 사용자 삭제
    @DeleteMapping("/{id}")
    @Operation(
            summary = "사용자 삭제",
            description = "ID에 해당하는 사용자를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "대상 없음")
            }
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    // DTO 클래스들
    @Data
    static class UserRequest {
        @Schema(description = "이름", example = "홍길동")
        private String name;

        @Schema(description = "이메일", example = "hong@test.com")
        private String email;
    }

    @Data
    static class UserResponse {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        @Schema(description = "이메일", example = "hong@test.com")
        private String email;

        public UserResponse(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }
}

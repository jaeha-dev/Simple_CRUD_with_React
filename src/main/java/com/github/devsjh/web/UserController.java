package com.github.devsjh.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @title  : User 컨트롤러 클래스
 * @author : jaeha-dev (Git)
 */
@RestController
@RequestMapping("/api")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ClientRegistration registration;

    public UserController(ClientRegistrationRepository registrationRepository) {
        this.registration = registrationRepository.findByRegistrationId("okta");
    }

    /**
     * 사용자 인증 확인
     * @param user : okta 사용자 정보 및 권한(ROLE_USER, openid, email, profile)
     * @return     : okta 로그인 인증 결과
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User user) {
        log.info("Auth info {}", user);

        if (user == null) {
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return ResponseEntity.ok().body(user.getAttributes());
        }
    }

    /**
     * 로그아웃
     * @param request : 요청
     * @param idToken : 토큰
     * @return        : 상태 코드(200 Ok) 및 로그아웃 URL, 토큰 정보
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken) {

        // Send logout URL to client so they can initiate logout
        String logoutUrl = this.registration.getProviderDetails()
                                            .getConfigurationMetadata().get("end_session_endpoint").toString();

        Map<String, String> logoutDetails = new HashMap<>();
        logoutDetails.put("logoutUrl", logoutUrl);
        logoutDetails.put("idToken", idToken.getTokenValue());
        request.getSession(false).invalidate();

        log.info("Logout result info {}", ResponseEntity.ok().body(logoutDetails));
        return ResponseEntity.ok().body(logoutDetails);
    }
}
package com.spvue;


import jakarta.servlet.http.HttpSession;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
=======
import org.springframework.http.ResponseEntity;
>>>>>>> 606195535784410e6328c18bf255deb95ccbdccd
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @GetMapping("/api/check-connection")
    public ResponseEntity<String> checkConnection(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
        }
        return ResponseEntity.ok("Connection is alive");
    }

    @GetMapping("/api/check-connection")
    public ResponseEntity<String> checkConnection() {
        return ResponseEntity.ok("Connection is alive");
    }

//    @GetMapping("/renew-session")
//    public ResponseEntity<Map<String, Object>> renewSession(HttpSession session) {
//        // 세션의 최대 비활성화 간격을 갱신
//        session.setMaxInactiveInterval(60 * 1); // 1분
//
//        // 세션 발급 시간
//        Instant creationTime = Instant.ofEpochMilli(session.getCreationTime());
//        // 세션 만료 시간
//        Instant expirationTime = creationTime.plusSeconds(session.getMaxInactiveInterval());
//
//        // 남은 만료 시간 계산 (밀리초 단위)
//        long remainingTime = expirationTime.toEpochMilli() - Instant.now().toEpochMilli();
//
//        // 응답 데이터로 세션 정보를 포함
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("message", "Session renewed");
//        responseData.put("creationTime", creationTime.toString());
//        responseData.put("expirationTime", expirationTime.toString());
//        responseData.put("remainingTime", remainingTime);
//
//        return ResponseEntity.ok(responseData);
//    }




    @GetMapping("/api/session-info")
    public ResponseEntity<SessionInfo> getSessionInfo(HttpSession session) {

        System.out.println(session);
        // 세션이 null인지 확인
        if (session == null) {
            return ResponseEntity.status(400).body(null); // 세션이 없으면 400 Bad Request
        }

        // 세션 발급 시간
        Instant creationTime = Instant.ofEpochMilli(session.getCreationTime());
        // 세션 만료 시간
        Instant expirationTime = creationTime.plusSeconds(session.getMaxInactiveInterval());

        // 남은 만료 시간 계산 (밀리초 단위)
        long remainingTime = expirationTime.toEpochMilli() - Instant.now().toEpochMilli();

        return ResponseEntity.ok(new SessionInfo(creationTime, expirationTime, remainingTime));
    }



    public static class SessionInfo {
        private final Instant creationTime;
        private final Instant expirationTime;
        private final long remainingTime;

        public SessionInfo(Instant creationTime, Instant expirationTime, long remainingTime) {
            this.creationTime = creationTime;
            this.expirationTime = expirationTime;
            this.remainingTime = remainingTime;
        }

        public Instant getCreationTime() { return creationTime; }
        public Instant getExpirationTime() { return expirationTime; }
        public long getRemainingTime() { return remainingTime; }
    }


}

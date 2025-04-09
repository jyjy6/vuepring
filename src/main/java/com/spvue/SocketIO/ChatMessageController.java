package com.spvue.SocketIO;

import com.spvue.Auth.OAuth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;

    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(@RequestParam String room, Authentication auth) {
        if (auth.isAuthenticated()) {
            String username = ((CustomUserDetails)auth.getPrincipal()).getUsername();
            List<ChatMessageEntity> returnRoom = chatMessageRepository.findByRoomOrderByTimestampAsc(room);

            // 해당 채팅방에 현재 인증된 사용자의 메시지가 있는지 확인
            boolean userExists = returnRoom.stream()
                    .anyMatch(message -> message.getUsername().equals(username));

            // 해당 사용자가 채팅방에 존재하는 경우에만 채팅 기록 반환
            if (userExists) {
                return ResponseEntity.ok(returnRoom);
            }

            // 사용자가 채팅방에 존재하지 않으면 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("유저정보가 다릅니다");
        }

        // 인증되지 않은 경우 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("인증되지 않은 사용자입니다");
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allchat")
    public ResponseEntity<?> allChat(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {

        return chatMessageService.returnAllChat(page, size, status);
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/status/{room}")
    public ResponseEntity<?> updateChatStatus(
            @PathVariable String room,
            @RequestBody Map<String, String> payload) {

        return chatMessageService.handleChatStatus(room, payload);
    }

}

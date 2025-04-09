package com.spvue.SocketIO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class ChatMessageService {


    private final ChatMessageRepository chatMessageRepository;

    public void saveMessage(ChatMessage message) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setUsername(message.getUsername());
        entity.setMessage(message.getMessage());
        entity.setRoom(message.getRoom());
        entity.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(entity);
    }

    public ResponseEntity<?> returnAllChat(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status){
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<ChatMessageEntity> chatPage;

        if (status != null && !status.isEmpty()) {
            // 상담 상태에 따른 필터링 로직 추가 필요
            // 이를 위해 ChatMessageEntity에 상태 필드가 필요함
            chatPage = chatMessageRepository.findLatestMessagesByRoomAndStatus(status, pageable);
        } else {
            chatPage = chatMessageRepository.findLatestMessagesByRoom(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("items", chatPage.getContent());
        response.put("currentPage", chatPage.getNumber());
        response.put("totalItems", chatPage.getTotalElements());
        response.put("totalPages", chatPage.getTotalPages());

        return ResponseEntity.ok(response);

    }

    public ResponseEntity<?> handleChatStatus(
            @PathVariable String room,
            @RequestBody Map<String, String> payload) {

        String status = payload.get("status");
        if (status == null || status.isEmpty()) {
            return ResponseEntity.badRequest().body("상태 값이 필요합니다.");
        }

        // 해당 채팅방의 모든 메시지 상태 업데이트
        List<ChatMessageEntity> messages = chatMessageRepository.findByRoomOrderByTimestampAsc(room);
        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (ChatMessageEntity message : messages) {
            message.setStatus(status);
            chatMessageRepository.save(message);
        }

        return ResponseEntity.ok().body(Map.of("status", "success", "message", "상태가 업데이트되었습니다."));
        }


}

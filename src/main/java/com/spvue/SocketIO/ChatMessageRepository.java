package com.spvue.SocketIO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByRoomOrderByTimestampAsc(String room);



    Page<ChatMessageEntity> findByStatus(String status, Pageable pageable);

    // 방별로 그룹화된 최신 메시지를 가져오는 쿼리
    @Query("SELECT c FROM ChatMessageEntity c WHERE c.timestamp = (SELECT MAX(c2.timestamp) FROM ChatMessageEntity c2 WHERE c2.room = c.room)")
    Page<ChatMessageEntity> findLatestMessagesByRoom(Pageable pageable);

    // 상태별로 그룹화된 최신 메시지를 가져오는 쿼리
    @Query("SELECT c FROM ChatMessageEntity c WHERE c.timestamp = (SELECT MAX(c2.timestamp) FROM ChatMessageEntity c2 WHERE c2.room = c.room) AND c.status = :status")
    Page<ChatMessageEntity> findLatestMessagesByRoomAndStatus(String status, Pageable pageable);
}

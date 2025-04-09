package com.spvue.SocketIO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String username;
    private String message;
    private String room;

}
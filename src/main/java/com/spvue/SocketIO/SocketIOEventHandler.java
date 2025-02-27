package com.spvue.SocketIO;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOEventHandler {
    private final SocketIOServer server;

    @Autowired
    public SocketIOEventHandler(SocketIOServer server) {
        this.server = server;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", ChatMessage.class, onChatReceived());
        server.addEventListener("join_room", String.class, onRoomJoined());
    }

    private ConnectListener onConnected() {
        return client -> {
            System.out.println("Client connected: " + client.getSessionId());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            System.out.println("Client disconnected: " + client.getSessionId());
        };
    }

    private DataListener<ChatMessage> onChatReceived() {
        return (client, data, ackRequest) -> {
            System.out.println("Message received: " + data.getMessage());
            // 메시지를 같은 방에 있는 모든 클라이언트에게 브로드캐스트
            server.getRoomOperations(data.getRoom()).sendEvent("receive_message", data);
        };
    }

    private DataListener<String> onRoomJoined() {
        return (client, room, ackRequest) -> {
            client.joinRoom(room);
            System.out.println("Client " + client.getSessionId() + " joined room " + room);
        };
    }
}
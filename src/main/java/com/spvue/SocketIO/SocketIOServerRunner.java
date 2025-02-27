package com.spvue.SocketIO;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerRunner implements CommandLineRunner {

    private final SocketIOServer server;

    @Autowired
    public SocketIOServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting Socket.IO server on port " + server.getConfiguration().getPort());
        server.start();
        System.out.println("Socket.IO server started successfully");
    }

    @PreDestroy
    public void stopSocketIOServer() {
        server.stop();
    }

}
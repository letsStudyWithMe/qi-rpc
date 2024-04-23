package com.qi.example.provider;

import com.qi.example.common.service.UserService;
import com.qi.qirpc.easy.registry.LocalRegistry;
import com.qi.qirpc.easy.server.VertHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        VertHttpServer vertHttpServer = new VertHttpServer();
        vertHttpServer.doStart(8080);
    }
}

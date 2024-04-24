package com.qi.example.provider;

import com.qi.example.common.service.UserService;
import com.qi.qirpc.core.RpcApplication;
import com.qi.qirpc.core.config.RpcConfig;
import com.qi.qirpc.core.registry.LocalRegistry;
import com.qi.qirpc.core.server.VertHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        VertHttpServer vertHttpServer = new VertHttpServer();
        vertHttpServer.doStart(rpcConfig.getServerPort());
    }
}

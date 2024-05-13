package com.qi.example.provider;

import com.qi.example.common.service.UserService;
import com.qi.qirpc.core.RpcApplication;
import com.qi.qirpc.core.config.RegistryConfig;
import com.qi.qirpc.core.config.RpcConfig;
import com.qi.qirpc.core.model.ServiceMetaInfo;
import com.qi.qirpc.core.registry.EtcdRegistry;
import com.qi.qirpc.core.registry.LocalRegistry;
import com.qi.qirpc.core.registry.Registry;
import com.qi.qirpc.core.registry.RegistryFactory;
import com.qi.qirpc.core.server.VertHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName,UserServiceImpl.class);

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VertHttpServer vertHttpServer = new VertHttpServer();
        vertHttpServer.doStart(rpcConfig.getServerPort());

//        测试JVM -Shutdown Hook
//        try {
//            Thread.sleep(10 * 1000L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.exit(0);
    }
}

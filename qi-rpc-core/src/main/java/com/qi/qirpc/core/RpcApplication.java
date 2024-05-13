package com.qi.qirpc.core;

import com.qi.qirpc.core.config.ApplicationConfig;
import com.qi.qirpc.core.config.RegistryConfig;
import com.qi.qirpc.core.config.RpcConfig;
import com.qi.qirpc.core.constant.RpcConstant;
import com.qi.qirpc.core.registry.Registry;
import com.qi.qirpc.core.registry.RegistryFactory;
import com.qi.qirpc.core.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc config init success,config = {}" + newRpcConfig.toString());

        //注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init success,config = {}" + registryConfig.toString());

        //创建并注册Shutdown Hook，JVM退出时执行
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destory));
    }

    public static void init(){
        RpcConfig newRpcConfig;
        try {
            //加载配置文件环境类型
            ApplicationConfig applicationConfig = ConfigUtils.loadConfig(ApplicationConfig.class, RpcConstant.DEFAULT_APPLICATION_PREFIX);
            if (applicationConfig != null && RpcConstant.DEFAULT_APPLICATION_TYPE_DEV.equals(applicationConfig.getApplicationType())){
                //加载dev配置文件rpc配置
                newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX,RpcConstant.DEFAULT_APPLICATION_TYPE_DEV);
            }else if (applicationConfig != null && RpcConstant.DEFAULT_APPLICATION_TYPE_PROD.equals(applicationConfig.getApplicationType())){
                //加载prod配置文件rpc配置
                newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX,RpcConstant.DEFAULT_APPLICATION_TYPE_PROD);
            }else if (applicationConfig != null && RpcConstant.DEFAULT_APPLICATION_TYPE_TEST.equals(applicationConfig.getApplicationType())){
                //加载test配置文件rpc配置
                newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX,RpcConstant.DEFAULT_APPLICATION_TYPE_TEST);
            }else {
                //加载配置文件rpc配置
                newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
            }
        } catch (Exception e) {
            //配置加载失败，使用默认值
            log.info("load config fail,use default config");
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}

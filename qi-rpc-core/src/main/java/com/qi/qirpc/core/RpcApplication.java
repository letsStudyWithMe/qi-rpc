package com.qi.qirpc.core;

import com.qi.qirpc.core.config.RpcConfig;
import com.qi.qirpc.core.constant.RpcConstant;
import com.qi.qirpc.core.utils.ConfigUtiles;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc config init success,config = {}" + newRpcConfig.toString());
    }

    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtiles.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX, RpcConstant.DEFAULT_CONFIG_TYPE_YML);
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
                    rpcConfig = new RpcConfig();
                }
            }
        }
        return rpcConfig;
    }
}

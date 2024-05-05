package com.qi.qirpc.core.registry;

import com.qi.qirpc.core.constant.RegistryKeys;
import com.qi.qirpc.core.spi.SpiLoader;

public class RegistryFactory {
    static{
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认的注册中心
     */
    private static final Registry DEFAULT_REGISTRY = SpiLoader.getInstance(Registry.class, RegistryKeys.ETCD);

    /**
     * 获取注册中心实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class, key);
    }

}

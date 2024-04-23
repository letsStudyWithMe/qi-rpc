package com.qi.qirpc.easy.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry {

    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
    }

    public static void remove(String serviceName){
        map.remove(serviceName);
    }
}

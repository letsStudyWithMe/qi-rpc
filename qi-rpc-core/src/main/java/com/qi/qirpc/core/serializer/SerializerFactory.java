package com.qi.qirpc.core.serializer;

import com.qi.qirpc.core.constant.SerializerKeys;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    /**
     * 序列化映射
     */
    private static final Map<String,Serializer> SERIALIZER_MAP = new HashMap<>(){{
        put(SerializerKeys.JDK,new JdkSerializer());
        put(SerializerKeys.JSON,new JsonSerializer());
        put(SerializerKeys.KRYO,new KryoSerializer());
        put(SerializerKeys.HESSIAN,new HessianSerializer());
    }};

    /**
     * 默认的序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = SERIALIZER_MAP.get(SerializerKeys.JDK);

    /**
     * 获取序列化器
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
        return SERIALIZER_MAP.get(key);
    }
}

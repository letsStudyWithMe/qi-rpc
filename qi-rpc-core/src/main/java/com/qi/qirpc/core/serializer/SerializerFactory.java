package com.qi.qirpc.core.serializer;

import com.qi.qirpc.core.constant.SerializerKeys;
import com.qi.qirpc.core.spi.SpiLoader;

public class SerializerFactory {

    static{
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认的序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = SpiLoader.getInstance(Serializer.class, SerializerKeys.JDK);

    /**
     * 获取序列化器
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class, key);
    }
}

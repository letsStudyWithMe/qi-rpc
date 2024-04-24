package com.qi.qirpc.core.utils;

import com.qi.qirpc.core.constant.RpcConstant;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

import java.nio.charset.StandardCharsets;

public class ConfigUtiles {
    /**
     * 加载配置对象
     *
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix,String type) {
        return loadConfig(tClass, prefix, "",type);
    }

    /**
     * 加载配置对象，支持区分环境
     *
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment,String type) {
        StringBuilder configFileBuilder = new StringBuilder(RpcConstant.DEFAULT_CONFIG_FILE);
        configFileBuilder = new StringBuilder(RpcConstant.DEFAULT_CONFIG_FILE);
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(type);
        //StandardCharsets.UTF_8 配置文件中文乱码
        Props props = new Props(configFileBuilder.toString(), StandardCharsets.UTF_8);
        //自动加载配置
        props.autoLoad(true);
        return props.toBean(tClass, prefix);
    }
}








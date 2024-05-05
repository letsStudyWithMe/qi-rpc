package com.qi.qirpc.core.config;

import lombok.Data;

@Data
public class ApplicationConfig {
    /**
     * 配置文件环境类型
     */
    private String applicationType = "dev";
}

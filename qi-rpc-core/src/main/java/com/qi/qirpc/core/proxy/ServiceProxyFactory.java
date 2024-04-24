package com.qi.qirpc.core.proxy;

import com.qi.qirpc.core.RpcApplication;
import com.qi.qirpc.core.config.RpcConfig;

import java.lang.reflect.Proxy;


public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {
        // 如果是mock模式，则返回mock代理
        if (RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
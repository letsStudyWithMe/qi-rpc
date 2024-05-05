package com.qi.qirpc.core.registry;

import cn.hutool.json.JSONUtil;
import com.qi.qirpc.core.config.RegistryConfig;
import com.qi.qirpc.core.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry{

    private Client client;

    private KV kvClient;
    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 初始化
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
    }

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建Lease 和 KV客户端
        Lease leaseClient = client.getLeaseClient();
        //创建租约 30s
        long leaseId = leaseClient.grant(300).get().getID();
        //设置要存储的键值对
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo),StandardCharsets.UTF_8);
        //将键值对和租约联系起来，设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).get();
    }

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String deleteKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(deleteKey,StandardCharsets.UTF_8);
        try {
            kvClient.delete(key).get();
        } catch (Exception e) {
            throw new RuntimeException(deleteKey+"，删除失败"+e);
        }

        // 根据key前缀匹配，批量删除
//        DeleteOption deleteOption = DeleteOption.newBuilder().withPrefix(key).build();
//        try {
//            kvClient.delete(key, deleteOption).get();
//        } catch (Exception e) {
//            throw new RuntimeException(deleteKey+"，删除失败"+e);
//        }
    }

    /**
     * 服务发现
     * @param serviceKey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + serviceKey +"/";
        try {
            //前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            //解析服务信息
            return keyValues.stream()
                    .map(keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    /**
     * 服务销毁
     */
    @Override
    public void destory() {
        System.out.println("当前节点下线");
        if (kvClient != null){
            kvClient.close();
        }
        if (client != null){
            client.close();
        }
    }
}

package edu.neu.rpc.registry;

import java.net.InetSocketAddress;

/**
 * create time: 2021/8/3 下午 8:40
 *
 * @author DownUpZ
 */
public interface ServiceRegistry {
    /**
     * register 方法将服务的名称和地址注册进服务注册中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * lookupService 方法则是根据服务名称从注册中心获取到一个服务提供者的地址
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}

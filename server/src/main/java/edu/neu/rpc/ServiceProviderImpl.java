package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create time: 2021/7/31 上午 9:52
 *
 * @author DownUpZ
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
    private static final Map<Class<?>, Class<?>> MAP = new ConcurrentHashMap<>(16);

    /**
     * @param serviceName      接口名 服务名
     * @param service 具体的执行类

     */
    @Override
    public void addServiceProvider(Class<?> serviceName, Class<?> service) {
        MAP.put(serviceName, service);
    }

    @Override
    public Class<?> getServiceProvider(Class<?> serviceName) {
        return MAP.get(serviceName);
    }
}

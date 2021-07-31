package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * create time: 2021/7/31 上午 9:52
 *
 * @author DownUpZ
 */
@Slf4j
class Registry {
    private static final Map<String, Class<?>> MAP =  new HashMap<>(16);


    public Class<?> getRegistry(String className) {
        return MAP.get(className);
    }

    public <T> void register(T service) {
        try {
            String serviceName = service.getClass().getCanonicalName();
            Class<?>[] interfaces = service.getClass().getInterfaces();
            for (Class<?> i : interfaces) {
                log.info("向接口 {} 注册服务 {}", i, serviceName);
                MAP.put(i.getCanonicalName(), Class.forName(serviceName));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

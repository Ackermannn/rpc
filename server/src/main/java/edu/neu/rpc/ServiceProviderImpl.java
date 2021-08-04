package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
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
     * @param serviceName 接口名 服务名
     * @param service     具体的执行类
     */
    @Override
    public void addServiceProvider(Class<?> serviceName, Class<?> service) {
        log.info("服务端容器添加了服务{}的具体执行{}", serviceName.getCanonicalName(), service.getCanonicalName());
        MAP.put(serviceName, service);
    }

    @Override
    public Class<?> getServiceProvider(Class<?> serviceName) {
        if (!MAP.containsKey(serviceName)) {
            log.info("服务端容器中没有服务 {}", serviceName.getCanonicalName());
            throw new RuntimeException("服务端容器中没有服务");
        }
        return MAP.get(serviceName);
    }

    @Override
    public RpcResponse<?> runService(RpcRequest msg) {
        try {
            Class<?> aClass = getServiceProvider(Class.forName(msg.getInterfaceName()));
            Object o = aClass.newInstance();
            Method method = aClass.getDeclaredMethod(msg.getMethodName(), msg.getParamTypes());
            Object result = method.invoke(o, msg.getParameters());
            RpcResponse<Object> rpcResponse = new RpcResponse<>();
            rpcResponse.setData(result);
            rpcResponse.setMessage("SUCCESS");
            log.info("服务：{} 成功执行方法 {}", msg.getInterfaceName(), msg.getMethodName());
            return rpcResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

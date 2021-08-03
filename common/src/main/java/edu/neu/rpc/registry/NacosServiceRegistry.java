package edu.neu.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import edu.neu.rpc.exceptions.RpcError;
import edu.neu.rpc.exceptions.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * create time: 2021/8/3 下午 8:43
 *
 * @author DownUpZ
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    private static String SERVER_ADDR;
    private static NamingService namingService;

    public NacosServiceRegistry() {
        SERVER_ADDR = "127.0.0.1:8848";
        log.info("链接默认的Nacos注册: " + SERVER_ADDR);
        createNamingService();
    }

    public NacosServiceRegistry(String serverAddress) {
        SERVER_ADDR = serverAddress;
        createNamingService();
    }

    private void createNamingService() {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            log.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);

            if (instances.size() == 0) {
                log.error("服务[{}]在 Nacos 注册中心中没有查询到", serviceName);
                throw new RuntimeException("服务未在注册中心查询到");
            }

            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}

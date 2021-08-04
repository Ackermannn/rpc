package edu.neu.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import edu.neu.rpc.ConfigureParse;
import edu.neu.rpc.exceptions.RpcError;
import edu.neu.rpc.exceptions.RpcException;
import edu.neu.rpc.loadbalancer.LoadBalancer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * create time: 2021/8/3 下午 8:43
 *
 * @author DownUpZ
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    private String host;
    private int port;
    private final NamingService namingService;
    private LoadBalancer loadBalancer;

    /**
     * 记录注册过的服务
     */
    private final Set<String> serviceName = new HashSet<>();

    public NacosServiceRegistry() {
        String configName = ConfigureParse.parseLoadBalancer();
        try {
            Class<?> clazz = Class.forName("edu.neu.rpc.loadbalancer." + configName);
            loadBalancer = (LoadBalancer) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // 注册中心的地址与端口
        String serverAdder = ConfigureParse.parseRegistryServerAdder();
        log.info("链接默认的Nacos注册: " + serverAdder);

        try {
            namingService = NamingFactory.createNamingService(serverAdder);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    /**
     * 清理注册中心
     */
    @Override
    public void clearRegistry() {
        log.info("开始创建钩子线程");
        class CloseThread extends Thread {
            @Override
            public void run() {
                log.info("开始注销服务");
                try {
                    if (namingService != null && serviceName.size() > 0) {
                        for (String iter : serviceName) {
                            namingService.deregisterInstance(iter, host, port);
                        }
                    }
                    log.info("注销服务 {} 成功", serviceName);
                } catch (NacosException e) {
                    log.error("注销服务 {} 失败", serviceName, e);
                }
            }
        }
        Runtime.getRuntime().addShutdownHook(new CloseThread());
    }


    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {

            host = inetSocketAddress.getHostName();
            port = inetSocketAddress.getPort();
            this.serviceName.add(serviceName);
            namingService.registerInstance(serviceName, host, port);
            log.info("注册中心注册了服务{}在{}:{}", serviceName, host, port);
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

            Instance instance = loadBalancer.select(instances);

            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }

}

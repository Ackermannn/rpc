package edu.neu.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import edu.neu.rpc.loadbalancer.LoadBalancer;
import edu.neu.rpc.loadbalancer.RandomLoadBalancer;
import edu.neu.rpc.exceptions.RpcError;
import edu.neu.rpc.exceptions.RpcException;
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

    private String SERVER_ADDR;
    private NamingService namingService;
    private String host;
    private int port;
    private LoadBalancer loadBalancer = new RandomLoadBalancer(); // 默认随机
    private final Set<String> serviceName = new HashSet<>(); // 记录注册过的服务

    public NacosServiceRegistry() {
        SERVER_ADDR = "127.0.0.1:8848";
        log.info("链接默认的Nacos注册: " + SERVER_ADDR);
        createNamingService();
    }

    public NacosServiceRegistry(String serverAddress) {
        SERVER_ADDR = serverAddress;
        createNamingService();
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
//                    e.printStackTrace();
                }
            }
        }
        Runtime.getRuntime().addShutdownHook(new CloseThread());
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

    public LoadBalancer getLoadBalancer() {
        log.info("采用 {} 负载均衡策略", loadBalancer.getClass().getCanonicalName());
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        log.info("采用 {} 负载均衡策略", loadBalancer.getClass().getCanonicalName());
        this.loadBalancer = loadBalancer;
    }
}

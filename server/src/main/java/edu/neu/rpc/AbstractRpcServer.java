package edu.neu.rpc;

import edu.neu.rpc.annontation.Service;
import edu.neu.rpc.registry.NacosServiceRegistry;
import edu.neu.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * create time: 2021/8/3 下午 8:57
 *
 * @author DownUpZ
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry = new NacosServiceRegistry();
    protected ServiceProviderImpl serviceProviderImpl = new ServiceProviderImpl();

    public AbstractRpcServer(String host, int port) {
        this.host = host;
        this.port = port;

        // 扫描包中的所有带有Service注解的类然后 publishService
        Set<Class<?>> classSet = ClassUtil.getClasses("edu.neu.rpc");
        for (Class<?> clazz : classSet) {

            boolean b = clazz.isAnnotationPresent(Service.class);
            if (b) {
                log.info("发现" + clazz.getName() + "被Service注解");
                for (Class<?> service : clazz.getInterfaces()) {
                    publishService(service, clazz);
                }
            }
        }

        // 创建钩子进程当服务器关闭时注销注册
        serviceRegistry.clearRegistry();

    }

    @Override
    public void publishService(Class<?> serviceName, Class<?> service) {
        serviceProviderImpl.addServiceProvider(serviceName, service);
        serviceRegistry.register(serviceName.getCanonicalName(), new InetSocketAddress(host, port));
    }
}

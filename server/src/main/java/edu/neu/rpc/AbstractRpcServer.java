package edu.neu.rpc;

import edu.neu.rpc.registry.NacosServiceRegistry;
import edu.neu.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

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
    }

    @Override
    public void publishService(Class<?> serviceName, Class<?> service) {
        serviceProviderImpl.addServiceProvider(serviceName, service);
        serviceRegistry.register(serviceName.getCanonicalName(), new InetSocketAddress(host, port));
    }
}

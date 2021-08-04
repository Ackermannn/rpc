package edu.neu.rpc;

/**
 * @author 201
 */
public interface ServiceProvider {
    /**
     *
     */
    void addServiceProvider(Class<?> serviceClass, Class<?> service);

    /**
     *
     */
    Class<?> getServiceProvider(Class<?> serviceName);

    /**
     *
     */
    RpcResponse<?> runService(RpcRequest msg);

}

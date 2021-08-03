package edu.neu.rpc;

/**
 *
 */
public interface RpcServer {
    void start();
    void publishService(Class<?> service,  Class<?> serviceClass);
}

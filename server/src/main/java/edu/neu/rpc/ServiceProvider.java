package edu.neu.rpc;

public interface ServiceProvider {
    void addServiceProvider(Class<?> serviceClass, Class<?> service);

    Class<?> getServiceProvider(Class<?> serviceName);

}

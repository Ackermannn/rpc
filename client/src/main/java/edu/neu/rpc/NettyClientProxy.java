package edu.neu.rpc;

import edu.neu.rpc.api.Caller;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * @author 201
 */
@Slf4j
public class NettyClientProxy implements InvocationHandler {
    private NettyClient client;

    public NettyClientProxy(NettyClient client) {
        this.client = client;
    }

    /**
     * Proxy类就是用来创建一个代理对象的类，它提供了很多方法，但是我们最常用的是newProxyInstance方法
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<Caller> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);

        RpcResponse<Object> response;
        response = client.send(rpcRequest);
        return response.getData();

    }


}

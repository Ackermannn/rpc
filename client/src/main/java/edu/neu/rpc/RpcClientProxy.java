package edu.neu.rpc;

import edu.neu.rpc.api.Caller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * create time: 2021/7/31 上午 8:53
 * 代理类
 *
 * @author DownUpZ
 */
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Proxy类就是用来创建一个代理对象的类，它提供了很多方法，但是我们最常用的是newProxyInstance方法
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<Caller> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try (Socket socket = new Socket(host, port)) {
            // 对象输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParamTypes(method.getParameterTypes());
            rpcRequest.setParameters(args);

            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();


            // 对象输入流
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();
            RpcResponse<Object> rpcResponse;
            if (object instanceof RpcResponse<?>) {
                rpcResponse = (RpcResponse<Object>) object;
            } else {
                throw new RuntimeException("类型强制转化失败");
            }

            return rpcResponse.getData();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


}

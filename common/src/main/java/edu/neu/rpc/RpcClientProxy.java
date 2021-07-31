package edu.neu.rpc;

import java.io.IOException;
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
//    private Socket socket;

    public RpcClientProxy(String host, int port) {
        this.host = host; // "127.0.0.1"
        this.port = port; // 10086
    }

    /**
     * Proxy类就是用来创建一个代理对象的类，它提供了很多方法，但是我们最常用的是newProxyInstance方法
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<Caller> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try(Socket socket = new Socket(host, port)){
            // 对象输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParamTypes(method.getParameterTypes());
            rpcRequest.setParameters(args);

            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
//            System.out.println("客户端已经发送请求");


            // 对象输入流
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse<Object> rpcResponse = (RpcResponse<Object>) objectInputStream.readObject();
//            System.out.println("rpcResponse Message: " + rpcResponse.getMessage());

            return rpcResponse.getData();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }


}

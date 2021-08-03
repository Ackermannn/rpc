package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * create time: 2021/7/31 上午 9:52
 *
 * @author DownUpZ
 */
@Slf4j
class ServerThread implements Runnable {
    Socket socket;
    private ServiceProviderImpl serviceProviderImpl;

    public ServerThread(Socket socket, ServiceProviderImpl serviceProviderImpl) {
        this.socket = socket;
        this.serviceProviderImpl = serviceProviderImpl;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            log.info("接收到: " + rpcRequest.toString()); // 打印输入信息


            Class<?> aClass = serviceProviderImpl.getServiceProvider(rpcRequest.getInterfaceName().getClass());
            Object o = aClass.newInstance();
            Method method = aClass.getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object o1 = method.invoke(o, rpcRequest.getParameters());
            log.info("服务：{} 成功执行方法 {}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            RpcResponse<Object> rpcResponse = new RpcResponse<>();
            rpcResponse.setData(o1);
            rpcResponse.setMessage("SUCCESS");

            // socket 通讯
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());//字节输出流
            objectOutputStream.writeObject(rpcResponse);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

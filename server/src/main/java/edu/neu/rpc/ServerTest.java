package edu.neu.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * create time: 2021/7/30 下午 8:55
 *
 * @author DownUpZ
 */
public class ServerTest {
    private static final Map<String, Class<?>> MAP;

    static {
        // 登记服务
        MAP = new HashMap<>(16);
        try {
            MAP.put("edu.neu.rpc.Caller", Class.forName("edu.neu.rpc.CallerImpl"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给接口名字返回 实现类类对象
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> registry(String className) throws ClassNotFoundException {
        return MAP.get(className);
    }

    public static void main(String[] args) {
        try {
            //1、创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            // 1024-65535的某个端口
            ServerSocket serverSocket = new ServerSocket(10086);
            //2、调用accept()方法开始监听，等待客户端的连接
            Socket socket;
            while (true) {
                try {


                    socket = serverSocket.accept();

                    // socket 通讯
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                    System.out.println(rpcRequest); // 打印输入信息


                    Class<?> aClass = registry(rpcRequest.getInterfaceName());
                    Object o = aClass.newInstance();
                    Method method = aClass.getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                    Object o1 = method.invoke(o, rpcRequest.getParameters());
                    System.out.println("客户端打印执行结果" + o1);

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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

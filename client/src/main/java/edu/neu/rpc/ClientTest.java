package edu.neu.rpc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * create time: 2021/7/30 下午 8:51
 * 客户端启动入口
 *
 * @author DownUpZ
 */
public class ClientTest {
    public static void main(String[] args) throws ClassNotFoundException {
        //客户端
        //1、创建客户端Socket，指定服务器地址和端口
        try {
            Socket socket = new Socket("127.0.0.1", 10086);
            //2、获取输出流，向服务器端发送信息
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setInterfaceName("edu.neu.rpc.Caller");
            rpcRequest.setMethodName("hello");
            rpcRequest.setParamTypes(new Class[]{String.class});
            rpcRequest.setParameters(new Object[]{"参数是什么"});
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());//字节输出流
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse<Object> rpcResponse = (RpcResponse<Object>) objectInputStream.readObject();
            String ret = (String) rpcResponse.getData();
            System.out.println(ret);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

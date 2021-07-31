package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * create time: 2021/7/30 下午 8:51
 * 客户端启动入口
 *
 * @author DownUpZ
 */
@Slf4j
public class ClientTest {
    public static void main(String[] args) {
        //客户端
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 10086);
        Caller caller = proxy.getProxy(Caller.class);

        log.info("客户端开始执行 hello 函数");
        String ret = caller.hello("123");
        log.info("客户端收到: {}", ret);

        log.info("客户端开始执行 getRpcResult 函数");
        RpcResult ret2 = caller.getRpcResult("Tom");
        log.info("客户端收到: {}", ret2);

    }
}

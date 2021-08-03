package edu.neu.rpc.test;

import edu.neu.rpc.*;
import edu.neu.rpc.api.Caller;
import lombok.extern.slf4j.Slf4j;

/**
 * create time: 2021/7/31 下午 3:46
 *
 * @author DownUpZ
 */
@Slf4j
public class NettyClientTest {
    static int i;

    public static void main(String[] args) {
        //客户端
        NettyClientProxy proxy = new NettyClientProxy(new NettyClient());
        Caller caller = proxy.getProxy(Caller.class);
        Integer ret;
        for (i = 0; i < 1; i++) {
            new Thread(() -> {
                log.info("Client Get: {} + {} = {}", i, i, caller.add(i, i));
            }).start();
        }


    }
}

package edu.neu.rpc.test;

import edu.neu.rpc.NettyClient;
import edu.neu.rpc.NettyClientProxy;
import edu.neu.rpc.api.Caller;
import edu.neu.rpc.api.CallerTwo;
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
        CallerTwo callerTwo = proxy.getProxy(CallerTwo.class);

        log.info("Client Get: {} + {} = {}", i, i, caller.add(i, i));
        log.info(callerTwo.hello("bala bala"));


    }
}

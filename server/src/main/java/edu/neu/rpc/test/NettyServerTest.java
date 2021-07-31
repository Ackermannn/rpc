package edu.neu.rpc.test;

import edu.neu.rpc.*;

/**
 * create time: 2021/7/31 下午 3:47
 *
 * @author DownUpZ
 */
public class NettyServerTest {
    public static void main(String[] args) {
        Caller caller = new CallerImpl();
        Registry registry = new Registry();
        registry.register(caller);

        RpcServer rs = new RpcServerNettyImpl(registry);
        rs.start(10086);
    }
}

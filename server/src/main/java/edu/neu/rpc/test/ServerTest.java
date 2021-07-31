package edu.neu.rpc.test;

import edu.neu.rpc.*;

/**
 * create time: 2021/7/30 下午 8:55
 *
 * @author DownUpZ
 */
public class ServerTest {
    public static void main(String[] args) {
        Caller caller = new CallerImpl();
        Registry registry = new Registry();
        registry.register(caller);

        RpcServer rs = new RpcServerSocketImpl(registry);
        rs.start(10086);
    }
}

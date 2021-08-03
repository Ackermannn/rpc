package edu.neu.rpc.test;

import edu.neu.rpc.*;
import edu.neu.rpc.api.Caller;
import edu.neu.rpc.api.CallerImpl;

/**
 * create time: 2021/7/31 下午 3:47
 *
 * @author DownUpZ
 */
public class NettyServerTest {
    static final int DEFAULT_PORT = 10086;

    public static void main(String[] args) {

        RpcServer rs = new NettyServer("127.0.01", DEFAULT_PORT);

        rs.publishService(Caller.class, CallerImpl.class);
        rs.start();

    }
}

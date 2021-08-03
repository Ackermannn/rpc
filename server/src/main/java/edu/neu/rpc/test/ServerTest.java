package edu.neu.rpc.test;

import edu.neu.rpc.*;
import edu.neu.rpc.api.Caller;
import edu.neu.rpc.api.CallerImpl;

/**
 * create time: 2021/7/30 下午 8:55
 *
 * @author DownUpZ
 */
public class ServerTest {
    public static void main(String[] args) {

        RpcServer rs = new SocketServer("127.0.0.1", 10086);
        rs.publishService(Caller.class, CallerImpl.class);
        rs.start();
    }
}

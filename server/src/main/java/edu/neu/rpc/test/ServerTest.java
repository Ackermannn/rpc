package edu.neu.rpc.test;

import edu.neu.rpc.RpcServer;
import edu.neu.rpc.SocketServer;

/**
 * create time: 2021/7/30 下午 8:55
 *
 * @author DownUpZ
 */
public class ServerTest {
    public static void main(String[] args) {
        RpcServer rs = new SocketServer("127.0.0.1", 10086);
        rs.start();
    }
}

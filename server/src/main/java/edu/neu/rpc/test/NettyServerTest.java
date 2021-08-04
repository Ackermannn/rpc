package edu.neu.rpc.test;

import edu.neu.rpc.NettyServer;
import edu.neu.rpc.RpcServer;
import lombok.extern.slf4j.Slf4j;

/**
 * create time: 2021/7/31 下午 3:47
 *
 * @author DownUpZ
 */
@Slf4j
public class NettyServerTest {

    public static void main(String[] args) {
        RpcServer rs = new NettyServer("127.0.01", 10086);
        rs.start();

    }
}

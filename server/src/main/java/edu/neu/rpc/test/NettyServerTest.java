package edu.neu.rpc.test;

import edu.neu.rpc.NettyServer;
import edu.neu.rpc.RpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * create time: 2021/7/31 下午 3:47
 *
 * @author DownUpZ
 */
@Slf4j
public class NettyServerTest {

    public static void main(String[] args) {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        log.info("本地ip地址：{}", host);
        int port = 8765;

        RpcServer rs = new NettyServer(host, port);
        rs.start();

    }
}

package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;

/**
 * create time: 2021/7/31 上午 9:52
 *
 * @author DownUpZ
 */
@Slf4j
class ServerThread implements Runnable {
    Socket socket;
    private ServiceProviderImpl serviceProviderImpl;

    public ServerThread(Socket socket, ServiceProviderImpl serviceProviderImpl) {
        this.socket = socket;
        this.serviceProviderImpl = serviceProviderImpl;
    }

    @Override
    public void run() {
    }
}

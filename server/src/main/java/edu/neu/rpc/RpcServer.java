package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * create time: 2021/7/31 上午 9:35
 *
 * @author DownUpZ
 */
@Slf4j
public class RpcServer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private Registry registry;

    public RpcServer(Registry registry) {
        this.registry = registry;
    }

    public void start(int port) {
        try {
            log.info("服务启动....");
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket;
            BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
            ThreadFactory threadFactory = Executors.defaultThreadFactory();
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: " + socket.getInetAddress() + ":" + socket.getPort());
                threadPool.execute(new ServerThread(socket, registry));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


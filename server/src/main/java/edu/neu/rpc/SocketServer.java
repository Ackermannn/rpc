package edu.neu.rpc;

import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * create time: 2021/7/31 上午 9:35
 *
 * @author DownUpZ
 */
@Slf4j
public class SocketServer extends AbstractRpcServer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    public SocketServer(String host, int port) {
        super(host, port);
    }


    @Override
    public void start() {


        try {
            log.info("服务启动....");
            ServerSocket serverSocket = new ServerSocket(this.port);
            Socket socket;
            BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
            ThreadFactory threadFactory = Executors.defaultThreadFactory();
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: " + socket.getInetAddress() + ":" + socket.getPort());
                Socket finalSocket = socket;
                class ServerThread implements Runnable {
                    @Override
                    public void run() {
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(finalSocket.getInputStream());
                            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                            log.info("收到发来的rpcRequest" + rpcRequest);

                            RpcResponse<?> rpcResponse = serviceProviderImpl.runService(rpcRequest);

                            // socket 通讯
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(finalSocket.getOutputStream());//字节输出流
                            objectOutputStream.writeObject(rpcResponse);
                            objectOutputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                threadPool.execute(new ServerThread());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


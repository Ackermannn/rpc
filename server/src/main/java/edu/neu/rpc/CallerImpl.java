package edu.neu.rpc;

/**
 * create time: 2021/7/30 下午 9:29
 *
 * @author DownUpZ
 */
public class CallerImpl implements Caller{
    CallerImpl(){

    }
    @Override
    public String hello(String param) {
        return "我是服务器上的CallerImpl类下的hello方法, 方法执行的参数是" + param;
    }
}

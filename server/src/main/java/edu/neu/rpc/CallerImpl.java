package edu.neu.rpc;

/**
 * create time: 2021/7/30 下午 9:29
 *
 * @author DownUpZ
 */
public class CallerImpl implements Caller{
    public CallerImpl(){

    }
    @Override
    public String hello(String param) {
        return "我是服务器上CallerImpl类下的hello方法, 方法执行的参数是" + param;
    }

    @Override
    public RpcResult getRpcResult(String param) {
        RpcResult rpcResult = new RpcResult();
        rpcResult.setName(param);
        rpcResult.setAge(12);
        return rpcResult;
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }
}

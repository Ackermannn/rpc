package edu.neu.rpc.api;


import edu.neu.rpc.RpcResult;

/**
 * @author 201
 */
public interface Caller {
    /**
     * hello
     *
     * @param param input param
     * @return return result
     */
    String hello(String param);

    /**
     *
     */
    RpcResult getRpcResult(String param);

    /**
     *
     */
    Integer add(Integer a, Integer b);

}

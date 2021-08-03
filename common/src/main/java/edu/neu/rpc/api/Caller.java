package edu.neu.rpc.api;


import edu.neu.rpc.RpcResult;

public interface Caller {
    String hello(String param);

    RpcResult getRpcResult(String param);

    Integer add(Integer a, Integer b);

}

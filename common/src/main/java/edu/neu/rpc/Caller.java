package edu.neu.rpc;


public interface Caller {
    String hello(String param);
    RpcResult getRpcResult(String param);

}

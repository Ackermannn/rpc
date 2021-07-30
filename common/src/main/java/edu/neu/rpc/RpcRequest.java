package edu.neu.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * create time: 2021/7/30 下午 9:01
 *
 * @author DownUpZ
 */
@Data
public class RpcRequest implements Serializable {

    /**
     * 待调用接口名称
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 调用方法的参数
     */
    private Object[] parameters;

    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;

}

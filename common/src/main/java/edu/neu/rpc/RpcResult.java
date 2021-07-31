package edu.neu.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * create time: 2021/7/31 上午 9:29
 *
 * @author DownUpZ
 */
@Data
public class RpcResult implements Serializable {
    private String name;
    private int age;
}

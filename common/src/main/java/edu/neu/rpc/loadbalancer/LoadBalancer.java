package edu.neu.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author 201
 */
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}

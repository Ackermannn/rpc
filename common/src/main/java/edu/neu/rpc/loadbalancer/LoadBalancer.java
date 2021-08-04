package edu.neu.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡
 *
 * @author 201
 */
public interface LoadBalancer {
    /**
     * 负载均衡函数
     *
     * @param instances 可选负载
     * @return 选择的负载
     */
    Instance select(List<Instance> instances);
}

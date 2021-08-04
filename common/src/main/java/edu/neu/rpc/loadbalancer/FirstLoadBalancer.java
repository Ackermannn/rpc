package edu.neu.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * create time: 2021/8/4 上午 11:53
 *
 * @author DownUpZ
 */
public class FirstLoadBalancer implements LoadBalancer{
    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(0);
    }
}

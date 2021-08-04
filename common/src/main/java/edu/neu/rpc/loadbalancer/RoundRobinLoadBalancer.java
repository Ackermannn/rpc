package edu.neu.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * create time: 2021/8/4 上午 11:46
 * 转轮算法大家也应该了解，按照顺序依次选择第一个、第二个、第三个……这里就需要一个变量来表示当前选到了第几个：
 * @author DownUpZ
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private int index = 0;

    @Override
    public Instance select(List<Instance> instances) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }
}

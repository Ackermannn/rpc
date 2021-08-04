package edu.neu.rpc.api;

import edu.neu.rpc.annontation.Service;

/**
 * create time: 2021/8/4 下午 1:34
 *
 * @author DownUpZ
 */
@Service
public class CallerTwoImpl implements CallerTwo {
    @Override
    public String hello(String msg) {
        return "我是CallerTwoImpl的hello我收到了" + msg;
    }
}

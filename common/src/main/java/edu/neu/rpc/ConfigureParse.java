package edu.neu.rpc;

import java.util.ResourceBundle;

/**
 * create time: 2021/8/4 下午 8:12
 *
 * @author DownUpZ
 */
public class ConfigureParse {
    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("config");


    public static String parseRegistryServerAdder() {
        return RESOURCE.getString("RegistryServerAdder");
    }

    public static String parseLoadBalancer() {
        return RESOURCE.getString("loadBalancer");
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        System.out.println(parseRegistryServerAdder());
    }
}

package edu.neu.rpc.serializer;


/**
 * 序列化器接口（CommonSerializer）
 * @author 201
 */
public interface CommonSerializer {

    /**
     * @param obj obj
     * @return 序列化字节数组
     */
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}

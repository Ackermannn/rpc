package edu.neu.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * create time: 2021/7/31 下午 1:20
 *
 * @author DownUpZ
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic = in.readInt();
        if(magic != MAGIC_NUMBER) {
            log.error("不识别的协议包: {}", magic);
            throw new Exception();
        }
        int packageCode = in.readInt();
        Class<?> packageClass;
        if(packageCode == 0) {
            packageClass = RpcRequest.class;
        } else if(packageCode == 1) {
            packageClass = RpcResponse.class;
        } else {
            log.error("不识别的数据包: {}", packageCode);
            throw new Exception();
        }
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null) {
            log.error("不识别的反序列化器: {}", serializerCode);
            throw new Exception();
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }
}

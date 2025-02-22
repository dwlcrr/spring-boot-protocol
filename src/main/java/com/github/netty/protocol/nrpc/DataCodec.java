package com.github.netty.protocol.nrpc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Data encoder decoder. (Serialization or Deserialization)
 *
 * @author wangzihao
 */
public interface DataCodec {
    Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    /**
     * Request data - encoding
     *
     * @param data      data
     * @param rpcMethod rpcMethod
     * @return ByteBuf
     */
    byte[] encodeRequestData(Object[] data, RpcMethod<RpcClient> rpcMethod);

    /**
     * Response data - decoding
     *
     * @param data      data
     * @param rpcMethod rpcMethod
     * @return Object
     */
    Object decodeResponseData(byte[] data, RpcMethod<RpcClient> rpcMethod);

    /**
     * Response data - encoding
     *
     * @param data      data
     * @param rpcMethod rpcMethod
     * @return byte[]
     */
    byte[] encodeResponseData(Object data, RpcMethod<RpcServerInstance> rpcMethod);

    /**
     * Request data - decoding
     *
     * @param data      data
     * @param rpcMethod rpcMethod
     * @return Object[]
     */
    Object[] decodeRequestData(byte[] data, RpcMethod<RpcServerInstance> rpcMethod);

    /**
     * The client parses
     *
     * @return EncodeRequestConsumer
     */
    List<Consumer<Map<String, Object>>> getEncodeRequestConsumerList();

    /**
     * The server parses
     *
     * @return DecodeRequestConsumer
     */
    List<Consumer<Map<String, Object>>> getDecodeRequestConsumerList();

    /**
     * data encode enum  (note: 0=binary, 1=json)
     */
    enum Encode {
        /**
         * binary data encode
         */
        BINARY((byte) 0),
        /**
         * json data encode
         */
        JSON((byte) 1);

        private int code;

        Encode(byte code) {
            this.code = code;
        }

        public static Encode indexOf(int code) {
            for (Encode encode : values()) {
                if (encode.code == code) {
                    return encode;
                }
            }
            throw new IllegalArgumentException("value=" + code);
        }

        public int getCode() {
            return code;
        }
    }
}

# Netty 核心模块 PART-two

## Unpooled类
Netty提供的一个专门用来操作缓冲区（Netty的数据容器）的工具类。
- ByteBuf
  - 类似NIO 中的 ByteBuffer，但有区别。
- Unpooled.copiedBuffer("hello,China!武汉加油！", CharsetUtil.UTF_8);



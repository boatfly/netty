# Netty 核心模块 PART-two

## Unpooled类
Netty提供的一个专门用来操作缓冲区（Netty的数据容器）的工具类。
- ByteBuf
  - 类似NIO 中的 ByteBuffer，但有区别。
- Unpooled.copiedBuffer("hello,China!武汉加油！", CharsetUtil.UTF_8);

```java
//创建一个对象，包含一个数组arr，是一个byte[10]
ByteBuf buf = Unpooled.buffer(10);

for (int i = 0; i < 10; i++) {
    buf.writeByte(i);
}

//相对于NIO ByteBuffer，不需要用flip进行反转
//底层维护了 readindex writeindex
// writeindex == limit
for (int i = 0; i < 10; i++) {
    System.out.println(buf.getByte(i));//用index方式get，不会引起readindex变化
}

for (int i = 0; i < 10; i++) {
    System.out.println(buf.readByte());//会造成readindex的变化
}

//创建一个对象，包含一个数组arr，是一个byte[10]
ByteBuf buf = Unpooled.copiedBuffer("武汉加油！", CharsetUtil.UTF_8);
```


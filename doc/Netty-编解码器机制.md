# Netty 编解码器机制
数据在网络传输的都是二进制字节码数据，在发送数据的时候就需要编码，接收数据时就需要解码。
- codec(编解码器)的组成：
  - decoder 解码器
    - 将字节码数据转换为业务数据
  - encoder 编码器
    - 将业务数据转换为字节码数据
    
## Netty本身提供的解码器
- 底层使用java序列化
- 无法跨语言
- 序列化的体积过大，是二进制编码的5倍多
- 序列化性能太低
- 由此------引出-------》更好的序列化方案：Google的Protobuf

## Protobuf
以message的方式管理数据，跨平台、跨语言，适合做数据存储或者RPC数据交换格式。

`http+json` --转变--> `tcp+protobuf`

### Protobuf install in Mac
```
1. https://github.com/protocolbuffers/protobuf/releases
2. 下载带all的压缩包，譬如protobuf-all-3.6.1.zip，解压
3. cd 解压目录
4. sudo ./configure --prefix=/usr/local/protobuf 
5. sudo make
6. sudo make install
7. 添加环境变量：vi .bash_profile
8. export PROTOBUF=/usr/local/protobuf
   export PATH=$PROTOBUF/bin:$PATH
9  source .bash_profile
10.验证: protoc --version
```
### 使用
```
1.切换到.proto文件目录
2.protoc -I=./ --java_out=./ ./Student.proto
  -I 等价于 -proto_path：指定 .proto 文件所在的路径
  --java_out：编译成 java 文件时，标明输出目标路径
  ./Student.proto：指定需要编译的 .proto 文件
```
## 其他的解码器

### ReplayingDecoder

### LineBasedFrameDecoder
- 它使用行尾控制字符(\n或者\r\n)作为分隔符来解析数据

### DelimiterBasedFrameDecoder
- 它使用自定义的特殊字符作为消息的分割符

### HttpObjectDecoder
- 一个HTTP数据的解码器

### LengthFieldBasedFrameDecoder
- 通过制定长度来标识整包信息，这样就可以自动的处理粘包和半包信息。




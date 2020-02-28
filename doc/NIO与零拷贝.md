# NIO与零拷贝

`所谓零拷贝，不是没有copy，而且避免了CPU copy。`
`更少的数据复制，更少的上下文切换`

## 零拷贝
- 零拷贝是网络编程的关键，很多性能优化都离不开
  - 传统io，经历了4次copy，3次切换。
  - 如文件的传输，可以跳过（物理内存和jvm）（内核态和用户态）的copy过程。
  - DMA(Direct Memory Access) 直接内存拷贝。
- 在Java程序中，常用的零拷贝有mmap(内存映射)和sendFile

### mmap 优化
 - mmapt通过内存映射，将文件映射都到内核缓冲区，同时，用户空间可以共享内核空间的数据，这样，就可以在网络传输时，减少内核空间与用户空间的拷贝次数。
 - 经历3次copy，3次切换。（减少1次copy）

### sendFile 优化
 - Linux2.1版本提供了sendFile函数，
   - 基本原理：
     - 数据根本不用经过用户态，直接从内存缓冲区到SocketBuffer，同时，由于与用户态完全无关，就减少了一次切换。
   - 经历3次copy，2次切换。（减少1次切换）
 - Linux2.4版本中，做了一些修改，避免了从内核缓冲区copy到SocketBuffer的操作，直接copy到了协议栈，从而再减少一次copy。
   - 其实还是有1次cpu拷贝，kernel buffer -> socket buffer,但是copy的信息很少，譬如：length，offset，消耗低，可以忽略。
   - 经历2次copy，2次切换。
   - 常说的"零拷贝""
  
### mmap 和 sendFile 的区别
 - mmap适合少数据量读写，sendFile适合大文件传输。
 - mmap：3-3；sendFile 2-2
 - sendFile可以利用DMA方式，减少CPU拷贝。
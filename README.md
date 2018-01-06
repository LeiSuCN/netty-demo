Practice for Netty.

## 核心组件

* Channel. -> Socket .屏蔽了底层实现，提供了统一的编程模型。
* EventLoop.
* ChannelFuture.
* ChannelHandler.业务逻辑与网络处理代码的分离。
* ChannelPipeline.
* ChannelHandlerContext

```
Channel --+  
...       |
Channel --+-> EventLoop(Single Thread) --+  
                                         |
Channel --+                              |--> EventLoopGroup
          |                              |
Channel --+-> EventLoop(Single Thread) --+ 
...       |
Channel --+
```

Channel 1-->1 ChannelPipeline 1-->n ChannelHandler 1-->1 ChannelHandlerContext 


                                                   I/O Request
                                              via {@link Channel} or
                                          {@link ChannelHandlerContext}
                                                        |
    +---------------------------------------------------+---------------+
    |                           ChannelPipeline         |               |
    |                                                  \|/              |
    |    +---------------------+            +-----------+----------+    |
    |    | Inbound Handler  N  |            | Outbound Handler  1  |    |
    |    +----------+----------+            +-----------+----------+    |
    |              /|\                                  |               |
    |               |                                  \|/              |
    |    +----------+----------+            +-----------+----------+    |
    |    | Inbound Handler N-1 |            | Outbound Handler  2  |    |
    |    +----------+----------+            +-----------+----------+    |
    |              /|\                                  .               |
    |               .                                   .               |
    | ChannelHandlerContext.fireIN_EVT() ChannelHandlerContext.OUT_EVT()|
    |        [ method call]                       [method call]         |
    |               .                                   .               |
    |               .                                  \|/              |
    |    +----------+----------+            +-----------+----------+    |
    |    | Inbound Handler  2  |            | Outbound Handler M-1 |    |
    |    +----------+----------+            +-----------+----------+    |
    |              /|\                                  |               |
    |               |                                  \|/              |
    |    +----------+----------+            +-----------+----------+    |
    |    | Inbound Handler  1  |            | Outbound Handler  M  |    |
    |    +----------+----------+            +-----------+----------+    |
    |              /|\                                  |               |
    +---------------+-----------------------------------+---------------+
                    |                                  \|/
    +---------------+-----------------------------------+---------------+
    |               |                                   |               |
    |       [ Socket.read() ]                    [ Socket.write() ]     |
    |                                                                   |
    |  Netty Internal I/O Threads (Transport Implementation)            |
    +-------------------------------------------------------------------+
	
	
## NIO

### Channel(管道)和Buffer(缓冲器)
- **唯一** **直接** 与Channel交互的Buffer： ByteBuffer
### Selector
- Java NIO's selectors allow a single thread to monitor multiple channels of input.

### Zero-copy
- Performance is enhanced by allowing the CPU to move on to other tasks while data copies proceed in parallel in another part of the machine. Also, zero-copy operations reduce the number of time-consuming mode switches between user space and kernel space. 
- 目前只有在使用NIO和Epoll传输时才可以使用。input streams can support zero-copy through the java.nio.channels.FileChannel's transferTo() method if the underlying operating system also supports zero copy.
- [wikipedia](https://en.wikipedia.org/wiki/Zero-copy)

### Select, Poll, Epoll
- [select / poll / epoll: practical difference for system architects](https://www.ulduzsoft.com/2014/01/select-poll-epoll-practical-difference-for-system-architects/)

### channel vs stream
- Channel: A channel represents an open connection to an entity such as a hardware device, a file, a network socket, or a program component that is capable of performing one or more distinct I/O operations, for example reading or writing.
- Buffer: A buffer is a linear, finite sequence of elements of a specific primitive type.
- OutputStream: This abstract class is the superclass of all classes representing an output stream of bytes. An output stream accepts output bytes and sends them to some sink.
- InputStream: This abstract class is the superclass of all classes representing an input stream of bytes.
- stream 将连接和操作，数据类型全部耦合在了一起；而nio中将连接，操作，数据通过channel，（WritableByteChannel／ReadableByteChannel），Buffer解耦，更加符合SRP的理念。


## Pipeline，ChannelHandlerContext
- Pipeline中包含ChannelHandlerContext的双向链表，每个ChannelHandlerContext封装了**一个**Handler
- Outbound是package的发送者：即write、read等请求包的发送者，Inbound是package的接受者<br>
Outbound并不是write，同理Inbound也不是read

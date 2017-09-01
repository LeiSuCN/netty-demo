Practice for Netty.

## 核心组件

* Channel. -> Socket
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

Channel 1->1 ChannelPipeline 1->n ChannelHandler。 


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
- [elect / poll / epoll: practical difference for system architects](https://www.ulduzsoft.com/2014/01/select-poll-epoll-practical-difference-for-system-architects/)

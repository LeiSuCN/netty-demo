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

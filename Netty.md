# NIO 基本概念
## 阻塞（Block）与非阻塞（Non-Block）
阻塞和非阻塞是进程在访问数据的时候，数据是否准备就绪的一种处理方式，当数据没有准备的时候。  
**阻塞**: 往往需要等待缓冲区中的数据准备好过后才处理其他的事情，否则一直等待在那里。  
**非阻塞**: 当我们的进程访问我们的数据缓冲区的时候，如果数据没有准备好则直接返回，不会等待。如果数据已经准备好，也直接返回。  
**阻塞 IO**:  
![](https://mmbiz.qpic.cn/mmbiz_jpg/uChmeeX1Fpx7bubp6WLdlCN7ERvRXSp9cXXb6QPq5bWrnkZfPk0auvBt4QqhEuO9XWfdsHYicLr2ibB1KA8ofHqA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)  
**非阻塞 IO**:  
![](https://mmbiz.qpic.cn/mmbiz_jpg/uChmeeX1Fpx7bubp6WLdlCN7ERvRXSp9AmibBhTwpj7FE8EUvfwwGrMGn0icXpCxg6w56IibhUGWBTuwulCAzWDiaw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
## 同步（Synchronous）与异步（Asynchronous）
同步和异步都是基于应用程序和操作系统处理 IO 事件所采用的方式。比如  
**同步**: 是应用程序要直接参与 IO 读写的操作。  
**异步**: 所有的 IO 读写交给操作系统去处理，应用程序只需要等待通知。  
同步方式在处理 IO 事件的时候，必须阻塞在某个方法上面等待我们的 IO 事件完成（阻塞 IO 事件或者通过轮询 IO事件的方式），对于异步来说，所有的 IO 读写都交给了操作系统。这个时候，我们可以去做其他的事情，并不需要去完成真正的 IO 操作，当操作完成 IO 后，会给我们的应用程序一个通知。  
所以异步相比较于同步带来的直接好处就是在我们处理IO数据的时候，异步的方式我们可以把这部分等待所消耗的资源用于处理其他事务，提升我们服务自身的性能。  
**同步 IO**:  
![](https://mmbiz.qpic.cn/mmbiz_jpg/uChmeeX1Fpx7bubp6WLdlCN7ERvRXSp9v7ygg5uCUTeXBuCgL6EvLT5L1dmdF7r8JWiaJC4qiaV3ickakuLSoEclw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)  
**异步 IO**:  
![](https://mmbiz.qpic.cn/mmbiz_jpg/uChmeeX1Fpx7bubp6WLdlCN7ERvRXSp9qiaTibrEZcv4eu6FlP3icY8ShurfZxsBqo4giagkmukvze0To2ib8r7juJw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
## Java BIO与NIO对比
### BIO（传统IO）：
BIO是一个同步并阻塞的IO模式，传统的  java.io 包，它基于流模型实现，提供了我们最熟知的一些 IO 功能，比如File抽象、输入输出流等。交互方式是同步、阻塞的方式，也就是说，在读取输入流或者写入输出流时，在读、写动作完成之前，线程会一直阻塞在那里，它们之间的调用是可靠的线性顺序。
### NIO（Non-blocking/New I/O）
NIO 是一种同步非阻塞的 I/O 模型，于 Java 1.4 中引入，对应 java.nio 包，提供了 Channel , Selector，Buffer 等抽象。NIO 中的 N 可以理解为 Non-blocking，不单纯是 New。它支持面向缓冲的，基于通道的 I/O 操作方法。NIO 提供了与传统 BIO 模型中的 Socket 和 ServerSocket 相对应的 SocketChannel 和 ServerSocketChannel 两种不同的套接字通道实现,两种通道都支持阻塞和非阻塞两种模式。对于高负载、高并发的（网络）应用，应使用 NIO 的非阻塞模式来开发
#### NIO 的 Server 通信的简单模型：
![](https://mmbiz.qpic.cn/mmbiz_jpg/uChmeeX1Fpx7bubp6WLdlCN7ERvRXSp97iaXsd6waAcg6ufjpQvlXrYZFxicXzoJz5Dprv11XcMyjcHtp4ydkQ1w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
#### BIO 的 Server 通信的简单模型：
![](https://mmbiz.qpic.cn/mmbiz_jpg/uChmeeX1Fpx7bubp6WLdlCN7ERvRXSp9V33Dm7aCHUHdDiaoQiaQ2iaicCX7SrHsOU5zzEz16EcwXJqwL76cHqzllA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
**NIO的特点：**:  
1. 一个线程可以处理多个通道，减少线程创建数量。
2. 读写非阻塞，节约资源：没有可读／可写数据时，不会发生阻塞导致线程资源的浪费
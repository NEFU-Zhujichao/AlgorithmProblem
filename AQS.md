# 深入浅出AbstractQueuedSynchronizer
![](https://mmbiz.qpic.cn/mmbiz_jpg/uChmeeX1Fpx7JSVwOERcCuTUA4ZfuvczmND0icvG6FhriaLYKfWiaQ2jyzBO0r1f953zPKcT4xZEialc3rtzNaHwicQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 
> 下面是排他锁获得请求许可的代码：
```java
public final void acquire(int arg) {
    //尝试获得许可， arg为许可的个数。对于重入锁来说，每次请求1个。
    if (!tryAcquire(arg) &&
    // 如果tryAcquire 失败，则先使用addWaiter()将当前线程加入同步等待队列
    // 然后继续尝试获得锁
    acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
    selfInterrupt();
}
```

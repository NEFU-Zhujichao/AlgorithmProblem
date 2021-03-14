# 多线程高频面试考点
### 怎么保证线程同步
- [很详细](https://blog.csdn.net/yuqing2015/article/details/82788041?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.baidujs&dist_request_id=1328642.38542.16157091267591879&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.baidujs)
### Volatile关键字的作用和实现
- 保证可见性
- 不保证原子性
- 避免指令重排(内存屏障) 再Volatile写的前后加锁避免指令重排
[讲解的很详细](https://blog.csdn.net/zzti_erlie/article/details/86355477)
### Volatile怎么保证原子性
- 加Lock锁，Synchronized锁。
- 使用原子类，解决原子性问题(AtomicInteger)
### 聊聊线程池你知道多少
- **为什么用线程池**
    - 降低资源消耗。提高线程利用率，降低创建和销毁线程的消耗。
    - 提高响应速度。任务来了，直接有线程可用可执行，而不是先创建线程再执行。
    - 提高线程的可管理性。线程是稀缺资源，使用线程池可以统一分配调优监控。
- **创建线程的4大方法**
    - Executors.newSingleThreadExecutor() 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
    - Executors.newFixedThreadPool()      创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
    - Executors.newCachedThreadPool()     创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
    - Executors.newScheduledThreadPool()  创建一个定长线程池，支持定时及周期性任务执行。
    - newSingleThreadExecutor() newFixedThreadPool() 默认阻塞队列LinkedBlockingQueue(capacity: Integer.MAX_VALUE(21_4748_3647)) 可能会堆积大量请求，导致OOM。
    - newCachedThreadPool() newScheduledThreadPool() 默认允许创建的最大线程数为Integer.MAX_VALUE(21_4748_3647)，可能会创建大量的线程，导致OOM。
    - OOM：Out Of Memory 内存用完了。
- **线程池的7大参数**
    - int corePoolSize：核心线程数
    - int maximumPoolSize：最大线程数
    - long keepAliveTime、TimeUnit unit：如果创建了核心线程数之外的线程，当超过指定时间后线程将会被回收。  超时单位。
    - BlockingQueue<Runnable> workQueue：阻塞队列。
    - ThreadFactory threadFactory：线程工厂用来创建线程，可以自定义创建的方法。
    - RejectedExecutionHandler handle：拒绝策略。
    - 当来任务的时候，会使用核心线程来完成任务。当核心线程都在被使用的时候，再来任务将会被放入阻塞队列中等待。当阻塞队列也满了的时候并且核心线程的工作还没有结束将会创建额外的线程直到到达最大线程数。若到达最大线程数还有任务则会触发拒绝策略。
- **4种拒绝策略**
    - new ThreadPoolExecutor.AbortPolicy()**默认**  丢弃任务并抛出RejectedExecutionException异常。
    - new ThreadPoolExecutor.CallerRunsPolicy()    由提交任务的当前线程处理。
    - new ThreadPoolExecutor.DiscardPolicy()       悄无声息丢弃任务，但是不抛出异常。
    - new ThreadPoolExecutor.DiscardOldestPolicy() 悄无声息丢弃最老的任务，也不会抛出异常。

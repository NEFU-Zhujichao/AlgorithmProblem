# 多线程高频面试考点
### Volatile关键字的作用和实现
- 保证可见性
- 不保证原子性
- 避免指令重排(内存屏障) 再Volatile写的前后加锁避免指令重排
[讲解的很详细](https://blog.csdn.net/zzti_erlie/article/details/86355477)
### Volatile怎么保证原子性
- 加Lock锁，Synchronized锁。
- 使用原子类，解决原子性问题(AtomicInteger)
### 聊聊线程池你知道多少

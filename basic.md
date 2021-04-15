# Java基础面经
### ==和equals比较
- ==对比的是栈中的值。**基本数据类型比较的是变量值，引用类型是堆中内存对象的地址。**
- Object默认equals方法为==比较。但是我们可以重写equals方法。**String类重写了equals方法，如果说我们两个字符串只要是每个位置对应的字符相同equals方法就会返回true。**
### String StringBuffer StringBuilder 区别和使用场景
- String是final修饰的，是不可变的，每次操作都会生成一个新对象。
- StringBuffer StringBuilder都是在原对象上操作的。
- StringBuilder线程不安全。StringBuffer是线程安全的，StringBuffer的方法都是synchronized修饰的。
- 性能：StringBuilder>StringBuffer>String
- 场景：优先使用StringBuilder，如果这个字符串作为共享变量在多线程环境下使用时，若要保证结果正确则使用StringBuffer。若字符串不会改动，则使用String。
### 双亲委派模型
![](https://img-blog.csdnimg.cn/20210415162151887.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xvdmVfenNx,size_16,color_FFFFFF,t_70#pic_center)
### private的意义是什么
- private并不是解决安全问题的，如果想让解决代码的安全问题，请用别的办法。
- private的意义是OOP（面向对象编程）的封装概念。
### sleep与wait的区别
- 这两个方法来自不同的类分别是，sleep来自Thread类，和wait来自Object类。sleep是Thread的静态类方法，谁调用的谁去睡觉，即使在a线程里调用了b的sleep方法，实际上还是a去睡觉，要让b线程睡觉要在b的代码中调用sleep。
- 最主要是sleep方法没有释放锁，而wait方法释放了锁，使得其他线程可以使用同步控制块或者方法。
- wait，notify和notifyAll只能在同步控制方法或者同步控制块里面使用，而sleep可以在任何地方使用
### hashCode()和equals()，要一起重写
- 虽然通过重写equals方法使得逻辑上姓名和年龄相同的两个对象被判定为相等的对象（跟String类类似），但是要知道默认情况下，hashCode方法是将对象的存储地址进行映射。生成的是两个对象，它们的存储地址肯定不同。
### SimpleDateFormat线程不安全
> 可以看到，多个线程之间共享变量calendar，并修改calendar。因此在多线程环境下，当多个线程同时使用相同的SimpleDateFormat对象（如static修饰）的话，如调用format方法时，多个线程会同时调用calender.setTime方法，导致time被别的线程修改，因此线程是不安全的。 此外，parse方法也是线程不安全的，parse方法实际调用的是CalenderBuilder的establish来进行解析，其方法中主要步骤不是原子操作。
**解决方案：** 
- 将SimpleDateFormat定义成局部变量
- 加一把线程同步锁：synchronized(lock)
- 使用ThreadLocal，每个线程都拥有自己的SimpleDateFormat对象副本。如：
```java
public class Main{
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };  
}
```
### Java线程的6种状态
> 1. 初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。
> 2. 运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”。 线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取CPU的使用权，此时处于就绪状态（ready）。就绪状态的线程在获得CPU时间片后变为运行中状态（running）。
> 3. 阻塞(BLOCKED)：表示线程阻塞于锁。
> 4. 等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。
> 5. 超时等待(TIMED_WAITING)：该状态不同于WAITING，它可以在指定的时间后自行返回。
> 6. 终止(TERMINATED)：表示该线程已经执行完毕。
### java实现线程的方式
- 继承Thread类，重写run函数 不可以抛异常 无返回值
- 实现Runnable接口，重写run函数 不可以抛异常 无返回值
- 实现Callable接口，重写call函数 此方法可以抛异常,而前两种不能 而且此方法可以有返回值
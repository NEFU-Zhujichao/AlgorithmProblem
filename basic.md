# Java基础面经
### ==和equals比较
==对比的是栈中的值。**基本数据类型比较的是变量值，引用类型是堆中内存对象的地址。**  
Object默认equals方法为==比较。但是我们可以重写equals方法。**String类重写了equals方法，如果说我们两个字符串只要是每个位置对应的字符相同equals方法就会返回true。**
### String StringBuffer StringBuilder 区别和使用场景
- String是final修饰的，是不可变的，每次操作都会生成一个新对象。
- StringBuffer StringBuilder都是在原对象上操作的。
- StringBuilder线程不安全。StringBuffer是线程安全的，StringBuffer的方法都是synchronized修饰的。
- 性能：StringBuilder>StringBuffer>String
- 场景：优先使用StringBuilder，如果这个字符串作为共享变量在多线程环境下使用时，若要保证结果正确则使用StringBuffer。若字符串不会改动，则使用String。
### 重写和重载区别
> 重写 总结：  
1.发生在父类与子类之间  
2.方法名，参数列表，返回类型（除过子类中方法的返回类型是父类中返回类型的子类）必须相同 eg: 父类返回Object，子类可以返回Integer  
3.访问修饰符的限制一定要大于被重写方法的访问修饰符（public>protected>default>private)eg: 父类是protected，子类可以是public，private方法不能被重写。
4.重写方法一定不能抛出新的检查异常或者比被重写方法申明更加宽泛的检查型异常。eg: 父类是Exception，子类是RuntimeException。

> 重载 总结：  
1.重载是一个类中多态性的一种表现  
2.重载要求同名方法的参数列表不同(参数类型，参数个数甚至是参数顺序)  
3.重载的时候，返回值类型可以相同也可以不相同。无法以返回型别作为重载函数的区分标准(如果只有返回值类型不同，那么不可以进行重载)
### 双亲委派模型
![](https://img-blog.csdnimg.cn/20210415162151887.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xvdmVfenNx,size_16,color_FFFFFF,t_70#pic_center)
### private的意义是什么
- private并不是解决安全问题的，如果想要解决代码的安全问题，请用别的办法。
- private的意义是OOP（面向对象编程）的封装概念。
### hashCode()和equals()，要一起重写
> 前提条件：equals相同，hashcode也必须相同。但是hashcode相同不一定equals相同。
- 重写equals之后两个对象相同了，所以要让他们两个对象的hashcode也相同，保证前提条件是成立的。所以要按照一定的方式保证他俩的hashcode值相同，如果不重写的话默认是堆中内存存储地址，那么他俩一定不相同。(因为是两个对象，所以内存地址一定不相同)
- 虽然通过重写equals方法使得逻辑上姓名和年龄相同的两个对象被判定为相等的对象（跟String类类似），但是要知道默认情况下，hashCode方法是将对象的存储地址进行映射。生成的是两个对象，它们的存储地址肯定不同。
### SimpleDateFormat线程不安全
> 可以看到，多个线程之间共享变量calendar，并修改calendar。因此在多线程环境下，当多个线程同时使用相同的SimpleDateFormat对象（如static修饰）的话，如调用format方法时，多个线程会同时调用calender.setTime方法，导致time被别的线程修改，因此线程是不安全的。 此外，parse方法也是线程不安全的，parse方法实际调用的是CalenderBuilder的establish来进行解析，其方法中主要步骤不是原子操作。  

**解决方案：**
- 将SimpleDateFormat定义成局部变量
- 加一把线程同步锁：synchronized、lock
- 使用ThreadLocal，每个线程都拥有自己的SimpleDateFormat对象副本。如：
```java
private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL=new ThreadLocal<SimpleDateFormat>(){
    @Override
    protected SimpleDateFormat initialValue(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
};
```
### 强引用（StrongReference） 软引用（SoftReference） 弱引用（WeakReference） 虚引用（PhantomReference）
1. 强引用  
以前我们使用的大部分引用实际上都是强引用，这是使用最普遍的引用。如果一个对象具有强引用，**那就类似于必不可少的生活用品**，垃圾回收器绝不会回收它。当内存空间不足，Java 虚拟机宁愿抛出 OutOfMemoryError 错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足问题。
2. 软引用  
如果一个对象只具有软引用，**那就类似于可有可无的生活用品**。如果内存空间足够，垃圾回收器就不会回收它，如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。软引用可用来实现内存敏感的高速缓存。  
软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收，JAVA 虚拟机就会把这个软引用加入到与之关联的引用队列中。
3. 弱引用  
如果一个对象只具有弱引用，**那就类似于可有可无的生活用品**。弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于垃圾回收器是一个优先级很低的线程， 因此不一定会很快发现那些只具有弱引用的对象。  
弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java 虚拟机就会把这个弱引用加入到与之关联的引用队列中。
4. 虚引用  
"虚引用"顾名思义，就是形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收。    
**虚引用主要用来跟踪对象被垃圾回收的活动**。  
**虚引用与软引用和弱引用的一个区别在于**： 虚引用必须和引用队列（ReferenceQueue）联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。程序如果发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动。    
特别注意，在程序设计中一般很少使用弱引用与虚引用，使用软引用的情况较多，**这是因为软引用可以加速 JVM 对垃圾内存的回收速度，可以维护系统的运行安全，防止内存溢出（OutOfMemory）等问题的产生。**
### 序列化与反序列化基础知识
[序列化与反序列化基础知识](https://blog.csdn.net/litianxiang_kaola/article/details/100097019?utm_term=%E4%B8%BA%E4%BB%80%E4%B9%88%E6%9C%89%E4%BA%9B%E5%BA%8F%E5%88%97%E5%8C%96%E4%B8%8D%E9%9C%80%E8%A6%81%E5%AE%9E%E7%8E%B0%E6%8E%A5%E5%8F%A3&utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~sobaiduweb~default-1-100097019&spm=3001.4430)
- 序列化和反序列化
- 什么时候需要用到序列化和反序列化呢?
- 实现序列化和反序列化为什么要实现Serializable接口?
- 实现Serializable接口就算了, 为什么还要显示指定serialVersionUID的值?
   
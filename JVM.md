# JVM面经
### 反射
反射：我们告诉虚拟机要怎么操作，是一种解释操作，所以会有一定的性能消耗。  
反射，就是在运行时获取某个类的类型相关信息，如它的字段信息，方法信息，构造函数信息，父类信息，实现的接口信息。
### JVM包括两种数据类型
- 基本类型：数值类型，boolean类型，和returnAddress类型
  - 数值类型包括，整型，浮点型，和char类型。boolean类型同样只有true和false。returnAddress类型是一个指针，指向jvm指令的操作码，在Java中没有与之对应的类型。
- 引用类型：类类型，数组类型，和接口类型。
### JVM内存模型
![](https://uploadfiles.nowcoder.com/images/20210321/359311331_1616298576508/844176BACF4DCE28D7AD4AE3EF10A6D4) 
JVM调优在堆中调
> 运行时公有数据区   **一个JVM只有一个堆内存。**  
> 也就是说，永久代是 HotSpot 的概念，方法区是 Java 虚拟机规范中的定义，是一种规范，而永久代是一种实现，一个是标准一个是实现，其他的虚拟机实现并没有永久代这一说法。
- 方法区：静态变量、final、Class类模板、常量池(用于存放编译期生成的各种字面量和符号引用)
- 堆Heap(HotSpot)：保存我们所有引用类型的真实对象。对象实例和数组都是在堆上分配的，GC 也主要对这两类数据进行回收。
    - 堆分为三个区域：新生代、老年代、元空间。
    - 元空间：这个区域是常驻内存的。用来存放jdk自身携带的Class对象，interface元数据。存储的是java运行时的环境和类信息。这个区域不存在垃圾回收。关闭VM就会释放这个区域的内存。
        - jdk 1.6之前：常量池分为：运行时常量池逻辑包含字符串常量池存放在方法区，此时hotspot虚拟机对方法区的实现为永久代。
        - jdk1.7：字符串常量池被从方法区拿到了堆中, 这里没有提到运行时常量池,也就是说字符串常量池被单独拿到堆,运行时常量池剩下的东西还在方法区, 也就是hotspot中的永久代 。
        - jdk1.8之后：JDK1.8 hotspot移除了永久代用元空间(Metaspace)取而代之， 这时候字符串常量池还在堆, 运行时常量池还在方法区, 只不过方法区的实现从永久代变成了元空间(Metaspace)。元空间使用的是直接内存。
    - 元空间：逻辑上存在，物理上不存在。元空间里面存放的是类的元数据，这样加载多少类的元数据就不由 MaxPermSize 控制了, 而由系统的实际可用空间来控制，这样能加载的类就更多了。
> 运行时私有数据区
- 栈：8大基本类型、对象引用、实例方法
- 程序寄存器：所以程序计数器的主要作用是记录线程运行时的状态，方便线程被唤醒时能从上一次被挂起时的状态继续执行，需要注意的是，程序计数器是唯一一个在 Java 虚拟机规范中没有规定任何 OOM 情况的区域，所以这块区域也不需要进行 GC
- 虚拟机栈：生命周期与线程相同,每个方法被执行的同时会创建栈桢（下文会看到），主要保存执行方法时的局部变量表、操作数栈、动态连接和方法返回地址等信息,方法执行时入栈，方法执行完出栈，出栈就相当于清空了数据，入栈出栈的时机很明确，所以这块区域不需要进行 GC。
- 本地方法栈：与虚拟机栈功能非常类似，主要区别在于虚拟机栈为虚拟机执行 Java 方法时服务，而本地方法栈为虚拟机执行本地方法时服务的。这块区域也不需要进行 GC。
### 如何识别垃圾
> 引用计数法：最容易想到的一种方式是引用计数法，啥叫引用计数法，简单地说，就是对象被引用一次，在它的对象头上加一次引用次数，如果没有被引用（引用次数为 0），则此对象可回收。看起来用引用计数确实没啥问题了，不过它无法解决一个主要的问题：循环引用！
```java
public class ReferenceCountingGc {
  Object instance = null;
  public static void main(String[] args) {
    ReferenceCountingGc objA = new ReferenceCountingGc();
    ReferenceCountingGc objB = new ReferenceCountingGc();
    objA.instance = objB;
    objB.instance = objA;
    objA = null;
    objB = null;
  }
}
```
> 可达性算法：现代虚拟机基本都是采用这种算法来判断对象是否存活，可达性算法的原理是以一系列叫做  GC Roots  的对象为起点出发，引出它们指向的下一个节点，再以下个节点为起点，引出此节点指向的下一个结点。。。（这样通过 GC Roots 串成的一条线就叫引用链），直到所有的结点都遍历完毕,如果相关对象不在任意一个以 GC Roots 为起点的引用链中，则这些对象会被判断为「垃圾」,会被 GC 回收。
![](https://snailclimb.gitee.io/javaguide/docs/java/jvm/pictures/jvm%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6/72762049.png)  
- a, b 对象可回收，就一定会被回收吗?并不是，对象的 finalize 方法给了对象一次垂死挣扎的机会，当对象不可达（可回收）时，当发生GC时，会先判断对象是否执行了 finalize 方法，如果未执行，则会先执行 finalize 方法，我们可以在此方法里将当前对象与 GC Roots 关联，这样执行 finalize 方法之后，GC 会再次判断对象是否可达，如果不可达，则会被回收，如果可达，则不回收！
- **注意**：finalize 方法只会被执行一次，如果第一次执行 finalize 方法此对象变成了可达确实不会回收，但如果对象再次被 GC，则会忽略 finalize 方法，对象会被回收！这一点切记！
- 那么这些GC Roots到底是什么东西呢，哪些对象可以作为 GC Roots 呢，有以下几类
    - 虚拟机栈（栈帧中的本地变量表）中引用的对象
    - 本地方法栈中 JNI（即一般说的 Native 方法）引用的对象
    - 方法区中类静态属性引用的对象
    - 方法区中常量引用的对象
    - 所有被同步锁持有的对象
### GC
![](https://snailclimb.gitee.io/javaguide/docs/java/jvm/pictures/jvm%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6/01d330d8-2710-4fad-a91c-7bbbfaaefc0e.png)  
- 初始堆内存大小为10M：默认1/64 最大堆内存大小为10M：默认1/4 
- -Xms10m -Xmx10m -Xlog:gc*   
![](https://mmbiz.qpic.cn/mmbiz_png/OyweysCSeLUrYqPicjVwjuMChPrPicNHdXunqRMuhq6FZbcKicNibcvKmeTwic05ordoZ2wgW4hH5KtMlKu8DvQibE5w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 
> GC回收在伊甸园区和老年代。新生代分为伊甸园区、幸存区from、幸存区to。  
**复制算法最佳使用场景：对象存活度较低。也就是新生代，所以新生代使用复制算法。**
> 新生代主要用复制算法 from -> to 谁空谁是to区。老年代使用标记清除 + 标记整理混合。
- 复制算法：
    - 优点：没有内存碎片。
    - 缺点：浪费了内存空间。因为浪费了堆的一半，它一直有一半是空着的。极端情况移动一大堆对象，就会移动的时间很长，浪费资源。
- 标记清除算法：第一次扫描，先根据可达性算法标记出相应的可回收对象，第二次扫描，对可回收的对象进行回收。会产生内部碎片。
    - 优点：算法简单，不需要额外的空间。
    - 缺点：两次扫描，严重浪费时间。会产生内存碎片。
- 标记整理算法：防止内存碎片产生。再次扫描，向一端移动存活的对象。多了一个移动的成本。
- 分代收集算法：大部分的对象都很短命，都在很短的时间内都被回收了。所以分代收集算法根据对象存活周期的不同将堆分成新生代和老年代（Java8以前还有个永久代）,默认比例为 1 : 2，新生代又分为 Eden 区， from Survivor 区（简称S0），to Survivor 区(简称 S1),三者的比例为 8: 1 : 1，这样就可以根据新老年代的特点选择最合适的垃圾回收算法，我们把新生代发生的 GC 称为 Young GC（也叫 Minor GC）,老年代发生的 GC 称为 Full GC。
- 分代收集工作原理：
  - 1、对象在新生代的分配与回收：
    - **新生的对象放在Eden区，当Eden区放满之后，会触发一次轻GC。在Eden区存活的对象存入To区，From区的对象也移动到To区，然后To区变为From区，From区变为To区。To区对象年龄标记为1。**
  - 2、对象何时晋升老年代：
    - **当对象的年龄达到了我们设定的阈值(默认为15)，则会从S0（或S1）晋升到老年代。对象晋升到老年代的年龄阈值，可以通过参数 -XX:MaxTenuringThreshold 来设置。**
    - **大对象 当某个对象分配需要大量的连续内存时，此时对象的创建不会分配在 Eden 区，会直接分配在老年代，因为如果把大对象分配在 Eden 区, 轻GC后再移动到S0,S1会有很大的开销（对象比较大，复制会比较慢，也占空间），也很快会占满 S0,S1 区，所以干脆就直接移到老年代**
    - **还有一种情况也会让对象晋升到老年代，即在 S0（或S1） 区相同年龄的对象大小之和大于 S0（或S1）空间一半以上时，则年龄大于等于该年龄的对象也会晋升到老年代。同时取这个年龄和MaxTenuringThreshold中更小的一个值，作为新的晋升年龄阈值**
  - 3、空间分配担保：在发生 MinorGC 之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象的总空间，如果大于，那么Minor GC 可以确保是安全的,如果不大于，那么虚拟机会查看 HandlePromotionFailure 设置值是否允许担保失败。如果允许，那么会继续检查老年代最大可用连续空间是否大于历次晋升到老年代对象的平均大小，如果大于则进行 Minor GC，否则可能进行一次 Full GC。
  - 4、Stop The World：如果老年代满了，会触发 Full GC, Full GC 会同时回收新生代和老年代（即对整个堆进行GC），它会导致 Stop The World（简称 STW）,造成挺大的性能开销。什么是 STW ？所谓的 STW, 即在 GC（minor GC 或 Full GC）期间，只有垃圾回收器线程在工作，其他工作线程则被挂起。
- ~~标记清除和整理结合算法：先进行一定次数的标记清除算法，然后再标记整理，减少移动成本，不用每次都移动。~~
### JVM新生代为什么要存在幸存区
Survivor的存在意义，就是减少被送到老年代的对象，进而减少Full GC的发生，Survivor的预筛选保证，只有经历16次Minor GC还能在新生代中存活的对象，才会被送到老年代。  
设置两个Survivor区最大的好处就是解决了碎片化。刚刚新建的对象在Eden中，一旦Eden满了，触发一次Minor GC，Eden中的存活对象就会被移动到Survivor区。这样继续循环下去，下一次Eden满了的时候，问题来了，此时进行Minor GC，Eden和Survivor各有一些存活对象，如果此时把Eden区的存活对象硬放到Survivor区，很明显这两部分对象所占有的内存是不连续的，也就导致了内存碎片化。
### 垃圾收集器种类
> 如果说收集算法是内存回收的方法论，那么垃圾收集器就是内存回收的具体实现。Java 虚拟机规范并没有规定垃圾收集器应该如何实现，因此一般来说不同厂商，不同版本的虚拟机提供的垃圾收集器实现可能会有差别，一般会给出参数来让用户根据应用的特点来组合各个年代使用的收集器，主要有以下垃圾收集器
- 在新生代工作的垃圾回收器：Serial, ParNew, ParallelScavenge
- 在老年代工作的垃圾回收器：CMS，Serial Old, Parallel Old
- 同时在新老生代工作的垃圾回收器：G1
---
- 什么是 STW ？所谓的 STW, 即在 GC（minor GC 或 Full GC）期间，只有垃圾回收器线程在工作，其他工作线程则被挂起。
#### 新生代收集器
> Serial 收集器
- Serial 收集器是**工作在新生代的，单线程的垃圾收集器**，单线程意味着它只会使用一个 CPU 或一个收集线程来完成垃圾回收，不仅如此，还记得我们上文提到的 STW 了吗，它在进行垃圾收集时，其他用户线程会暂停，直到垃圾收集结束，也就是说在 GC 期间，此时的应用不可用。
- 看起来单线程垃圾收集器不太实用，不过我们需要知道的任何技术的使用都不能脱离场景，在 Client 模式下，它简单有效（与其他收集器的单线程比），对于限定单个 CPU 的环境来说，Serial 单线程模式无需与其他线程交互，减少了开销，专心做 GC 能将其单线程的优势发挥到极致，另外在用户的桌面应用场景，分配给虚拟机的内存一般不会很大，收集几十甚至一两百兆（仅是新生代的内存，桌面应用基本不会再大了），STW 时间可以控制在一百多毫秒内，只要不是频繁发生，这点停顿是可以接受的，所以对于运行在 Client 模式下的虚拟机，**Serial 收集器是新生代的默认收集器**
> ParNew 收集器
- **ParNew 收集器是 Serial 收集器的多线程版本，除了使用多线程，其他像收集算法,STW,对象分配规则，回收策略与 Serial 收集器完成一样**，在底层上，这两种收集器也共用了相当多的代码，它的垃圾收集过程如下
![](https://mmbiz.qpic.cn/mmbiz_png/OyweysCSeLUrYqPicjVwjuMChPrPicNHdXaCHQlQ54mGjmJ5ac4D700X8nFBcMqpayjGWdrdhNcMPY89NZpPvLSA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)  
- ParNew 主要工作在 Server 模式，我们知道服务端如果接收的请求多了，响应时间就很重要了，多线程可以让垃圾回收得更快，也就是减少了 STW 时间，能提升响应时间，所以是许多运行在 Server 模式下的虚拟机的首选新生代收集器，另一个与性能无关的原因是因为除了 Serial  收集器，只有它能与 CMS 收集器配合工作，CMS 是一个划时代的垃圾收集器，是真正意义上的并发收集器，它第一次实现了垃圾收集线程与用户线程（基本上）同时工作，它采用的是传统的 GC 收集器代码框架，与 Serial,ParNew 共用一套代码框架，所以能与这两者一起配合工作，而后文提到的 Parallel Scavenge 与 G1 收集器没有使用传统的 GC 收集器代码框架，而是另起炉灶独立实现的，另外一些收集器则只是共用了部分的框架代码,所以无法与 CMS 收集器一起配合工作。
- 在多 CPU 的情况下，由于 ParNew 的多线程回收特性，毫无疑问垃圾收集会更快，也能有效地减少 STW 的时间，提升应用的响应速度。
> Parallel Scavenge 收集器
- Parallel Scavenge 收集器也是一个**使用复制算法，多线程，工作于新生代的垃圾收集器**，看起来功能和 ParNew 收集器一样，它有啥特别之处吗
- 关注点不同，CMS 等垃圾收集器关注的是尽可能缩短垃圾收集时用户线程的停顿时间，而 Parallel Scavenge 目标是达到一个可控制的吞吐量（吞吐量 = 运行用户代码时间 / （运行用户代码时间+垃圾收集时间）），也就是说 CMS 等垃圾收集器更适合用到与用户交互的程序，因为停顿时间越短，用户体验越好，而 Parallel Scavenge 收集器关注的是吞吐量，所以更适合做后台运算等不需要太多用户交互的任务。
- Parallel Scavenge 收集器提供了两个参数来精确控制吞吐量，分别是控制最大垃圾收集时间的 -XX:MaxGCPauseMillis 参数及直接设置吞吐量大小的 -XX:GCTimeRatio（默认99%）
- 除了以上两个参数，还可以用 Parallel Scavenge 收集器提供的第三个参数 -XX:UseAdaptiveSizePolicy，开启这个参数后，就不需要手工指定新生代大小,Eden 与 Survivor 比例（SurvivorRatio）等细节，只需要设置好基本的堆大小（-Xmx 设置最大堆）,以及最大垃圾收集时间与吞吐量大小，虚拟机就会根据当前系统运行情况收集监控信息，动态调整这些参数以尽可能地达到我们设定的最大垃圾收集时间或吞吐量大小这两个指标。**自适应策略也是 Parallel Scavenge  与 ParNew 的重要区别！**
#### 老年代收集器
> Serial Old 收集器
- 上文我们知道， Serial 收集器是工作于新生代的单线程收集器，与之相对地，Serial Old 是工作于老年代的单线程收集器，此收集器的主要意义在于给 Client 模式下的虚拟机使用，如果在 Server 模式下，则它还有两大用途：一种是在 JDK 1.5 及之前的版本中与 Parallel Scavenge 配合使用，另一种是作为 CMS 收集器的后备预案,在并发收集发生 Concurrent Mode Failure 时使用（后文讲述）,它与 Serial 收集器配合使用示意图如下
![](https://mmbiz.qpic.cn/mmbiz_png/OyweysCSeLUrYqPicjVwjuMChPrPicNHdXXsEhJL7oahZ9JjKw7EKJ1rr1ic6fPTrEzLia8Ede4T2uqZdOUeqrf0nw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)  
> Parallel Old 收集器
- Parallel Old 是相对于 Parallel Scavenge 收集器的老年代版本，使用多线程和标记整理法，两者组合示意图如下,这两者的组合由于都是多线程收集器，真正实现了「吞吐量优先」的目标
![](https://mmbiz.qpic.cn/mmbiz_png/OyweysCSeLUrYqPicjVwjuMChPrPicNHdXyM3YO3k6awoMwkkZLT6CSBnuwaYtrBOlaqBzkbas4Jf5OGolSh5lIg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)  
> CMS 收集器
- CMS 收集器是以实现最短 STW 时间为目标的收集器，如果应用很重视服务的响应速度，希望给用户最好的体验，则 CMS 收集器是个很不错的选择！
- 我们之前说老年代主要用标记整理法，而 CMS 虽然工作于老年代，但采用的是**标记清除法**，主要有以下四个步骤
1. 初始标记
2. 并发标记
3. 重新标记
4. 并发清除  
![](https://mmbiz.qpic.cn/mmbiz_png/OyweysCSeLUrYqPicjVwjuMChPrPicNHdXBk0HdaA4x1FqkobKRxdEribRMQn86zDt9FpceO1icmLwd6oichSjk4ibZQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
- 从图中可以的看到初始标记和重新标记两个阶段会发生 STW，造成用户线程挂起，不过初始标记仅标记 GC Roots 能关联的对象，速度很快，并发标记是进行 GC Roots  Tracing 的过程，重新标记是为了修正并发标记期间因用户线程继续运行而导致标记产生变动的那一部分对象的标记记录，这一阶段停顿时间一般比初始标记阶段稍长，但远比并发标记时间短。
- 整个过程中耗时最长的是并发标记和标记清理，不过这两个阶段用户线程都可工作，所以不影响应用的正常使用，所以总体上看，可以认为 CMS 收集器的内存回收过程是与用户线程一起并发执行的。
- 但是 CMS 收集器远达不到完美的程度，主要有以下三个缺点
  - CMS 收集器对 CPU 资源非常敏感  原因也可以理解，比如本来我本来可以有 10 个用户线程处理请求，现在却要分出 3 个作为回收线程，吞吐量下降了30%，CMS 默认启动的回收线程数是 （CPU数量+3）/ 4, 如果 CPU 数量只有一两个，那吞吐量就直接下降 50%,显然是不可接受的
  - CMS 无法处理浮动垃圾（Floating Garbage）,可能出现 「Concurrent Mode Failure」而导致另一次 Full GC 的产生，由于在并发清理阶段用户线程还在运行，所以清理的同时新的垃圾也在不断出现，这部分垃圾只能在下一次 GC 时再清理掉（即浮云垃圾），同时在垃圾收集阶段用户线程也要继续运行，就需要预留足够多的空间要确保用户线程正常执行，这就意味着 CMS 收集器不能像其他收集器一样等老年代满了再使用，JDK 1.5 默认当老年代使用了68%空间后就会被激活，当然这个比例可以通过 -XX:CMSInitiatingOccupancyFraction 来设置，但是如果设置地太高很容易导致在 CMS 运行期间预留的内存无法满足程序要求，会导致 Concurrent Mode Failure 失败，这时会启用 Serial Old 收集器来重新进行老年代的收集，而我们知道 Serial Old 收集器是单线程收集器，这样就会导致 STW 更长了。
  - CMS 采用的是标记清除法，上文我们已经提到这种方法会产生大量的内存碎片，这样会给大内存分配带来很大的麻烦，如果无法找到足够大的连续空间来分配对象，将会触发 Full GC，这会影响应用的性能。
> G1（Garbage First）收集器
- G1 收集器是面向服务端的垃圾收集器，被称为驾驭一切的垃圾回收器，主要有以下几个特点
  - 像 CMS 收集器一样，能与应用程序线程并发执行。
  - 整理空闲空间更快。
  - 需要 GC 停顿时间更好预测。
  - 不会像 CMS 那样牺牲大量的吞吐性能。
  - 不需要更大的 Java Heap
- 与 CMS 相比，它在以下两个方面表现更出色
  - 运作期间不会产生内存碎片，G1 从整体上看采用的是标记-整理法，局部（两个 Region）上看是基于复制算法实现的，两个算法都不会产生内存碎片，收集后提供规整的可用内存，这样有利于程序的长时间运行。
  - 在 STW 上建立了可预测的停顿时间模型，用户可以指定期望停顿时间，G1 会将停顿时间控制在用户设定的停顿时间以内。
-  G1 各代的存储地址不是连续的，每一代都使用了 n 个不连续的大小相同的 Region，每个Region占有一块连续的虚拟内存地址，如图示
![](https://mmbiz.qpic.cn/mmbiz_png/OyweysCSeLUrYqPicjVwjuMChPrPicNHdX7eYnibUFHuo7AibZyLsaHq6tWpWnwLO3jj1rk3mThrAPSsdmVMrgzlLQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 
- 除了和传统的新老生代，幸存区的空间区别，Region还多了一个H，它代表Humongous，这表示这些Region存储的是巨大对象（humongous object，H-obj），即大小大于等于region一半的对象，这样超大对象就直接分配到了老年代，防止了反复拷贝移动。那么 G1 分配成这样有啥好处呢？
- 传统的收集器如果发生 Full GC 是对整个堆进行全区域的垃圾收集，而分配成各个 Region 的话，方便 G1 跟踪各个 Region 里垃圾堆积的价值大小（回收所获得的空间大小及回收所需经验值），这样根据价值大小维护一个优先列表，根据允许的收集时间，优先收集回收价值最大的 Region,也就避免了整个老年代的回收，也就减少了 STW 造成的停顿时间。同时由于只收集部分 Region,可就做到了 STW 时间的可控。
- G1 收集器的工作步骤如下
  - 初始标记
  - 并发标记
  - 最终标记
  - 筛选回收
![](https://mmbiz.qpic.cn/mmbiz_png/OyweysCSeLUrYqPicjVwjuMChPrPicNHdX8S08rDRVliaVW84ibCM9kzCtIxCIYqFGGbiad6VPBV9qSZEqOJtTuyyicQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 
- 可以看到整体过程与 CMS 收集器非常类似，筛选阶段会根据各个 Region 的回收价值和成本进行排序，根据用户期望的 GC 停顿时间来制定回收计划。
### OOM排查
- VM参数 -Xms10m -Xmx10m -XX:+HeapDumpOnOutOfMemoryError -Xms10m -Xmx10m -XX:+HeapDumpOnClassNotFoundException
- 把OOM的文件Dump下来，用Jprofiler分析大对象所在的位置

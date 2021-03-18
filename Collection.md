# 集合框架面经总结
### Collection接口下有 List(ArrayList,LinkedList) Set(HashSet) Map(HashMap)
- ArrayList：底层数据结构为数组，它初始化的时候数据量是0，当你add的时候默认会变成10，它的扩容机制是每次扩容容量是它之前容量的1.5倍，查找访问通过索引下标，速度快，增删元素的效率低，线程不安全。 当遇到线程安全问题时，考虑使用JUC包下的CopyOnWriteArrayList。如果使用尾插法并且指定初始容量可以极大提高它的性能。
- LinkedList： 底层数据结构为双向链表，提供头插和尾插两种方式，适合插入删除频繁的情况下，内部维护了链表的长度。
- HashMap：1.7 底层数据结构 数组加单链表，数据节点是一个Entry内部类。发生hash冲突时，新元素插入到链表头中，即新元素总是添加到数组中，旧元素移动到链表中。利用头插法进行resize() ，扩容时调用resize()然后又调用了transfer()方法，把里面的Entry进行rehash()。在扩容resize()过程中在将旧数组上的数据转移到新数组上时，转移操作 = 按旧链表的正序遍历链表、在新链表的头部依次插入，即在转移数据、扩容后，容易出现链表逆序的情况 。多线程下resize()容易出现死循环，此时若(多线程)并发执行 put()操作，一旦出现扩容情况，则容易出现环形链表，从而在获取数据、遍历链表时 形成死循环，即死锁的状态 。
- HashMap：1.8 底层数据结构 数组加单链表(红黑树)内部Entry节点改为Node节点存储。当单链表长度 >=8并且hash桶大小大于64时会将单链表转化为红黑树，当红黑树节点个数小于6时退化为链表提高插入删除性能。转移数据操作 = 按旧链表的正序遍历链表、在新链表的尾部依次插入，所以不会出现链表逆序、倒置的情况，故不容易出现环形链表的情况 ，但jdk1.8仍是线程不安全的，因为没有加同步锁保护。
- 扩容机制：如果在初始化HashMap时没有给定容量，则容量为默认的16。并且默认的负载因子为0.75，扩容的阈值threshold为 负载因子*初始容量(0.75 * 16) 当put操作时，若size > threshold 则会将原来的数据节点扩容为原来的两倍。
[具体区别](https://blog.csdn.net/sky_xin/article/details/84926333) 
- HashMap允许key和value都是null，但是HashTable不允许。
```java
public class Main{
/**实际存储的key-value键值对的个数*/
transient int size;
/**阈值，当table == {}时，该值为初始容量（初始容量默认为16）；当table被填充了，也就是为table分配内存空间后，threshold一般为 capacity*loadFactory。HashMap在进行扩容时需要参考threshold，后面会详细谈到*/
int threshold;
/**负载因子，代表了table的填充度有多少，默认是0.75
 加载因子存在的原因，还是因为减缓哈希冲突，如果初始桶为16，等到满16个元素才扩容，某些桶里可能就有不止一个元素了。
 所以加载因子默认为0.75，也就是说大小为16的HashMap，到了第13个元素，就会扩容成32。
 */
final float loadFactor;
/**HashMap被改变的次数，由于HashMap非线程安全，在对HashMap进行迭代时，
 如果期间其他线程的参与导致HashMap的结构发生变化了（比如put，remove等操作），
 需要抛出异常ConcurrentModificationException*/
transient int modCount;
}
```
### 为什么是2的次幂
[详细](https://blog.csdn.net/sidihuo/article/details/78489820) 
- 为了提升模运算的计算速度。减少碰撞，或者说为了让HashMap里面元素分布尽量均匀些，应该是哈希算法的功能。
- HashMap为了存取高效，要尽量较少碰撞，就是要尽量把数据分配均匀，每个链表长度大致相同，这个实现就在把数据存到哪个链表中的算法。
- 这个算法实际就是取模，hash%length，计算机中直接求余效率不如位移运算，源码中做了优化hash&(length-1)，hash%length==hash&(length-1)的前提是length是2的n次方。
- 为什么这样能均匀分布减少碰撞呢？2的n次方实际就是1后面n个0，2的n次方-1  实际就是n个1；其实就是按位“与”的时候，每一位都能&1，这样会充分利用到二进制下hash值的后面几位。如果不是2的次幂，这样n-1 的二进制后面一定会有0的位，这样这位与操作之后一定是0，所以数组上的某些位置可能一直得不到利用，数组元素分布不均匀，导致使用效率降低。
### 线程安全的map有HashTable，或者加Synchronized、Lock，或者Collections.synchronizedMap()，为什么选择用ConcurrentHashMap
- 锁粒度小，并发度更高。普通的HashTable直接对里面的方法加了全局的synchronized对象锁，ConcurrentHashMap在jdk1.8之后底层数据结构也变成了数组加链表加红黑树，它只会锁住目前我在的Node节点的值，在上锁时使用了CAS加synchronized，再加上jdk1.6以后对synchronized进行了锁升级的优化，所以它的效率是更高的。
- HashTable是加全局锁Synchronized，ConcurrentHashMap加的是分段锁。并且ConcurrentHashMap在1.7和1.8有一些改变。  
### ConcurrentHashMap原理，以及jdk7和jdk8的区别。
- jdk7：
    - 数据结构：Segment(继承ReentrantLock)+HashEntry+单链表，一个Segment中包含一个HashEntry数组，每个HashEntry又是一个链表。
    - 元素查询：两次hash，第一次hash定位到segment，第二次hash定位到元素所在的链表的头部。
    - 锁：Segment分段锁(继承ReentrantLock)，锁定正在操作的segment，其他的segment不受影响，并发度为segment的大小。
- jdk8：
    - 数据结构：synchronized+CAS+Node+单链表(红黑树)，Node的val和next都用volatile修饰，保证可见性。查找替换赋值都用CAS。
    - 锁：锁定链表的Head节点(jdk1.7锁的是segment，里面有很多的链表)，不影响其他元素的读写，锁粒度更细，效率更高。扩容时，阻塞所有的读写操作，并发扩容。
    - 读操作无锁：Node的val和next都是volatile修饰的，读写线程对该变量互相可见。
### 锁升级过程
[讲解的很详细](https://blog.csdn.net/wangyy130/article/details/106495180/) 
synchronize锁升级过程：jdk高版本之后对synchronize关键字进行了很多优化，其中一项就是锁升级，以前synchronize默认就是悲观锁，加锁解锁的开销都比较大。所以引入了偏向锁、轻量级锁、重量级锁。 synchronized锁有四种状态，无锁，偏向锁，轻量级锁，重量级锁，这几个状态会随着竞争状态逐渐升级，锁可以升级但不能降级，但是偏向锁状态可以被重置为无锁状态。 
CAS：compare and swap。会出现ABA问题，中间有线程快速改过值了但是又改回了原值，一般会像乐观锁一样解决此类问题。
![CAS](https://img-blog.csdnimg.cn/20200602171359487.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmd5eTEzMA==,size_16,color_FFFFFF,t_70) 
- CAS解决ABA问题：
  - AtomicMarkableReference：使用boolean变量——表示引用变量是否被更改过,不关心中间变量变化了几次。
  - AtomicStampedReference：其中的构造方法中initialStamp（时间戳）用来唯一标识引用变量,引用变量中途被更改了几次。
- **对象头**：synchronized用的锁是存在java对象头mark word里的。其中对象头的最后两位代表是否加锁的标志位，锁标志位如果是01的话需要根据前一位的是否为偏向锁来判断当前的锁状态，如果前一位为0则代表无锁状态，如果为1则代表有偏向锁。后两位：00代表轻量级锁，10代表重量级锁，11代表GC垃圾回收的标记信息。
- **偏向锁**：当一个线程访问同步块时，会先判断锁标志位是否为01，如果是01，则判断是否为偏向锁，如果是，会先判断当前锁对象头中是否存储了当前的线程id，如果存储了，则直接获得锁。如果对象头中指向不是当前线程id，则通过CAS尝试将自己的线程id存储进当前锁对象的对象头中来获取偏向锁。当cas尝试获取偏向锁成功后则继续执行同步代码块，否则等待安全点的到来撤销原来线程的偏向锁，撤销时需要暂停原持有偏向锁的线程，判断线程是否活动状态，如果已经退出同步代码块则唤醒新的线程开始获取偏向锁，否则开始锁竞争进行锁升级过程，升级为轻量级锁。
- **轻量级锁**：当出现锁竞争时，会升级为轻量级锁。线程获取轻量级锁时会先把锁对象的对象头MarkWord复制一份到该线程的栈帧中创建的用于存储锁记录的空间(LockRecord)，然后使用CAS把对象头中的内容替换为线程存储的锁记录(LockRecord)的地址。如果成功则当前线程获取锁，如果失败则使用自旋来获取锁。替换成功之后将锁标志位改为00，表示获取轻量级锁成功。lockrecord的作用：在这里实现了锁重入，每当同一个线程多次获取同一个锁时，会在当前栈帧中放入一个lockrecord，但是重入是放入的lockrecord关于锁信息的内容为null，代表锁重入。当轻量级解锁时，每解锁一次则从栈帧中弹出一个lockrecord，直到为0。自旋锁简单来说就是让另一条竞争该锁的线程在循环中不断CAS，但是如果自旋的时间太长也不行，因为自旋是要消耗CPU的，因此自旋的次数是有限制的，如果自旋次数到了但是工作线程还没有释放锁，那么这个时候轻量级锁就会膨胀为重量级锁。重量级锁把除了拥有锁的线程都阻塞，防止CPU空转。
![](https://img-blog.csdnimg.cn/20200603145142474.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmd5eTEzMA==,size_16,color_FFFFFF,t_70)
![](https://img-blog.csdnimg.cn/2020060314564655.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmd5eTEzMA==,size_16,color_FFFFFF,t_70)
- **重量级锁**：在重量级锁中将LockRecord对象替换为了monitor对象的实现。主要通过monitorenter和monitorexit两个指令来实现。需要经过系统调用，在并发低的情况下效率会低。重量级锁在进行锁重入的时候每获取到锁一次会对monitor对象中的计数器+1，等锁退出时则会相应的-1，直到减到0为止，锁完全退出。

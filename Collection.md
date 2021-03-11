# 集合框架面经总结
![脑图](https://www.bilibili.com/read/cv6238451?spm_id_from=333.788.b_636f6d6d656e74.6#reply2963501648)
Collection接口下有List(ArrayList,LinkedList)Set(HashSet)Map(HashMap)
- ArrayList：底层数据结构为数组，它初始化的时候数据量是0，当你add的时候默认会变成10，它的扩容机制是每次扩容容量是它之前容量的1.5倍，查找访问通过索引下标，速度快，增删元素的效率低，线程不安全。 
  当遇到线程安全问题时，考虑使用JUC包下的CopyOnWriteArrayList
- LinkedList： 底层数据结构为双向链表，提供头插和尾插两种方式，适合插入删除频繁的情况下，内部维护了链表的长度。
- HashMap：1.7 底层数据结构 数组加单链表 利用头插法进行resize() 
- HashMap：1.8 底层数据结构 数组加单链表(红黑树)当单链表长度 >=8并且hash桶大小大于64时会将单链表转化为红黑树，当红黑树节点个数小于6时退化为链表提高插入删除性能。 
[具体区别](https://blog.csdn.net/sky_xin/article/details/84926333)
```java
/**实际存储的key-value键值对的个数*/
transient int size;

/**阈值，当table == {}时，该值为初始容量（初始容量默认为16）；当table被填充了，也就是为table分配内存空间后，
threshold一般为 capacity*loadFactory。HashMap在进行扩容时需要参考threshold，后面会详细谈到*/
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
```
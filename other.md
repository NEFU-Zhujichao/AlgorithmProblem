# 面经整理
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
![click](https://github.com/NEFU-Zhujichao/AlgorithmProblem/blob/master/src/main/resources/img/%E5%8F%8C%E4%BA%B2%E5%A7%94%E6%B4%BE.png)
### private的意义是什么
- private并不是解决安全问题的，如果想让解决代码的安全问题，请用别的办法。
- private的意义是OOP（面向对象编程）的封装概念。
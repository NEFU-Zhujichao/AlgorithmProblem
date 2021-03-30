# Spring全家桶面经
- IOC：控制反转：将对象的创建权，由Spring管理。
- DI（依赖注入）：在Spring创建对象的过程中，把对象依赖的属性注入到类中。 
---
[Bean的实例化过程源码debug](https://blog.csdn.net/qq_18433441/article/details/81866142)  
![Bean的实例化过程图解](https://img-blog.csdn.net/20180820100800644?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzE4NDMzNDQx/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70) 
### Spring IOC 初始化流程
- resource定位：即寻找用户定义的bean资源，由 ResourceLoader通过统一的接口Resource接口来完成。
- beanDefinition载入：BeanDefinitionReader读取、解析Resource定位的资源 成BeanDefinition 载入到ioc中（通过HashMap进行维护BeanDefinition）。
- BeanDefinition注册：即向IOC容器注册这些BeanDefinition， 通过BeanDefinitionRegistry实现。
### Bean的生命周期
- 实例化Bean：Ioc容器通过获取BeanDefinition对象中的信息进行实例化，实例化对象被包装在BeanWrapper对象中。
  - Spring使用BeanDefinition来装载着我们给Bean定义的元数据
  - 实例化Bean的时候会遍历BeanDefinitionMap
  - SpringBean的实例化和属性赋值是分两步来做的。
- 设置对象属性（DI）：通过BeanWrapper提供的设置属性的接口完成属性依赖注入。
- 注入Aware接口（BeanFactoryAware， 可以用这个方式来获取其它 Bean，ApplicationContextAware）：Spring会检测该对象是否实现了xxxAware接口，并将相关的xxxAware实例注入给bean。
- BeanPostProcessor：自定义的处理（分前置处理和后置处理）。
- InitializingBean和init-method：执行我们自己定义的初始化方法。
- 使用。
- destroy：bean的销毁。
> **在SpringBean的生命周期，Spring预留了很多的hook给我们去扩展** 
> 1. Bean实例化之前有BeanFactoryPostProcessor
> 2. Bean实例化之后，初始化之前，有相关的Aware接口供我们去拿到Context相关信息
> 3. 环绕着初始化阶段，有BeanPostProcessor(AOP的关键)
> 4. 在初始化阶段，有各种的init方法供我们去自定义
### Spring AOP 实现原理
- AOP：
  - 不同的模块（对象）间有时会出现公共的行为，这种公共的行为很难通过继承的方式来实现，如果用工具类的话也不利于维护，代码也显得异常繁琐。切面（AOP）的引入就是为了解决这类问题而生的，它要达到的效果是保证开发者在不修改源代码的前提下，为系统中不同的业务组件添加某些通用功能。
  - 这些行为都属于业务无关的，使用工具类嵌入的方式导致与业务代码紧藕合，很不合工程规范，代码可维护性极差!切面就是为了解决此类问题应运而生的，能做到相同功能的统一管理，对业务代码无侵入。
- 当委托类(被代理类)需要实现接口时，走JDK动态代理生成代理类对象。若不需要实现接口，则使用cglib字节码技术生成代理类对象。
- 代理分为静态代理和动态代理。动态代理又有 JDK 代理和 CGLib 代理两种。
- 静态代理：由程序员创建代理类或特定工具自动生成源代码再对其编译。在编译时已经将接口，被代理类（委托类），代理类等确定下来，在程序运行前代理类的.class文件就已经存在了
- 动态代理：在程序运行后通过反射创建生成字节码再由 JVM 加载而成。
- 静态代理主要有两大劣势：
  - 代理类只代理一个委托类（其实可以代理多个，但不符合单一职责原则），也就意味着如果要代理多个委托类，就要写多个代理（别忘了静态代理在编译前必须确定）。
  - 第一点还不是致命的，再考虑这样一种场景：如果每个委托类的每个方法都要被织入同样的逻辑，比如说我要计算前文提到的每个委托类每个方法的耗时，就要在方法开始前，开始后分别织入计算时间的代码，那就算用代理类，它的方法也有无数这种重复的计算时间的代码。
#### jdk动态代理实现方式：
```java
// 委托类
public class RealSubject implements Subject {
   @Override
   public void request() {
       // 卖房
       System.out.println("卖房");
   }
}
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
public class ProxyFactory {
   private Object target;// 维护一个目标对象
   public ProxyFactory(Object target) {
       this.target = target;
   }
   // 为目标对象生成代理对象
   public Object getProxyInstance() {
       return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
               new InvocationHandler() {
                   @Override
                   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                       System.out.println("计算开始时间");
                       // 执行目标对象方法
                       method.invoke(target, args);
                       System.out.println("计算结束时间");
                       return null;
                   }
               });
   }
   public static void main(String[] args) {
       RealSubject realSubject = new RealSubject();
       System.out.println(realSubject.getClass());
       Subject subject = (Subject) new ProxyFactory(realSubject).getProxyInstance();
       System.out.println(subject.getClass());
       subject.request();
   }
}
/** 打印结果如下:
 *  原始类:class com.example.demo.proxy.staticproxy.RealSubject
 *  代理类:class com.sun.proxy.$Proxy0
 *  计算开始时间
 *  卖房
 *  计算结束时间
*/
```
> 注意看看 Proxy 的 newProxyInstance 签名
> public static Object newProxyInstance(ClassLoader loader,
>                                       Class<?>[] interfaces,
>                                       InvocationHandler h);
> - loader: 代理类的ClassLoader，最终读取动态生成的字节码，并转成 java.lang.Class 类的一个实例（即类），通过此实例的 newInstance() 方法就可以创建出代理的对象.
> - interfaces: 委托类实现的接口，JDK 动态代理要实现所有的委托类的接口.
> - InvocationHandler: 委托对象所有接口方法调用都会转发到 InvocationHandler.invoke()，在 invoke() 方法里我们可以加入任何需要增强的逻辑 主要是根据委托类的接口等通过反射生成的。
---
> 由于动态代理是程序运行后才生成的，哪个委托类需要被代理到，只要生成动态代理即可，避免了静态代理那样的硬编码，另外所有委托类实现接口的方法都会在 Proxy 的 InvocationHandler.invoke() 中执行，这样如果要统计所有方法执行时间这样相同的逻辑，可以统一在 InvocationHandler 里写， 也就避免了静态代理那样需要在所有的方法中插入同样代码的问题，代码的可维护性极大的提高了。
#### cglib动态代理实现方式：
> CGlib 动态代理也提供了类似的  Enhance 类，增强逻辑写在 MethodInterceptor.intercept() 中，也就是说所有委托类的非 final 方法都会被方法拦截器拦截。
```java
public class MyMethodInterceptor implements MethodInterceptor {
   @Override
   public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
       System.out.println("目标类增强前！！！");
       //注意这里的方法调用，不是用反射哦！！！
       Object object = proxy.invokeSuper(obj, args);
       System.out.println("目标类增强后！！！");
       return object;
   }
}
public class CGlibProxy {
   public static void main(String[] args) {
       //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
       Enhancer enhancer = new Enhancer();
       //设置目标类的字节码文件
       enhancer.setSuperclass(RealSubject.class);
       //设置回调函数
       enhancer.setCallback(new MyMethodInterceptor());
       //这里的creat方法就是正式创建代理类
       RealSubject proxyDog = (RealSubject) enhancer.create();
       //调用代理类的eat方法
       proxyDog.request();
   }
}
/**
 * 可以看到主要就是利用 Enhancer 这个类来设置委托类与方法拦截器，这样委托类的所有非 final 方法就能被方法拦截器拦截，从而在拦截器里实现增强。
*/
```
- 通过继承自委托类，重写委托类的非 final 方法（final 方法不能重载），并在方法里调用委托类的方法来实现代码增强的。可以看到它并不要求委托类实现任何接口，而且 CGLIB 是高效的代码生成包，底层依靠 ASM（开源的 java 字节码编辑类库）操作字节码实现的，性能比 JDK 强，所以 Spring AOP 最终使用了 CGlib 来生成动态代理。
- CGlib 动态代理使用限制：
  - 第一点，只能代理委托类中任意的非 final 的方法，另外它是通过继承自委托类来生成代理的，所以如果委托类是 final 的，就无法被代理了（final 类不能被继承）。
- **JDK 动态代理的拦截对象是通过反射的机制来调用被拦截方法的。CGlib 采用了FastClass 的机制来实现对被拦截方法的调用。FastClass 机制就是对一个类的方法建立索引，通过索引来直接调用相应的方法**
### Spring事务隔离级别
- DEFAULT （默认）：使用数据库默认的事务隔离级别。
- READ_UNCOMMITTED （读未提交）
- READ_COMMITTED （读已提交）
- REPEATABLE_READ （可重复读）
- SERIALIZABLE（串行化）
###  Spring 中七种事务传播行为
|     事务传播类型说明      |                             说明                             |
| :-----------------------: | :----------------------------------------------------------: |
|   PROPAGATION_REQUIRED    | 如果当前没有事务，就新建一个事务。如果已经存在一个事务中，加入到这个事务中。这是最常见的选择。 |
|   PROPAGATION_SUPPORTS    |     支持当前事务，如果当前没有事务，就以非事务方式执行。     |
|   PROPAGATION_MANDATORY   |         使用当前事务，如果当前没有事务，就抛出异常。         |
| PROPAGATION_REQUIRES_NEW  |         新建事务，如果当前存在事务，把当前事务挂起。         |
| PROPAGATION_NOT_SUPPORTED |   以非事务方式执行操作，如果当前存在事务，把当前事务挂起。   |
|     PROPAGATION_NEVER     |       以非事务方式执行，如果当前存在事务，则抛出异常。       |
|    PROPAGATION_NESTED     | 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。 |
### Spring 如何解决循环依赖
- 三级缓存
  - singletonObjects：一级，日常实际获取Bean的地方。
  - earlySingletonObjects：二级，已实例化，但还没进行属性注入，由三级缓存放进来。
  - singletonFactory：三级，value是一个对象工厂。
#### 解决过程
> 1. **A对象实例化之后，属性注入之前，其实会把A对象放入到三级缓存中。key是BeanName，value是ObjectFactory**
> 2. **等到A对象属性注入时，发现依赖B，又去实例化B时。B属性注入需要去获取A对象，这里就是从三级缓存中拿出ObjectFactory，从ObjectFactory中得到对应的Bean(就是对象A)**
> 3. **把三级缓存的A记录干掉，然后放到二级缓存中。显然，二级缓存的key是BeanName，value是Bean(这里的Bean还没做完属性注入的相关工作)。等到完全初始化之后，就会把二级缓存给remove掉，塞到一级缓存中，我们getBean的时候，实际上拿到的是一级缓存的。**
- 循环依赖的本质 Two Sum
- 先去缓存里找Bean，没有则实例化当前的Bean放到Map，如果有需要依赖当前Bean的，就能从Map取到。 
### SpringMVC执行流程
![SpringMVC执行流程](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcxMTE1MTQxNDIzODAz?x-oss-process=image/format,png) 
1. 用户发起请求到前端控制器（DispatcherServlet），该控制器会过滤出哪些请求可以访问Servlet、哪些不能访问。就是url-pattern的作用，并且会加载springmvc.xml配置文件。
2. 前端控制器会找到处理器映射器（HandlerMapping），通过HandlerMapping完成url到controller映射的组件，简单来说，就是将在springmvc.xml中配置的或者注解的url与对应的处理类找到并进行存储，用map<url,handler>这样的方式来存储。
3. HandlerMapping有了映射关系，并且找到url对应的处理器，HandlerMapping就会将其处理器（Handler）返回，在返回前，会加上很多拦截器。
4. DispatcherServlet拿到Handler后，找到HandlerAdapter（处理器适配器），通过它来访问处理器，并执行处理器。
5. 执行处理器。
6. 处理器会返回一个ModelAndView对象给HandlerAdapter。
7. 通过HandlerAdapter将ModelAndView对象返回给前端控制器(DispatcherServlet)。
8. 前端控制器请求视图解析器(ViewResolver)去进行视图解析，根据逻辑视图名解析成真正的视图(jsp)，其实就是将ModelAndView对象中存放视图的名称进行查找，找到对应的页面形成视图对象。
9. 返回视图对象到前端控制器。
10. 视图渲染，就是将ModelAndView对象中的数据放到request域中，用来让页面加载数据的。
11. 通过第8步，通过名称找到了对应的页面，通过第10步，request域中有了所需要的数据，那么就能够进行视图渲染了。最后将其返回即可。
### Mybatis原理
- sqlSessionFactoryBuilder加载mybatis核心配置文件生成sqlSessionFactory（单例）
- 工厂模式生成sqlSession执行sql以及控制事务
- Mybatis通过动态代理使Mapper（sql映射器）接口能运行起来即为接口生成代理对象将sql查询到结果映射成pojo
**sqlSessionFactory构建过程：** 
- 解析并读取配置中的xml创建Configuration对象 （单例）
- 使用Configuration类r去创建sqlSessionFactory（builder模式）
### SpringBoot 配置文件的加载顺序
1. 先去项目根目录找config文件夹下找配置文件件
2. 再去根目录下找配置文件
3. 去resources下找cofnig文件夹下找配置文件
4. 去resources下找配置文件
![官网加载顺序](https://img-blog.csdnimg.cn/20181123162045737.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Y2NDEzODU3MTI=,size_16,color_FFFFFF,t_70) 
![本地项目测试](https://img-blog.csdnimg.cn/20181123175928348.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Y2NDEzODU3MTI=,size_16,color_FFFFFF,t_70)
###  SpringBoot 自动装配原理
[SpringBoot自动装配原理](https://blog.csdn.net/qq_36986015/article/details/107488437) 
> SpringBoot项目无需各种配置文件，一个main方法，就能把项目启动起来。
- SpringBoot通过main方法启动SpringApplication类的静态方法run()来启动项目。
- 
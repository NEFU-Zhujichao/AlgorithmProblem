package concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("等裁判吹口哨...");
                    //这里停顿两秒更便于观察线程执行的先后顺序
                    Thread.sleep(2000);
                    System.out.println("裁判吹口哨->>>>>");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Runner runner1 = new Runner(barrier, "张三");
        Runner runner2 = new Runner(barrier, "李四");
        Runner runner3 = new Runner(barrier, "王五");
        Runner runner4 = new Runner(barrier, "赵六");

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(runner1);
        service.execute(runner2);
        service.execute(runner3);
        service.execute(runner4);
        service.shutdown();

    }

}


class Runner implements Runnable{

    private CyclicBarrier barrier;
    private String name;

    public Runner(CyclicBarrier barrier, String name) {
        this.barrier = barrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            //模拟准备耗时
            Thread.sleep(new Random().nextInt(5000));
            System.out.println(name + ":准备OK");
            barrier.await();
            System.out.println(name +": 开跑");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e){
            e.printStackTrace();
        }
    }
}

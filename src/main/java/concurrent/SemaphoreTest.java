package concurrent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    private static int count = 20;

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(count);

        //指定最多只能有五个线程同时执行
        Semaphore semaphore = new Semaphore(5);

        Random random = new Random();
        for (int i = 0; i < count; i++) {
            final int no = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //获得许可
                        semaphore.acquire();
                        System.out.println(no +":号车可通行");
                        //模拟车辆通行耗时
                        Thread.sleep(random.nextInt(5000));
                        //释放许可
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();

    }
}
package com.xjl;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
//        LockProvider lockProvider = LockProvider.getInstance();
//        lockProvider.lock(FiLocks.slock("lock"));
//        lockProvider.lock(FiLocks.slock("lock"),FiLocks.xlock("lock2"));

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(5);
        try {
            Future<String> future = executorService.submit(() -> {
                for (int i = 0; i < 5; i++) {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    latch.countDown();
                    System.out.println(Thread.currentThread().getId() + "print" + LocalDateTime.now().toString());
                }
                return "ok";
            });
            String print = future.get(1000, TimeUnit.MILLISECONDS);
            System.out.println("rs:"+print);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
//            Thread.sleep(2500l);
            System.out.println(Thread.currentThread().getId()+"start"+ LocalDateTime.now().toString());
            latch.await();
            System.out.println(Thread.currentThread().getId()+"await"+ LocalDateTime.now().toString());
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        System.out.println("Hello world!");
    }
}
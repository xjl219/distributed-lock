package com.xjl.locks;


import com.xjl.locks.utils.ILockBuilder;

public class Main {
    public static void main(String[] args) {

        try {
            LockExecutor lockProvider = PcLockExecutorFacory.newRedissionExecutor();
//            lockProvider.lock(IPcLockBuilder.shared("lock"));
//            lockProvider.lock(IPcLockBuilder.shared("lock"), IPcLockBuilder.exclusive("lock2"));
//            lockProvider.tryLock(Main::printHello,IPcLockBuilder.exclusive("lock2"));
            lockProvider.tryLock(Main::printHello, ILockBuilder.exclusive("lock2"), ILockBuilder.shared("lock2"), ILockBuilder.fair("lock2"));
//            String hello = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"),IPcLockBuilder.shared("lock2"),IPcLockBuilder.fair("lock2"));
//            String hello2 = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"));
            lockProvider.unlock();
            System.out.println("-----------");
            System.exit(1);
        } catch (Exception e) {
           e.printStackTrace();
            System.exit(1);
        }
    }

    static void printHello(){
//        do some thing
    }
    static String get(){
        return "Hello";
    }
}
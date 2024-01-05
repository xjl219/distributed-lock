package com.xjl.locks;


import com.xjl.locks.spi.LockRedissionExecutor;

public class PcLockExecutorFacory {
    public static LockExecutor newRedissionExecutor() {
        return new LockRedissionExecutor();
    }
    public static LockExecutor newRedissionExecutor(String serviceCode) {
        return new LockRedissionExecutor(serviceCode);
    }
}

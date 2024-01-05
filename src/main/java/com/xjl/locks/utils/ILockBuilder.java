package com.xjl.locks.utils;



import com.xjl.locks.common.ExclusiveLock;
import com.xjl.locks.common.SharedLock;
import com.xjl.locks.spi.FairLock;

import java.util.List;
import java.util.function.Supplier;

public interface ILockBuilder {
    /**
     * 排它锁
     * @param keys
     * @return
     */
    static ExclusiveLock exclusive(String ...keys){
        return new ExclusiveLock() {
            @Override
            public String[] get() {
                return keys;
            }
        };
    }
    /**
     * 排它锁
     * @param keys
     * @return
     */
    static  ExclusiveLock exclusive(List<String> keys){
        return new ExclusiveLock() {
            @Override
            public String[] get() {
                return keys.toArray(new  String[0]);
            }
        };
    }
    /**
     * 排它锁
     * @param keysSupplier
     * @return
     */
    static  ExclusiveLock exclusive(Supplier<String[]> keysSupplier){
        return new ExclusiveLock() {
            @Override
            public String[] get() {
                return keysSupplier.get();
            }
        };
    }

    /**
     * 共享锁
     * @param keys
     * @return
     */
    static SharedLock shared(List<String> keys){
        return new SharedLock() {
            @Override
            public String[] get() {
                return keys.toArray(new  String[0]);
            }
        };
    }
    /**
     * 共享锁
     * @param keys
     * @return
     */
    static  SharedLock shared(String ...keys){
        return new SharedLock() {
            @Override
            public String[] get() {
                return keys;
            }
        };
    }
    /**
     * 共享锁
     * @param keysSupplier
     * @return
     */
    static  SharedLock shared(Supplier<String[]> keysSupplier){
        return new SharedLock() {
            @Override
            public String[] get() {
                return keysSupplier.get();
            }
        };
    }
    /**
     * 公平锁
     * @param keys
     * @return
     */
    static FairLock fair(String ...keys){
        return new FairLock() {
            @Override
            public String[] get() {
                return keys;
            }
        };
    }
    /**
     * 公平锁
     * @param keys
     * @return
     */
    static FairLock fair(List<String> keys){
        return new FairLock() {
            @Override
            public String[] get() {
                return keys.toArray(new  String[0]);
            }
        };
    }
    /**
     * 公平锁
     * @param keysSupplier
     * @return
     */
    static FairLock fair(Supplier<String[]> keysSupplier){
        return new FairLock() {
            @Override
            public String[] get() {
                return keysSupplier.get();
            }
        };
    }
}

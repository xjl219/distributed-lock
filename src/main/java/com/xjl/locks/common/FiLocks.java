package com.xjl.locks.common;

import java.util.List;
import java.util.function.Supplier;

public interface FiLocks {
    static  ExclusiveLock  xlock(String ...keys){
        return new ExclusiveLock() {
            @Override
            public String[] get() {
                return keys;
            }
        };
    }
    static  ExclusiveLock  xlock(List<String> keys){
        return new ExclusiveLock() {
            @Override
            public String[] get() {
                return keys.toArray(new  String[0]);
            }
        };
    }
    static  ExclusiveLock  xlock(Supplier<String[]> keysSupplier){
        return new ExclusiveLock() {
            @Override
            public String[] get() {
                return keysSupplier.get();
            }
        };
    }
    static    SharedLock  slock(List<String> keys){
        return new SharedLock() {
            @Override
            public String[] get() {
                return keys.toArray(new  String[0]);
            }
        };
    }
    static  SharedLock  slock(String ...keys){
        return new SharedLock() {
            @Override
            public String[] get() {
                return keys;
            }
        };
    }
    static  SharedLock  slock(Supplier<String[]> keysSupplier){
        return new SharedLock() {
            @Override
            public String[] get() {
                return keysSupplier.get();
            }
        };
    }
}

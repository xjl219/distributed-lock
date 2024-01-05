package com.xjl.locks.spi;

import com.xjl.locks.LockExecutor;
import com.xjl.locks.common.ILock;
import com.xjl.locks.utils.LockClientFactory;
import com.xjl.locks.utils.LockContextManager;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

public class LockRedissionExecutor implements LockExecutor {

    RedissonClient client;
    String serviceCode;
    RedissonMultiLock multiLock;
    RedissonClient getClient(){
        return client;
    }
    public LockRedissionExecutor(){

         this( LockContextManager.getContext().getServiceCode());
    }
    public LockRedissionExecutor(String serviceCode){
        this.serviceCode =serviceCode;
        client= LockClientFactory.getRedissonClient(serviceCode);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean lock(ILock... locks) {
        genRedissonMultiLock(locks).lock();
        return Boolean.TRUE;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean tryLock(ILock... locks) {

        return genRedissonMultiLock(locks).tryLock();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit, ILock... locks) throws InterruptedException {
        return genRedissonMultiLock(locks).tryLock(waitTime,leaseTime,unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unlock() {

        Optional.ofNullable(multiLock).ifPresent(RedissonMultiLock::unlock);
    }
    /**
     * {@inheritDoc}
     */   /**
     * {@inheritDoc}
     */
    public boolean isLocked(){
        if (Objects.isNull(multiLock)){
            return Boolean.FALSE;
        }else {
            return multiLock.isLocked();
        }
    }
    private  Stream<RLock> genLock(ILock lock) {
        Function<String, RLock> function = realKey -> {
            RLock rs = null;

            switch (lock.getType()) {
                case EXCLUSIVE:
                    rs = this.getClient().getLock(realKey);
                    break;
                case SHARED:
                    rs = this.getClient().getReadWriteLock(realKey).readLock();
                    break;
                case FAIRLOCK:
                    rs = this.getClient().getFairLock(realKey);
                    break;
                default:
                    throw new UnsupportedOperationException(lock.getType().toString() + " is unsupported locktype!");
            }
            return rs;
        };

        return Arrays.stream(lock.getKeys(serviceCode)).map(function);
    }

    RedissonMultiLock genRedissonMultiLock(ILock...locks) {
        RLock[] lockList = Arrays.stream(locks).flatMap(this::genLock).toArray(RLock[]::new);
        this. multiLock = new RedissonMultiLock(lockList);
        return multiLock;
    }

}

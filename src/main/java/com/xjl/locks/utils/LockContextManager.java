package com.xjl.locks.utils;


public class LockContextManager {
    private static LockContext context = null;

    public LockContextManager() {
    }

    public static void setContext(LockContext lockPlugin) {
        context = lockPlugin;
    }

    public static LockContext getContext() {
        if (context == null) {
            context = new LockContext() {
            };
        }

        return context;
    }
}
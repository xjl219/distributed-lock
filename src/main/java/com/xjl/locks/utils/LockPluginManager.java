package com.xjl.locks.utils;


public class LockPluginManager {
    private static ILockPlugin plugin = null;

    public LockPluginManager() {
    }

    public static void setPlugin(ILockPlugin lockPlugin) {
        plugin = lockPlugin;
    }

    public static ILockPlugin getPlugin() {
        if (plugin == null) {
            plugin = new ILockPlugin() {
            };
        }

        return plugin;
    }
}

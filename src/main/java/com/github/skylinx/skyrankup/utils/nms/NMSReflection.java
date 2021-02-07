package com.github.skylinx.skyrankup.utils.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class NMSReflection {

    public static void sendPacket(Player player, Object packet) {
        Object handle = null;
        try {
            handle = player.getClass().getMethod("getHandle").invoke(player);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Object playerConnection = null;
        try {
            playerConnection = Objects.requireNonNull(handle).getClass()
                    .getField("playerConnection").get(handle);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            Objects.requireNonNull(playerConnection).getClass()
                    .getMethod("sendPacket", getNMSClass("Packet"))
                    .invoke(playerConnection, packet);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass()
                .getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getOBCClass(String name) {
        String version = Bukkit.getServer().getClass()
                .getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

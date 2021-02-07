package com.github.skylinx.skyrankup.utils.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ActionBar extends NMSReflection {

    private static final Class<?> PACKET_PLAY_OUT_CHAT = getNMSClass("PacketPlayOutChat");
    private static Constructor<?> constructor;

    static {
        try {
            constructor = Objects.requireNonNull(PACKET_PLAY_OUT_CHAT).getConstructor(
                    getNMSClass("IChatBaseComponent"), byte.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    private Object packet;

    public ActionBar(String text) {
        setMessage(text);
    }

    public void setMessage(String text) {
        Class<?> iChatBaseComponent = getNMSClass("IChatBaseComponent");
        Class<?> chatSerializer = Objects.requireNonNull(iChatBaseComponent).getClasses()[0];

        Method a = null;
        try {
            a = chatSerializer.getMethod("a", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object component = null;
        try {
            component = Objects.requireNonNull(a)
                    .invoke(chatSerializer, "{\"text\": \"" + text + "\"}");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            this.packet = constructor.newInstance(component, (byte) 2);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void send(Player player) {
        sendPacket(player, packet);
    }

    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

}

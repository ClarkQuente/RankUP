package com.github.skylinx.skyrankup.utils.nms;

import com.google.common.base.Preconditions;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.Objects;

@Getter
@Setter
public class Title extends NMSReflection {

    private JSONObject title, subtitle;
    private int fadeIn, fadeOut, stay;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = convert(title);
        this.subtitle = convert(subtitle);
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    @SuppressWarnings("unchecked")
    static JSONObject convert(String text) {
        JSONObject json = new JSONObject();
        json.put("text", text);

        return json;
    }

    public void send(Player player) {
        Preconditions.checkNotNull(player);

        try {
            Object entityPlayer = player.getClass()
                    .getMethod("getHandle").invoke(player);
            Object playerConnection = entityPlayer.getClass()
                    .getField("playerConnection").get(entityPlayer);

            Class<?> clsPacketPlayOutTitle = getNMSClass("PacketPlayOutTitle");
            Class<?> clsPacket = getNMSClass("Packet");
            Class<?> clsIChatBaseComponent = getNMSClass("IChatBaseComponent");
            Class<?> clsChatSerializer = getNMSClass("IChatBaseComponent$ChatSerializer");
            Class<?> clsEnumTitleAction = getNMSClass("PacketPlayOutTitle$EnumTitleAction");

            Object timesPacket = Objects.requireNonNull(clsPacketPlayOutTitle).getConstructor(
                    int.class, int.class, int.class).newInstance(fadeIn, stay, fadeOut);

            playerConnection.getClass().getMethod("sendPacket", clsPacket)
                    .invoke(playerConnection, timesPacket);

            if (title != null && !title.isEmpty()) {
                Object titleComponent = Objects.requireNonNull(clsChatSerializer).getMethod(
                        "a", String.class).invoke(null, title.toString());

                Object titlePacket = clsPacketPlayOutTitle.getConstructor(clsEnumTitleAction,
                        clsIChatBaseComponent).newInstance(Objects.requireNonNull(clsEnumTitleAction)
                        .getField("TITLE").get(null), titleComponent);

                playerConnection.getClass().getMethod("sendPacket", clsPacket)
                        .invoke(playerConnection, titlePacket);
            }

            if (subtitle != null && !subtitle.isEmpty()) {
                Object subtitleComponent = Objects.requireNonNull(clsChatSerializer).getMethod("a",
                        String.class).invoke(null, subtitle.toString());

                Object subtitlePacket = clsPacketPlayOutTitle.getConstructor(clsEnumTitleAction,
                        clsIChatBaseComponent).newInstance(Objects.requireNonNull(clsEnumTitleAction)
                        .getField("SUBTITLE").get(null), subtitleComponent);

                playerConnection.getClass().getMethod("sendPacket", clsPacket)
                        .invoke(playerConnection, subtitlePacket);
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

}

package com.github.skylinx.skyrankup.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {

    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(ItemStack item) {
        this.itemStack = item;
        this.itemMeta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int quantity) {
        this(new ItemStack(material, quantity));
    }

    public ItemBuilder(Material material, int quantity, int data) {
        this(new ItemStack(material, quantity, (short) data));
    }

    public ItemBuilder(String texture) {
        val skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (texture == null || texture.isEmpty()) {
            this.itemStack = skullItem;
            return;
        }

        if (!texture.startsWith("http://textures.minecraft.net/texture/"))
            texture = "http://textures.minecraft.net/texture/" + texture;

        val skullMeta = (SkullMeta) skullItem.getItemMeta();
        val profile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);

        profile.getProperties().put("textures", new Property(
                "textures", new String(Base64.encodeBase64(String.format(
                "{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes()))));

        Field field = null;
        try {
            field = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(field).setAccessible(true);

        try {
            field.set(skullMeta, profile);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        this.itemMeta = skullMeta;
        skullItem.setItemMeta(skullMeta);

        this.itemStack = skullItem;
    }

    public ItemBuilder setQuantity(int quantity) {
        this.itemStack.setAmount(quantity);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        val list = new ArrayList<String>();

        for (String l : lore)
            list.add(ChatColor.translateAlternateColorCodes('&', l));

        this.itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        val list = new ArrayList<String>();

        for (String l : lore)
            list.add(ChatColor.translateAlternateColorCodes('&', l));

        this.itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> list = this.itemMeta.getLore() == null
                || this.itemMeta.getLore().isEmpty()
                ? new ArrayList<>()
                : this.itemMeta.getLore();

        for (String l : lore)
            list.add(ChatColor.translateAlternateColorCodes('&', l));

        this.itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        List<String> list = this.itemMeta.getLore() == null
                || this.itemMeta.getLore().isEmpty()
                ? new ArrayList<>()
                : this.itemMeta.getLore();

        for (String l : lore)
            list.add(ChatColor.translateAlternateColorCodes('&', l));

        this.itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder setGlow(boolean b) {
        if (b) {
            this.itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        this.itemMeta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean value) {
        this.itemMeta.spigot().setUnbreakable(value);
        return this;
    }

    public ItemBuilder setOwner(String owner) {
        val skullMeta = (SkullMeta) this.itemMeta;
        skullMeta.setOwner(owner);

        this.itemMeta = skullMeta;
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

}

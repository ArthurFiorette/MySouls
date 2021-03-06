package com.github.hazork.mysouls.utils;

import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Nbts {

    private static final String name = "MySoulsIdV1";
    private static final String id = "id";
    private static final String value = "value";

    public static ItemStack saveValue(ItemStack source, String id, String value) {
	Map<String, String> map = Maps.newHashMap();
	map.put(Nbts.id, id);
	if (value != null) {
	    map.put(Nbts.value, value);
	}
	return saveValues(source, map);
    }

    public static String getIdValue(ItemStack item) {
	return getValue(item, Nbts.id);
    }

    public static String getValue(ItemStack item) {
	return getValue(item, Nbts.value);
    }

    private static String getValue(ItemStack item, String keyName) {
	NBTTagCompound nbt = getNbt(item);
	if (isNbtsItem(item)) {
	    return nbt.getString(getKey(keyName));
	}
	return null;
    }

    public static boolean isNbtsItem(ItemStack item) {
	return getNbt(item).getBoolean(Nbts.name);
    }

    private static ItemStack saveValues(ItemStack source, Map<String, String> values) {
	return setNbt(source, nbt -> {
	    nbt.setBoolean(Nbts.name, true);
	    values.forEach((k, v) -> nbt.setString(getKey(k), v));
	});
    }

    private static ItemStack setNbt(ItemStack source, Consumer<NBTTagCompound> nbt) {
	net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(source);
	nbt.accept(i.hasTag() ? i.getTag() : new NBTTagCompound());
	i.setTag(i.getTag());
	return CraftItemStack.asBukkitCopy(i);
    }

    private static NBTTagCompound getNbt(ItemStack item) {
	net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
	return nms.hasTag() ? nms.getTag() : new NBTTagCompound();
    }

    private static String getKey(String keyName) {
	return keyName.startsWith(name + ".") ? keyName : name + "." + keyName;
    }
}

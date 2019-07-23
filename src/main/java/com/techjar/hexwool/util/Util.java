package com.techjar.hexwool.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Util {
	public static boolean itemMatchesOre(ItemStack item, String ore) {
		for (ItemStack oreItem : OreDictionary.getOres(ore)) {
			if (OreDictionary.itemMatches(oreItem, item, false)) {
				return true;
			}
		}
		return false;
	}

	public static Object getItemOrBlockFromStack(ItemStack itemStack) {
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block != Blocks.AIR)
			return block;
		return itemStack.getItem();
	}

	public static ItemStack growItemStack(ItemStack stack, int amount) {
		ItemStack itemStack = stack.copy();
		itemStack.grow(amount);
		return itemStack;
	}

	public static ItemStack shrinkItemStack(ItemStack stack, int amount) {
		ItemStack itemStack = stack.copy();
		itemStack.shrink(amount);
		return itemStack;
	}
}

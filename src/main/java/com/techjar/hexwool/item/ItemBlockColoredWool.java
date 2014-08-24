package com.techjar.hexwool.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.techjar.hexwool.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockColoredWool extends ItemBlock {
	public ItemBlockColoredWool(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		if (itemStack.hasTagCompound()) {
			int color = itemStack.getTagCompound().getInteger("color");
			list.add(StatCollector.translateToLocal("hexwool.string.colorText") + ": #" + (color == -1 ? "EASTER" : Util.colorToHex(color)));
		}
	}
}

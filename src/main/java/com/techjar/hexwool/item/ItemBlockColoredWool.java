package com.techjar.hexwool.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.techjar.hexwool.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockColoredWool extends ItemBlock {
	public ItemBlockColoredWool(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			int color = stack.getTagCompound().getInteger("Color");
			tooltip.add(I18n.format("hexwool.string.colorText") + ": #" + (color == -1 ? "EASTER" : Util.colorToHex(color)));
		}
	}
}

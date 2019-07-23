package com.techjar.hexwool.client.render.colors;

import java.util.Random;

import com.techjar.hexwool.util.ColorHelper;
import com.techjar.hexwool.util.Util;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ItemColorHandler implements IItemColor {
	private Random random = new Random();

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		int color = stack.hasTagCompound() ? stack.getTagCompound().getInteger("Color") : 0xFFFFFF;
		if (color == -1)
			return ColorHelper.rgbToColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
		return color | (0xFF << 24);
	}
}

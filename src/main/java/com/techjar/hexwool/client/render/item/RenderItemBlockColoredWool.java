package com.techjar.hexwool.client.render.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderItemBlockColoredWool implements IItemRenderer {
	private Random random = new Random();

	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
		System.out.println("fuck");
		return type != ItemRenderType.FIRST_PERSON_MAP;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		int metadata = itemStack.getItemDamage();
		Block block = Block.getBlockFromItem(itemStack.getItem());

		int color = itemStack.hasTagCompound() ? itemStack.getTagCompound().getInteger("color") : 0xFFFFFF;
		if (color == -1) {
			GL11.glColor4f(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
		} else {
			float red = ((color >> 16) & 0xFF) / 255.0F;
			float green = ((color >> 8) & 0xFF) / 255.0F;
			float blue = (color & 0xFF) / 255.0F;
			GL11.glColor4f(red, green, blue, 1);
		}

		if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

		RenderBlocks renderer = (RenderBlocks)data[0];
		renderer.useInventoryTint = false;
		renderer.renderBlockAsItem(block, metadata, 1);
		renderer.useInventoryTint = true;
	}
}

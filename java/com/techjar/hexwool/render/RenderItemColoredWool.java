package com.techjar.hexwool.render;

import com.techjar.hexwool.HexWool;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderItemColoredWool implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        RenderBlocks renderer = (RenderBlocks)data[0];
        int color = itemStack.hasTagCompound() ? itemStack.getTagCompound().getInteger("color") : 0xFFFFFF;
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        
        int metadata = itemStack.getItemDamage();
        Block block = Block.blocksList[itemStack.itemID];

        GL11.glColor4f(red, green, blue, 1);
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        renderer.useInventoryTint = false;
        renderer.renderBlockAsItem(block, metadata, 1);
        renderer.useInventoryTint = true;
    }
}

package com.techjar.hexwool.client.render.block;

import org.lwjgl.opengl.GL11;

import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.util.Util;
import com.techjar.hexwool.util.Util.RGBColor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBlockColoredWool implements ISimpleBlockRenderingHandler {
	final int renderId;
	private static IIcon[] iconOverride = new IIcon[6];
	
	public RenderBlockColoredWool(int renderId) {
		this.renderId = renderId;
	}
	
	private static IIcon getIconSafe(RenderBlocks renderer, Block block, int metadata, int side) {
		if (side >= 0 && side <= 5) {
			if (iconOverride[side] != null)
				return renderer.getIconSafe(iconOverride[side]);
		}
		return renderer.getIconSafe(block == null ? null : block.getIcon(side, metadata));
	}
	
	public static void setOverride(IIcon icon, int side) {
		iconOverride[side] = icon;
	}
	
	public static void setOverridesFromBlock(Block block, int metadata) {
		if (block == null) return;
		for (int i = 0; i < 6; i++)
			iconOverride[i] = block.getIcon(i, metadata);
	}
	
	public static void clearOverrides() {
		for (int i = 0; i < 6; i++)
			iconOverride[i] = null;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, getIconSafe(renderer, block, metadata, 0));
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, getIconSafe(renderer, block, metadata, 1));
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, getIconSafe(renderer, block, metadata, 2));
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, getIconSafe(renderer, block, metadata, 3));
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, getIconSafe(renderer, block, metadata, 4));
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, getIconSafe(renderer, block, metadata, 5));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		
		Block tileBlock = Blocks.air;
		int metadata = 0;
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			TileEntityColoredWool tileEntity = (TileEntityColoredWool)tile;
			tileBlock = tileEntity.block == null ? Blocks.air : tileEntity.block;
			metadata = tileEntity.meta;
		}
		
		int l = block.colorMultiplier(world, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        return Minecraft.isAmbientOcclusionEnabled() && tileBlock.getLightValue() == 0 ? (renderer.partialRenderBounds ? renderer.renderStandardBlockWithAmbientOcclusionPartial(tileBlock, x, y, z, f, f1, f2) : renderer.renderStandardBlockWithAmbientOcclusion(tileBlock, x, y, z, f, f1, f2)) : renderer.renderStandardBlockWithColorMultiplier(tileBlock, x, y, z, f, f1, f2);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return this.renderId;
	}
}

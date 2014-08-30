package com.techjar.hexwool.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.techjar.hexwool.block.BlockWoolColorizer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBlockWoolColorizer implements ISimpleBlockRenderingHandler {
	final int renderId;

	public RenderBlockWoolColorizer(int renderId) {
		this.renderId = renderId;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		IIcon icon = ((BlockWoolColorizer)block).dishIcon;
		renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
		tessellator.addVertexWithUV(0.25D, 0.8125D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(8.0D));
		tessellator.addVertexWithUV(0.75D, 0.8125D, 0.75D, icon.getInterpolatedU(16.0D), icon.getInterpolatedV(8.0D));
		tessellator.addVertexWithUV(0.75D, 0.8125D, 0.25D, icon.getInterpolatedU(16.0D), icon.getInterpolatedV(0.0D));
		tessellator.addVertexWithUV(0.25D, 0.8125D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(0.0D));
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
		tessellator.addVertexWithUV(0.25D, 0.8125D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(3.0D));
		tessellator.addVertexWithUV(0.25D, 0.8125D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(3.0D));
		tessellator.addVertexWithUV(0.25D, 1.0D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(0.0D));
		tessellator.addVertexWithUV(0.25D, 1.0D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(0.0D));
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
		tessellator.addVertexWithUV(0.75D, 0.8125D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(6.0D));
		tessellator.addVertexWithUV(0.75D, 0.8125D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(6.0D));
		tessellator.addVertexWithUV(0.75D, 1.0D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(3.0D));
		tessellator.addVertexWithUV(0.75D, 1.0D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(3.0D));
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
		tessellator.addVertexWithUV(0.25D, 0.8125D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(9.0D));
		tessellator.addVertexWithUV(0.75D, 0.8125D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(9.0D));
		tessellator.addVertexWithUV(0.75D, 1.0D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(6.0D));
		tessellator.addVertexWithUV(0.25D, 1.0D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(6.0D));
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
		tessellator.addVertexWithUV(0.75D, 0.8125D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(12.0D));
		tessellator.addVertexWithUV(0.25D, 0.8125D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(12.0D));
		tessellator.addVertexWithUV(0.25D, 1.0D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(9.0D));
		tessellator.addVertexWithUV(0.75D, 1.0D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(9.0D));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		IIcon icon = ((BlockWoolColorizer)block).dishIcon;
		renderer.renderStandardBlock(block, x, y, z);

		Tessellator tessellator = Tessellator.instance;
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(x + 0.25D, y + 0.8125D, z + 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(8.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 0.8125D, z + 0.75D, icon.getInterpolatedU(16.0D), icon.getInterpolatedV(8.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 0.8125D, z + 0.25D, icon.getInterpolatedU(16.0D), icon.getInterpolatedV(0.0D));
		tessellator.addVertexWithUV(x + 0.25D, y + 0.8125D, z + 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(0.0D));
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(x + 0.25D, y + 0.8125D, z + 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(3.0D));
		tessellator.addVertexWithUV(x + 0.25D, y + 0.8125D, z + 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(3.0D));
		tessellator.addVertexWithUV(x + 0.25D, y + 1.0D, z + 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(0.0D));
		tessellator.addVertexWithUV(x + 0.25D, y + 1.0D, z + 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(0.0D));
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(x + 0.75D, y + 0.8125D, z + 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(6.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 0.8125D, z + 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(6.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 1.0D, z + 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(3.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 1.0D, z + 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(3.0D));
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addVertexWithUV(x + 0.25D, y + 0.8125D, z + 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(9.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 0.8125D, z + 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(9.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 1.0D, z + 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(6.0D));
		tessellator.addVertexWithUV(x + 0.25D, y + 1.0D, z + 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(6.0D));
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertexWithUV(x + 0.75D, y + 0.8125D, z + 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(12.0D));
		tessellator.addVertexWithUV(x + 0.25D, y + 0.8125D, z + 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(12.0D));
		tessellator.addVertexWithUV(x + 0.25D, y + 1.0D, z + 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(9.0D));
		tessellator.addVertexWithUV(x + 0.75D, y + 1.0D, z + 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(9.0D));

		return true;
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

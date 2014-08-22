package com.techjar.hexwool.render;

import org.lwjgl.opengl.GL11;

import com.techjar.hexwool.ClientProxy;
import com.techjar.hexwool.block.BlockWoolColorizer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class HexWoolBlockRenderer implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        if (block instanceof BlockWoolColorizer) {
            Icon icon = ((BlockWoolColorizer)block).dishIcon;
            renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.addVertexWithUV(0.25D, 0.8125D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(8.0D));
            tessellator.addVertexWithUV(0.75D, 0.8125D, 0.75D, icon.getInterpolatedU(16.0D), icon.getInterpolatedV(8.0D));
            tessellator.addVertexWithUV(0.75D, 0.8125D, 0.25D, icon.getInterpolatedU(16.0D), icon.getInterpolatedV(0.0D));
            tessellator.addVertexWithUV(0.25D, 0.8125D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(0.0D));
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            tessellator.addVertexWithUV(0.25D, 0.8125D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(3.0D));
            tessellator.addVertexWithUV(0.25D, 0.8125D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(3.0D));
            tessellator.addVertexWithUV(0.25D, 1.0D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(0.0D));
            tessellator.addVertexWithUV(0.25D, 1.0D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(0.0D));
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            tessellator.addVertexWithUV(0.75D, 0.8125D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(6.0D));
            tessellator.addVertexWithUV(0.75D, 0.8125D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(6.0D));
            tessellator.addVertexWithUV(0.75D, 1.0D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(3.0D));
            tessellator.addVertexWithUV(0.75D, 1.0D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(3.0D));
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            tessellator.addVertexWithUV(0.25D, 0.8125D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(9.0D));
            tessellator.addVertexWithUV(0.75D, 0.8125D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(9.0D));
            tessellator.addVertexWithUV(0.75D, 1.0D, 0.25D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(6.0D));
            tessellator.addVertexWithUV(0.25D, 1.0D, 0.25D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(6.0D));
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addVertexWithUV(0.75D, 0.8125D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(12.0D));
            tessellator.addVertexWithUV(0.25D, 0.8125D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(12.0D));
            tessellator.addVertexWithUV(0.25D, 1.0D, 0.75D, icon.getInterpolatedU(8.0D), icon.getInterpolatedV(9.0D));
            tessellator.addVertexWithUV(0.75D, 1.0D, 0.75D, icon.getInterpolatedU(0.0D), icon.getInterpolatedV(9.0D));
            tessellator.draw();
            GL11.glPopMatrix();
        }
        
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
        tessellator.draw();
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (block instanceof BlockWoolColorizer) {
            Icon icon = ((BlockWoolColorizer)block).dishIcon;
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
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.renderId;
    }
}

package com.techjar.hexwool.render;

import org.lwjgl.opengl.GL11;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.Util;
import com.techjar.hexwool.Util.RGBColor;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class RenderTileEntityWoolColorizer extends TileEntitySpecialRenderer {
    private RenderBlocks renderBlocks;
    private RenderItem renderItem;
    private Minecraft mc;
    
    public RenderTileEntityWoolColorizer() {
        mc = FMLClientHandler.instance().getClient();
        renderBlocks = new RenderBlocks();
        renderItem  = new RenderItem() {
            @Override
            public boolean shouldBob() { return false; }
            @Override
            public boolean shouldSpreadItems() { return false; }
        };
        renderItem.setRenderManager(RenderManager.instance);
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity instanceof TileEntityWoolColorizer) {
            renderBlocks.blockAccess = tileEntity.worldObj;
            TileEntityWoolColorizer tile = (TileEntityWoolColorizer)tileEntity;
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            int light = tile.worldObj.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord + 1, tile.zCoord, 0);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light % 65536, light / 65536);
            
            ItemStack itemStack = tile.getStackInSlot(0) != null ? tile.getStackInSlot(0) : tile.getStackInSlot(1);
            if (itemStack != null) {
                itemStack = itemStack.copy();
                itemStack.stackSize = 1;
                
                EntityItem entityItem = new EntityItem(tile.worldObj, tile.xCoord, tile.yCoord + 1, tile.zCoord);
                entityItem.setEntityItemStack(itemStack);
                entityItem.hoverStart = 0.0F;
                
                boolean isBlock = itemStack.itemID < Block.blocksList.length && Block.blocksList[itemStack.itemID] != null && Block.blocksList[itemStack.itemID].blockID != 0;
                /*if (itemStack.itemID < Block.blocksList.length && Block.blocksList[itemStack.itemID] != null && Block.blocksList[itemStack.itemID].blockID != 0) {
                    GL11.glTranslatef(0.5F, 1.0F, 0.5F);
                }*/
                GL11.glPushMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (isBlock) {
                    GL11.glTranslatef(0.5F, 1.0F, 0.5F);
                    GL11.glRotatef(270.F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(1.5F, 1.5F, 1.5F);
                } else {
                    GL11.glTranslatef(0.5F, 0.9375F, 0.5F);
                }
                renderItem.doRenderItem(entityItem, 0, 0, 0, 0, 0);
                GL11.glPopMatrix();
            }
            
            if (tile.colorCode.length() == 6) {
                try {
                    RGBColor color = Util.colorToRgb(Integer.parseInt(tile.colorCode, 16));
                    GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                    mc.renderEngine.bindTexture("/mods/hexwool/textures/blocks/wool_colorizer_dye.png");
                    Tessellator tessellator = Tessellator.instance;
                    tessellator.startDrawingQuads();
                    tessellator.addVertexWithUV(0.25D, 0.9375D, 0.75D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV(0.75D, 0.9375D, 0.75D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV(0.75D, 0.9375D, 0.25D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV(0.25D, 0.9375D, 0.25D, 0.0D, 0.0D);
                    tessellator.draw();
                } catch (NumberFormatException ex) {}
            }
            
            GL11.glPopMatrix();
        }
    }
}

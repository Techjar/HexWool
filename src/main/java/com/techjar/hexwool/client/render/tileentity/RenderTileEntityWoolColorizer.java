package com.techjar.hexwool.client.render.tileentity;

import java.util.Random;

import com.techjar.hexwool.util.ColorHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

public class RenderTileEntityWoolColorizer extends TileEntitySpecialRenderer<TileEntityWoolColorizer> {
	private Random random = new Random();
	private Minecraft mc;

	public RenderTileEntityWoolColorizer() {
		mc = FMLClientHandler.instance().getClient();
	}

	@Override
	public void render(TileEntityWoolColorizer tile, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		int light = tile.getWorld().getCombinedLight(tile.getPos().up(), 0);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light % 65536, light / 65536);

		if (tile.colorCode.length() == 6) {
			try {
				ColorHelper.RGBColor color;
				if (tile.colorCode.toLowerCase().equals("easter"))
					color = new ColorHelper.RGBColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
				else
					color = ColorHelper.colorToRgb(Integer.parseInt(tile.colorCode, 16));

				mc.renderEngine.bindTexture(new ResourceLocation("hexwool:textures/blocks/wool_colorizer_dye.png"));
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferBuilder = tessellator.getBuffer();
				bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
				bufferBuilder.pos(0.25f, 0.9375f, 0.75f).tex(0.0f, 1.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).normal(0.0f, 1.0f, 0.0f).endVertex();
				bufferBuilder.pos(0.75f, 0.9375f, 0.75f).tex(1.0f, 1.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).normal(0.0f, 1.0f, 0.0f).endVertex();
				bufferBuilder.pos(0.75f, 0.9375f, 0.25f).tex(1.0f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).normal(0.0f, 1.0f, 0.0f).endVertex();
				bufferBuilder.pos(0.25f, 0.9375f, 0.25f).tex(0.0f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).normal(0.0f, 1.0f, 0.0f).endVertex();
				tessellator.draw();
			} catch (NumberFormatException e) {
			}
		}

		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack itemStack = !itemHandler.getStackInSlot(0).isEmpty() ? itemHandler.getStackInSlot(0) : itemHandler.getStackInSlot(1);
		if (!itemStack.isEmpty()) {
			itemStack = itemStack.copy();
			itemStack.setCount(1);

			GlStateManager.pushMatrix();

			Block block = Block.getBlockFromItem(itemStack.getItem());
			boolean isBlock = block != Blocks.AIR;
			if (isBlock) {
				GlStateManager.translate(0.5f, 1.0f, 0.5f);
				GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
				GlStateManager.scale(0.75f, 0.75f, 0.75f);

				if (block.getRenderLayer() == BlockRenderLayer.TRANSLUCENT)
					GlStateManager.depthMask(false);

				if (block instanceof BlockPane) { // TODO: Better way to check for these silly transforms?
					GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
					GlStateManager.scale(0.5f, 0.5f, 0.5f);
				}
			} else {
				GlStateManager.translate(0.5f, 1.03125f, 0.5f);
				GlStateManager.scale(0.5f, 0.5f, 0.5f);
			}

			RenderItem renderItem = mc.getRenderItem();
			renderItem.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);

			GlStateManager.depthMask(true);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}
}

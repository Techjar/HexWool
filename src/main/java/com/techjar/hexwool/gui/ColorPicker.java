package com.techjar.hexwool.gui;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.util.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ColorPicker {
	public static final ResourceLocation TEXTURE = new ResourceLocation(HexWool.ID, "textures/gui/color_picker.png");

	private DynamicTexture sbTexture;
	private ResourceLocation sbTextureLocation;
	private DynamicTexture hTexture;
	private ResourceLocation hTextureLocation;

	private final Minecraft mc;
	private float lastHue = -1;
	private boolean cursorClicked;
	private boolean sliderClicked;

	public final int width = 101;
	public final int height = 115;
	public int x;
	public int y;
	public float hue;
	public float saturation;
	public float brightness;

	public ColorPicker() {
		mc = FMLClientHandler.instance().getClient();
	}

	private void init() {
		if (sbTexture == null) {
			sbTexture = new DynamicTexture(256, 256);
			sbTextureLocation = mc.getTextureManager().getDynamicTextureLocation("hexwool:sb_texture", sbTexture);
			hTexture = new DynamicTexture(256, 1);
			hTextureLocation = mc.getTextureManager().getDynamicTextureLocation("hexwool:h_texture", hTexture);

			int[] data = hTexture.getTextureData();
			for (int i = 0; i < 256; i++) {
				ColorHelper.RGBColor color = ColorHelper.hsbToRgb(i / 255f, 1, 1);
				data[i] = 0xFF000000 | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
			}
			hTexture.updateDynamicTexture();
		}
	}

	public void update() {
		init();
		if (hue != lastHue) {
			int[] data = sbTexture.getTextureData();
			for (int x = 0; x < 256; x++) {
				for (int y = 0; y < 256; y++) {
					int i = x + (y * 256);
					ColorHelper.RGBColor color = ColorHelper.hsbToRgb(hue, x / 255f, (255 - y) / 255f);
					data[i] = 0xFF000000 | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
				}
			}

			sbTexture.updateDynamicTexture();
			lastHue = hue;
		}
	}

	public void render(int mouseX, int mouseY, float partialTicks) {
		init();

		// Who's setting a stupid color??
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();

		float zLevel = 500;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		mc.renderEngine.bindTexture(TEXTURE);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		// Background
		bufferBuilder.pos(x, y + height, zLevel).tex(0, height / 256f).endVertex();
		bufferBuilder.pos(x + width, y + height, zLevel).tex(width / 256f, height / 256f).endVertex();
		bufferBuilder.pos(x + width, y, zLevel).tex(width / 256f, 0).endVertex();
		bufferBuilder.pos(x, y, zLevel).tex(0, 0).endVertex();

		// Cursor
		bufferBuilder.pos(x + 5 + (int)(84 * saturation + 0.5f), y + 5 + (int)(84 * (1 - brightness) + 0.5f) + 7, zLevel + 2).tex(101f / 256f, 7f / 256f).endVertex();
		bufferBuilder.pos(x + 5 + (int)(84 * saturation + 0.5f) + 7, y + 5 + (int)(84 * (1 - brightness) + 0.5f) + 7, zLevel + 2).tex(108f / 256f, 7f / 256f).endVertex();
		bufferBuilder.pos(x + 5 + (int)(84 * saturation + 0.5f) + 7, y + 5 + (int)(84 * (1 - brightness) + 0.5f), zLevel + 2).tex(108f / 256f, 0).endVertex();
		bufferBuilder.pos(x + 5 + (int)(84 * saturation + 0.5f), y + 5 + (int)(84 * (1 - brightness) + 0.5f), zLevel + 2).tex(101f / 256f, 0).endVertex();

		// Slider
		bufferBuilder.pos(x + 6 + (int)(84 * hue + 0.5f), y + 109, zLevel + 2).tex(101f / 256f, 19f / 256f).endVertex();
		bufferBuilder.pos(x + 6 + (int)(84 * hue + 0.5f) + 5, y + 109, zLevel + 2).tex(106f / 256f, 19f / 256f).endVertex();
		bufferBuilder.pos(x + 6 + (int)(84 * hue + 0.5f) + 5, y + 97, zLevel + 2).tex(106f / 256f, 7f / 256f).endVertex();
		bufferBuilder.pos(x + 6 + (int)(84 * hue + 0.5f), y + 97, zLevel + 2).tex(101f / 256f, 7f / 256f).endVertex();

		tessellator.draw();
		mc.renderEngine.bindTexture(hTextureLocation);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		// Hue background
		bufferBuilder.pos(x + 8, y + 107, zLevel + 1).tex(0, 1).endVertex();
		bufferBuilder.pos(x + 93, y + 107, zLevel + 1).tex(1, 1).endVertex();
		bufferBuilder.pos(x + 93, y + 99, zLevel + 1).tex(1, 0).endVertex();
		bufferBuilder.pos(x + 8, y + 99, zLevel + 1).tex(0, 0).endVertex();

		tessellator.draw();
		mc.renderEngine.bindTexture(sbTextureLocation);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		// Saturation/brightness background
		bufferBuilder.pos(x + 8, y + 93, zLevel + 1).tex(0, 1).endVertex();
		bufferBuilder.pos(x + 93, y + 93, zLevel + 1).tex(1, 1).endVertex();
		bufferBuilder.pos(x + 93, y + 8, zLevel + 1).tex(1, 0).endVertex();
		bufferBuilder.pos(x + 8, y + 8, zLevel + 1).tex(0, 0).endVertex();

		tessellator.draw();
		GlStateManager.enableLighting();
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
			if (mouseX >= x + 8 && mouseX < x + 93 && mouseY >= y + 8 && mouseY < y + 93)
				cursorClicked = true;
			else if (mouseX >= x + 8 && mouseX < x + 93 && mouseY >= y + 99 && mouseY < y + 107)
				sliderClicked = true;
			mouseClickMove(mouseX, mouseY, mouseButton, 0);
			return true;
		}
		return false;
	}

	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (clickedMouseButton == 0) {
			if (cursorClicked) {
				saturation = MathHelper.clamp((mouseX - x - 8) / 84f, 0, 1);
				brightness = MathHelper.clamp(1 - ((mouseY - y - 8) / 84f), 0, 1);
			} else if (sliderClicked) {
				hue = MathHelper.clamp((mouseX - x - 8) / 84f, 0, 1);
			}
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		cursorClicked = false;
		sliderClicked = false;
	}
}

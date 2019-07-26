package com.techjar.hexwool.gui;

import java.io.IOException;

import com.techjar.hexwool.Config;
import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.util.ColorHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.network.packet.PacketGuiAction;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiWoolColorizer extends GuiContainer {
	public static final ResourceLocation TEXTURE = new ResourceLocation(HexWool.ID, "textures/gui/wool_colorizer.png");
	public static final ResourceLocation COLOR_PICKER_ICON = new ResourceLocation(HexWool.ID, "textures/gui/color_picker_icon.png");

	private static ColorPicker colorPicker = new ColorPicker();
	private boolean showColorPicker;

	public GuiButton colorizeBtn;
	public GuiButton colorPickerBtn;
	public GuiTextField hexField;
	public TileEntityWoolColorizer tileEntity;

	public GuiWoolColorizer(InventoryPlayer inventoryPlayer, TileEntityWoolColorizer tile) {
		super(new ContainerWoolColorizer(inventoryPlayer, tile));
		this.tileEntity = tile;
		this.ySize = 153;
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		buttonList.add(this.colorizeBtn = new GuiButton(1, this.guiLeft + 85, this.guiTop + 42, 83, 20, I18n.format("hexwool.string.colorizeButton")));
		buttonList.add(this.colorPickerBtn = new GuiButton(3, this.guiLeft + 147, this.guiTop + 17, 20, 20, I18n.format("")) {
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
				super.drawButton(mc, mouseX, mouseY, partialTicks);
				mc.renderEngine.bindTexture(COLOR_PICKER_ICON);
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferBuilder = tessellator.getBuffer();
				bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				bufferBuilder.pos(this.x + 2, this.y + 18, this.zLevel).tex(0, 1).endVertex();
				bufferBuilder.pos(this.x + 18, this.y + 18, this.zLevel).tex(1, 1).endVertex();
				bufferBuilder.pos(this.x + 18, this.y + 2, this.zLevel).tex(1, 0).endVertex();
				bufferBuilder.pos(this.x + 2, this.y + 2, this.zLevel).tex(0, 0).endVertex();
				tessellator.draw();
			}
		});
		colorizeBtn.enabled = false;
		hexField = new GuiTextField(2, mc.fontRenderer, this.guiLeft + 85, this.guiTop + 17, 57, 20);
		hexField.setMaxStringLength(6);
		hexField.setText(tileEntity.colorCode);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		mc.fontRenderer.drawString(HexWoolBlocks.WOOL_COLORIZER.getLocalizedName(), 8, 6, 4210752);
		TileEntityWoolColorizer tile = this.tileEntity;
		if (tile.cyanDye > 0)
			drawRect(9, 43, 9 + (int)(14 * (tile.cyanDye / 1000.0F)), 45, ColorHelper.rgbaToColor(0, 255, 255, 255));
		if (tile.magentaDye > 0)
			drawRect(27, 43, 27 + (int)(14 * (tile.magentaDye / 1000.0F)), 45, ColorHelper.rgbaToColor(255, 0, 255, 255));
		if (tile.yellowDye > 0)
			drawRect(45, 43, 45 + (int)(14 * (tile.yellowDye / 1000.0F)), 45, ColorHelper.rgbaToColor(255, 255, 0, 255));
		if (tile.blackDye > 0)
			drawRect(63, 43, 63 + (int)(14 * (tile.blackDye / 1000.0F)), 45, ColorHelper.rgbaToColor(0, 0, 0, 255));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		int scale = this.tileEntity.getProgressScaled(16);
		this.drawTexturedModalRect(x + 35, y + 22, 176, 0, scale + 1, 13);
		hexField.drawTextBox();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		if (showColorPicker)
			colorPicker.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateScreen() {
		hexField.updateCursorCounter();
		validateColorization();
		if (showColorPicker) {
			colorPicker.x = (width - xSize) / 2 - colorPicker.width + 147 + colorPickerBtn.width;
			colorPicker.y = (height - ySize) / 2 + 37;
			colorPicker.update();
			ColorHelper.RGBColor color = ColorHelper.hsbToRgb(colorPicker.hue, colorPicker.saturation, colorPicker.brightness);
			String hex = ColorHelper.colorToHex(ColorHelper.rgbToColor(color));
			if (!hex.equalsIgnoreCase(hexField.getText())) {
				hexField.setText(hex);
				HexWool.packetPipeline.sendToServer(new PacketGuiAction(PacketGuiAction.SET_HEX_CODE, hexField.getText()));
			}
		}
	}

	@Override
	public void keyTyped(char par1, int par2) throws IOException {
		if (hexField.textboxKeyTyped(par1, par2)) {
			try {
				TileEntityWoolColorizer tile = this.tileEntity;
				tile.colorCode = hexField.getText();
				IBlockState blockState = tile.getWorld().getBlockState(tile.getPos());
				tile.getWorld().notifyBlockUpdate(tile.getPos(), blockState, blockState, 2);
				HexWool.packetPipeline.sendToServer(new PacketGuiAction(PacketGuiAction.SET_HEX_CODE, hexField.getText()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			validateColorization();
		} else {
			super.keyTyped(par1, par2);
		}
	}

	private void validateColorization() {
		if (this.tileEntity.colorizing) {
			this.colorizeBtn.enabled = false;
			return;
		}
		ItemStack itemStack = this.inventorySlots.getSlot(0).getStack();
		if (!itemStack.isEmpty() && this.hexField.getText().length() == 6) {
			TileEntityWoolColorizer tile = this.tileEntity;
			try {
				int color = -1;
				if (!this.hexField.getText().toLowerCase().equals("easter"))
					color = Integer.parseInt(this.hexField.getText(), 16);
				this.colorizeBtn.enabled = ColorHelper.canColorizeItem(itemStack, color) && (tile.hasRequiredDyes(color) || Config.creative);
			} catch (NumberFormatException ex) {
				this.colorizeBtn.enabled = false;
			}
		} else {
			this.colorizeBtn.enabled = false;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (showColorPicker && colorPicker.mouseClicked(mouseX, mouseY, mouseButton))
			return;
		super.mouseClicked(mouseX, mouseY, mouseButton);
		hexField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		if (showColorPicker)
			colorPicker.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (showColorPicker)
			colorPicker.mouseReleased(mouseX, mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 1:
				HexWool.packetPipeline.sendToServer(new PacketGuiAction(PacketGuiAction.COLORIZE_WOOL, ""));
				break;
			case 3:
				int color = 0xFFFFFF;
				if (hexField.getText().length() == 6) {
					try {
						color = Integer.parseInt(hexField.getText(), 16);
					} catch (NumberFormatException e) {
					}
				}

				float[] hsb = ColorHelper.rgbToHsb(ColorHelper.colorToRgb(color));
				colorPicker.hue = hsb[0];
				colorPicker.saturation = hsb[1];
				colorPicker.brightness = hsb[2];

				showColorPicker = !showColorPicker;
				break;
		}
	}
}

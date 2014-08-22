package com.techjar.hexwool.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.container.SlotColorizer;
import com.techjar.hexwool.network.packet.PacketGuiAction;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;
import com.techjar.hexwool.util.Util;

public class GuiWoolColorizer extends GuiContainer /* implements ICrafting */{
	public GuiButton colorizeBtn;
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
		buttonList.add(this.colorizeBtn = new GuiButton(1, this.guiLeft + 85, this.guiTop + 42, 83, 20, StatCollector.translateToLocal("hexwool.string.colorizeButton")));
		colorizeBtn.enabled = false;
		hexField = new GuiTextField(fontRendererObj, this.guiLeft + 85, this.guiTop + 17, 83, 20);
		hexField.setMaxStringLength(6);
		((SlotColorizer)this.inventorySlots.getSlot(0)).gui = this;
		updateState();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		// this.inventorySlots.removeCraftingFromCrafters(this);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		GL11.glDisable(GL11.GL_LIGHTING);
		fontRendererObj.drawString(StatCollector.translateToLocal(this.tileEntity.getInventoryName()), 8, 6, 4210752);
		TileEntityWoolColorizer tile = this.tileEntity;
		if (tile.cyanDye > 0)
			this.drawRect(9, 43, 9 + (int)(14 * (tile.cyanDye / 1000.0F)), 45, Util.rgbaToColor(0, 255, 255, 255));
		if (tile.magentaDye > 0)
			this.drawRect(27, 43, 27 + (int)(14 * (tile.magentaDye / 1000.0F)), 45, Util.rgbaToColor(255, 0, 255, 255));
		if (tile.yellowDye > 0)
			this.drawRect(45, 43, 45 + (int)(14 * (tile.yellowDye / 1000.0F)), 45, Util.rgbaToColor(255, 255, 0, 255));
		if (tile.blackDye > 0)
			this.drawRect(63, 43, 63 + (int)(14 * (tile.blackDye / 1000.0F)), 45, Util.rgbaToColor(0, 0, 0, 255));
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("hexwool", "textures/gui/wool_colorizer.png"));
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		int scale = this.tileEntity.getProgressScaled(16);
		this.drawTexturedModalRect(x + 35, y + 22, 176, 0, scale + 1, 13);
		hexField.drawTextBox();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	@Override
	public void updateScreen() {
		hexField.updateCursorCounter();
	}

	@Override
	public void keyTyped(char par1, int par2) {
		if (hexField.textboxKeyTyped(par1, par2)) {
			try {
				TileEntityWoolColorizer tile = this.tileEntity;
				tile.colorCode = hexField.getText();
				tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
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
		if (HexWool.creative) {
			this.colorizeBtn.enabled = true;
			return;
		}
		ItemStack itemStack = this.inventorySlots.getSlot(0).getStack();
		if (itemStack != null && this.hexField.getText().length() == 6) {
			TileEntityWoolColorizer tile = this.tileEntity;
			try {
				int color = -1;
				if (!this.hexField.getText().toLowerCase().equals("easter"))
					color = Integer.parseInt(this.hexField.getText(), 16);
				this.colorizeBtn.enabled = Util.canColorizeItem(itemStack, color) && tile.hasRequiredDyes(color);
			} catch (NumberFormatException ex) {
				this.colorizeBtn.enabled = false;
			}
		} else {
			this.colorizeBtn.enabled = false;
		}
	}

	public void updateState() {
		validateColorization();
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		hexField.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 1:
				HexWool.packetPipeline.sendToServer(new PacketGuiAction(PacketGuiAction.COLORIZE_WOOL, ""));
				break;
		}
	}
}
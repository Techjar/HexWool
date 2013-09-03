package com.techjar.hexwool.gui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.Util;
import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.container.SlotColorizer;
import com.techjar.hexwool.network.packet.PacketGuiAction;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiWoolColorizer extends GuiContainer /*implements ICrafting*/ {
    public GuiButton colorizeBtn;
    public GuiTextField hexField;

    public GuiWoolColorizer(InventoryPlayer inventoryPlayer, TileEntityWoolColorizer tile) {
        super(new ContainerWoolColorizer(inventoryPlayer, tile));
        this.ySize = 153;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        buttonList.add(this.colorizeBtn = new GuiButton(1, this.guiLeft + 65, this.guiTop + 42, 95, 20, "Colorize"));
        colorizeBtn.enabled = false;
        hexField = new GuiTextField(fontRenderer, this.guiLeft + 65, this.guiTop + 17, 95, 20);
        hexField.setMaxStringLength(6);
        ((SlotColorizer)this.inventorySlots.getSlot(0)).gui = this;
        //this.inventorySlots.removeCraftingFromCrafters(this);
        //this.inventorySlots.addCraftingToCrafters(this);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        //this.inventorySlots.removeCraftingFromCrafters(this);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        GL11.glDisable(GL11.GL_LIGHTING);
        fontRenderer.drawString("Wool Colorizer", 8, 6, 4210752);
        //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/hexwool/textures/gui/woolColorizer.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
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
            validateColorization();
        } else {
            super.keyTyped(par1, par2);
        }
    }
    
    private void validateColorization() {
        ItemStack itemStack = this.inventorySlots.getSlot(0).getStack();
        if (itemStack != null && (itemStack.itemID == Block.cloth.blockID || itemStack.itemID == HexWool.idColoredWool) && this.hexField.getText().length() == 6) {
            try {
                Integer.parseInt(this.hexField.getText(), 16);
                this.colorizeBtn.enabled = true;
            } catch (NumberFormatException ex) {
                this.colorizeBtn.enabled = false;
            }
        } else {
            this.colorizeBtn.enabled = false;
        }
    }
    
    public void updateState() {
        ItemStack itemStack = this.inventorySlots.getSlot(0).getStack();
        if (itemStack != null && (itemStack.itemID == Block.cloth.blockID || itemStack.itemID == HexWool.idColoredWool)) {
            if (itemStack.itemID == HexWool.idColoredWool && itemStack.hasTagCompound() && this.hexField.getText().trim().isEmpty()) {
                this.hexField.setText(Util.rgbToHex(itemStack.getTagCompound().getInteger("color")));
            }
        }
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
                try {
                    PacketDispatcher.sendPacketToServer(new PacketGuiAction(PacketGuiAction.COLORIZE_WOOL, hexField.getText()).getPacket());
                } catch (IOException ex) { ex.printStackTrace(); }
                break;
        }
    }
    
    /*@Override
    public void sendContainerAndContentsToPlayer(Container container, List list)
    {
        this.sendSlotContents(container, 0, container.getSlot(0).getStack());
    }

    @Override
    public void sendSlotContents(Container container, int par2, ItemStack itemStack)
    {
        if (par2 == 0)
        {
            if (itemStack != null && (itemStack.itemID == Block.cloth.blockID || itemStack.itemID == HexWool.idColoredWool)) {
                if (itemStack.itemID == HexWool.idColoredWool && itemStack.hasTagCompound() && this.hexField.getText().isEmpty()) {
                    this.hexField.setText(Util.rgbToHex(itemStack.getTagCompound().getInteger("color")));
                }
            }
            validateColorization();
        }
    }

    @Override
    public void sendProgressBarUpdate(Container container, int par2, int par3) {}*/
}

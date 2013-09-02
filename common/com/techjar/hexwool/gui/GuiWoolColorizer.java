package com.techjar.hexwool.gui;

import java.io.DataOutputStream;

import com.techjar.hexwool.Util;
import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiWoolColorizer extends GuiContainer {

    public GuiWoolColorizer(InventoryPlayer inventoryPlayer, TileEntityWoolColorizer tile) {
        super(new ContainerWoolColorizer(inventoryPlayer, tile));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(1, 10, 52, 20, 20, "Colorize"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Wool Colorizer", 8, 6, 4210752);
        // fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"),
        // 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/hexwool/textures/gui/woolColorizer.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    protected void actionPerformed(GuiButton button) {
        // id is the id you give your button
        switch (button.id) {
            case 1:
                // handled by server
                break;
        }
        DataOutputStream out = Util.startPacket(0);
        //out.writeInt(2);
        PacketDispatcher.sendPacketToServer(Util.finishPacket());
    }
}

package com.techjar.hexwool.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.techjar.hexwool.GuiHandler;
import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.Util;
import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.gui.GuiWoolColorizer;
import com.techjar.hexwool.network.HexWoolPacket;
import com.techjar.hexwool.network.PacketHandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketGuiAction extends HexWoolPacket {
    public static final int COLORIZE_WOOL = 1;
    public static final int SET_HEX_CODE = 2;
    
    public final int action;
    public final String message;
    
    public PacketGuiAction(int action, String message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bos);
        data.write(getPacketId());
        data.write(this.action = action);
        data.writeUTF(this.message = message);
        this.packet = new Packet250CustomPayload(HexWool.networkChannel, bos.toByteArray());
        data.close();
    }
    
    public PacketGuiAction(DataInputStream input) throws IOException {
        this.action = input.read();
        this.message = input.readUTF();
    }
    
    @Override
    public void processClient(INetworkManager network, Player player) {
        Minecraft client = FMLClientHandler.instance().getClient();
        switch (this.action) {
            case SET_HEX_CODE:
                if (client.currentScreen instanceof GuiWoolColorizer) {
                    ((GuiWoolColorizer)client.currentScreen).hexField.setText(this.message);
                }
                break;
        }
    }
    
    @Override
    public void processServer(INetworkManager network, Player player) {
        EntityPlayerMP pl = (EntityPlayerMP)player;
        switch (this.action) {
            case COLORIZE_WOOL:
                if (pl.openContainer instanceof ContainerWoolColorizer) {
                    if (this.message.length() != 6) break;
                    try {
                        int color = Integer.parseInt(this.message, 16);
                        ItemStack itemStack = pl.openContainer.getSlot(0).getStack();
                        if (itemStack != null && Util.itemMatchesOre(itemStack, "blockWool")) {
                            pl.openContainer.getSlot(0).putStack(null);
                            if (itemStack.itemID != HexWool.idColoredWool) itemStack.itemID = HexWool.idColoredWool;
                            itemStack.setItemDamage(0);
                            if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound("tag"));
                            itemStack.getTagCompound().setInteger("color", color);
                            pl.openContainer.getSlot(1).putStack(itemStack);
                            pl.openContainer.detectAndSendChanges();
                        }
                    } catch (NumberFormatException ex) {}
                }
                break;
            case SET_HEX_CODE:
                if (pl.openContainer instanceof ContainerWoolColorizer) {
                    
                }
                break;
        }
    }
}

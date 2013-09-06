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
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

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
    public static final int SET_DYE_AMOUNTS = 3;
    
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
        EntityPlayerSP pl = (EntityPlayerSP)player;
        Minecraft client = FMLClientHandler.instance().getClient();
        switch (this.action) {
            case SET_HEX_CODE:
                if (client.currentScreen instanceof GuiWoolColorizer) {
                    ((GuiWoolColorizer)client.currentScreen).hexField.setText(this.message);
                    ((GuiWoolColorizer)client.currentScreen).updateState();
                }
                break;
            case SET_DYE_AMOUNTS:
                if (client.currentScreen instanceof GuiWoolColorizer && pl.openContainer instanceof ContainerWoolColorizer) {
                    String[] split = message.split(";");
                    ContainerWoolColorizer container = (ContainerWoolColorizer)pl.openContainer;
                    container.tileEntity.cyanDye = Integer.parseInt(split[0]);
                    container.tileEntity.magentaDye = Integer.parseInt(split[1]);
                    container.tileEntity.yellowDye = Integer.parseInt(split[2]);
                    container.tileEntity.blackDye = Integer.parseInt(split[3]);
                    ((GuiWoolColorizer)client.currentScreen).updateState();
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
                    try {
                        TileEntityWoolColorizer tile = ((ContainerWoolColorizer)pl.openContainer).tileEntity;
                        if (tile.colorCode.length() != 6) break;
                        int color = Integer.parseInt(tile.colorCode, 16);
                        if (tile.hasRequiredDyes(color)) tile.colorizeWool(color);
                    } catch (NumberFormatException ex) {}
                }
                break;
            case SET_HEX_CODE:
                if (pl.openContainer instanceof ContainerWoolColorizer) {
                    ((ContainerWoolColorizer)pl.openContainer).tileEntity.colorCode = message;
                    ((ContainerWoolColorizer)pl.openContainer).lastEditor = player;
                    pl.openContainer.detectAndSendChanges();
                }
                break;
        }
    }
}

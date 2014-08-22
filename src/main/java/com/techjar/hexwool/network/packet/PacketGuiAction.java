package com.techjar.hexwool.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.gui.GuiWoolColorizer;
import com.techjar.hexwool.network.IPacket;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketGuiAction implements IPacket {
	public static final byte COLORIZE_WOOL = 1;
	public static final byte SET_HEX_CODE = 2;
	public static final byte SET_DYE_AMOUNTS = 3;

	public byte action;
	public String message;

	public PacketGuiAction() {
	}

	public PacketGuiAction(byte action, String message) {
		this.action = action;
		this.message = message;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeByte(this.action);
		ByteBufUtils.writeUTF8String(buffer, this.message);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		this.action = buffer.readByte();
		this.message = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void handleClient(EntityPlayer player) {
		EntityPlayerSP pl = (EntityPlayerSP)player;
		Minecraft client = FMLClientHandler.instance().getClient();
		switch (this.action) {
			case SET_HEX_CODE:
				if (client.currentScreen instanceof GuiWoolColorizer && pl.openContainer instanceof ContainerWoolColorizer) {
					TileEntityWoolColorizer tile = ((ContainerWoolColorizer)pl.openContainer).tileEntity;
					tile.colorCode = this.message;
					tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
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
	public void handleServer(EntityPlayer player) {
		EntityPlayerMP pl = (EntityPlayerMP)player;
		switch (this.action) {
			case COLORIZE_WOOL:
				if (pl.openContainer instanceof ContainerWoolColorizer) {
					try {
						TileEntityWoolColorizer tile = ((ContainerWoolColorizer)pl.openContainer).tileEntity;
						if (tile.colorCode.length() != 6)
							break;
						int color = -1;
						if (!tile.colorCode.toLowerCase().equals("easter"))
							color = Integer.parseInt(tile.colorCode, 16);
						if (tile.hasRequiredDyes(color))
							tile.colorizeWool(color);
					} catch (NumberFormatException ex) {
					}
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

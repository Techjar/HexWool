package com.techjar.hexwool.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public interface IPacket {
	void encodePacket(ChannelHandlerContext context, ByteBuf buffer);

	void decodePacket(ChannelHandlerContext context, ByteBuf buffer);

	void handleClient(EntityPlayer player);

	void handleServer(EntityPlayer player);
}

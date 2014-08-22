package com.techjar.hexwool.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayerMP;

import com.techjar.hexwool.network.packet.PacketGuiAction;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class HexWoolChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket> {
	private EnumMap<Side, FMLEmbeddedChannel> channels;

	private HexWoolChannelHandler() {
		this.addDiscriminator(0, PacketGuiAction.class);
	}

	public static HexWoolChannelHandler init() {
		HexWoolChannelHandler channelHandler = new HexWoolChannelHandler();
		channelHandler.channels = NetworkRegistry.INSTANCE.newChannel("HexWool", channelHandler, new HexWoolPacketHandler());
		return channelHandler;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
		msg.encodePacket(ctx, target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg) {
		msg.decodePacket(ctx, source);
	}

	public void sendToAll(IPacket message) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	public void sendToPlayer(IPacket message, EntityPlayerMP player) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	public void sendToAllAround(IPacket message, NetworkRegistry.TargetPoint point) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	public void sendToDimension(IPacket message, int dimensionId) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	public void sendToServer(IPacket message) {
		this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channels.get(Side.CLIENT).writeOutbound(message);
	}
}

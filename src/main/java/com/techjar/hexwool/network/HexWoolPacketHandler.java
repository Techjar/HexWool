package com.techjar.hexwool.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.util.LogHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Sharable
public class HexWoolPacketHandler extends SimpleChannelInboundHandler<IPacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception {
		INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
		EntityPlayer player = HexWool.proxy.getPlayerFromNetHandler(netHandler);

		switch (FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT:
				msg.handleClient(player);
				break;
			case SERVER:
				msg.handleServer(player);
				break;
			default:
				LogHelper.severe("Impossible scenario encountered! Effective side is neither server nor client!");
				break;
		}
	}
}

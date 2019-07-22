package com.techjar.hexwool.proxy;

import com.techjar.hexwool.block.HexWoolBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import com.techjar.hexwool.client.render.tileentity.RenderTileEntityWoolColorizer;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@SuppressWarnings("unused")
public class ProxyClient extends ProxyCommon {
	@Override
	public void registerRenderers() {
		this.registerItemRenderers();
		this.registerTileEntityRenderers();
	}

	@Override
	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).player;
		}
		return FMLClientHandler.instance().getClientPlayerEntity();
	}

	private void registerItemRenderers() {
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HexWoolBlocks.COLORED_WOOL), new RenderItemBlockColoredWool());
	}

	private void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoolColorizer.class, new RenderTileEntityWoolColorizer());
	}
}

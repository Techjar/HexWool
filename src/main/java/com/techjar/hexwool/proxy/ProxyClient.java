package com.techjar.hexwool.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.client.render.block.*;
import com.techjar.hexwool.client.render.item.*;
import com.techjar.hexwool.client.render.tileentity.*;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {
	private int renderIdWoolColorizer;
	private int renderIdColoredWool;

	@Override
	public void registerRenderers() {
		this.registerBlockRenderers();
		this.registerItemRenderers();
		this.registerTileEntityRenderers();
	}

	@Override
	public int getBlockRender(Block block) {
		if (block == HexWoolBlocks.woolColorizer)
			return renderIdWoolColorizer;
		if (block == HexWoolBlocks.coloredBlock)
			return renderIdColoredWool;
		return -1;
	}

	@Override
	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).playerEntity;
		}
		return FMLClientHandler.instance().getClientPlayerEntity();
	}

	private void registerBlockRenderers() {
		RenderingRegistry.registerBlockHandler(new RenderBlockWoolColorizer(renderIdWoolColorizer = RenderingRegistry.getNextAvailableRenderId()));
		RenderingRegistry.registerBlockHandler(new RenderBlockColoredWool(renderIdColoredWool = RenderingRegistry.getNextAvailableRenderId()));
	}

	private void registerItemRenderers() {
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HexWoolBlocks.coloredBlock), new RenderItemBlockColoredWool());
	}

	private void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoolColorizer.class, new RenderTileEntityWoolColorizer());
	}
}

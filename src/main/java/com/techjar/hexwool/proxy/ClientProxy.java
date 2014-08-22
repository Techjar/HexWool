package com.techjar.hexwool.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.client.render.block.RenderBlockWoolColorizer;
import com.techjar.hexwool.client.render.item.RenderItemBlockColoredWool;
import com.techjar.hexwool.client.render.tileentity.RenderTileEntityWoolColorizer;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	private int renderIdWoolColorizer;

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
		return -1;
	}

	private void registerBlockRenderers() {
		renderIdWoolColorizer = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new RenderBlockWoolColorizer(renderIdWoolColorizer));
	}

	private void registerItemRenderers() {
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HexWoolBlocks.coloredWool), new RenderItemBlockColoredWool());
	}

	private void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoolColorizer.class, new RenderTileEntityWoolColorizer());
	}

	@Override
	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).playerEntity;
		}
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
}

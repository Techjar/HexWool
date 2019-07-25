package com.techjar.hexwool.proxy;

import com.techjar.hexwool.HexWool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.tileentity.TileEntityRGBColored;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

public class ProxyCommon {
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRGBColored.class, new ResourceLocation(HexWool.ID, "rgb_colored"));
		GameRegistry.registerTileEntity(TileEntityWoolColorizer.class, new ResourceLocation(HexWool.ID, "wool_colorizer"));
	}
	
	public void registerOres() {
		OreDictionary.registerOre("wool", HexWoolBlocks.COLORED_WOOL);
		OreDictionary.registerOre("blockGlass", HexWoolBlocks.COLORED_GLASS);
		OreDictionary.registerOre("paneGlass", HexWoolBlocks.COLORED_GLASS_PANE);
	}

	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics!
	}
	
	public void fireInterModComms() {
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", "com.techjar.hexwool.tileentity.TileEntityRGBColored");
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", "com.techjar.hexwool.tileentity.TileEntityWoolColorizer");
		FMLInterModComms.sendMessage("waila", "register", "com.techjar.hexwool.integration.WailaDataProvider.callbackRegister");
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).player;
		}
		return null;
	}
}

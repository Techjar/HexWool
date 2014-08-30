package com.techjar.hexwool.proxy;

import java.lang.reflect.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.techjar.hexwool.block.BlockColoredBlock;
import com.techjar.hexwool.block.BlockWoolColorizer;
import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.item.ItemBlockColoredWool;
import com.techjar.hexwool.tileentity.TileEntityColoredBlock;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class ProxyCommon {
	public void registerBlocks() {
		GameRegistry.registerBlock(HexWoolBlocks.woolColorizer = new BlockWoolColorizer(), "wool_colorizer");
		GameRegistry.registerBlock(HexWoolBlocks.coloredBlock = new BlockColoredBlock(), ItemBlockColoredWool.class, "colored_wool");
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityColoredBlock.class, "HW_ColoredWool");
		GameRegistry.registerTileEntity(TileEntityWoolColorizer.class, "HW_WoolColorizer");
	}
	
	public void registerOres() {
		OreDictionary.registerOre("cloth", new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("cloth", new ItemStack(HexWoolBlocks.coloredBlock, 1, OreDictionary.WILDCARD_VALUE));
	}

	public void registerRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HexWoolBlocks.woolColorizer), "ibi", "www", "iwi", 'w', "cloth", 'i', new ItemStack(Items.iron_ingot), 'b', new ItemStack(Items.bowl)));
	}

	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics!
	}
	
	public void fireInterModComms() {
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", "com.techjar.hexwool.tileentity.TileEntityColoredWool");
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", "com.techjar.hexwool.tileentity.TileEntityWoolColorizer");
		FMLInterModComms.sendMessage("Waila", "register", "com.techjar.hexwool.integration.WailaDataProvider.callbackRegister");
	}

	public int getBlockRender(Block block) {
		return -1;
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).playerEntity;
		}
		return null;
	}
}

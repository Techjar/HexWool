package com.techjar.hexwool.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.techjar.hexwool.block.BlockColoredWool;
import com.techjar.hexwool.block.BlockWoolColorizer;
import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.item.ItemBlockColoredWool;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics!
	}

	public int getBlockRender(Block block) {
		return -1;
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(HexWoolBlocks.woolColorizer = new BlockWoolColorizer(), "hexwool:wool_colorizer");
		GameRegistry.registerBlock(HexWoolBlocks.coloredWool = new BlockColoredWool(), ItemBlockColoredWool.class, "hexwool:colored_wool");
		Blocks.fire.setFireInfo(HexWoolBlocks.coloredWool, 30, 60);
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityColoredWool.class, "HW_ColoredWool");
		GameRegistry.registerTileEntity(TileEntityWoolColorizer.class, "HW_WoolColorizer");
	}

	public void registerRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HexWoolBlocks.woolColorizer), "ibi", "www", "iwi", 'w', "cloth", 'i', new ItemStack(Items.iron_ingot), 'b', new ItemStack(Items.bowl)));
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).playerEntity;
		}
		return null;
	}
}

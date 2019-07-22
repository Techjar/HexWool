package com.techjar.hexwool;

import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.client.render.colors.BlockColorHandler;
import com.techjar.hexwool.client.render.colors.ItemColorHandler;
import com.techjar.hexwool.item.ItemBlockColoredWool;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public final class RegistryHandler {
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(HexWoolBlocks.WOOL_COLORIZER, HexWoolBlocks.COLORED_WOOL);
		Blocks.FIRE.setFireInfo(HexWoolBlocks.COLORED_WOOL, 30, 60);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(HexWoolBlocks.WOOL_COLORIZER).setRegistryName(HexWoolBlocks.WOOL_COLORIZER.getRegistryName()));
		event.getRegistry().register(new ItemBlockColoredWool(HexWoolBlocks.COLORED_WOOL).setRegistryName(HexWoolBlocks.COLORED_WOOL.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerBlockColors(ColorHandlerEvent.Block event) {
		BlockColors blockColors = event.getBlockColors();
		blockColors.registerBlockColorHandler(new BlockColorHandler(), HexWoolBlocks.COLORED_WOOL);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerBlockColors(ColorHandlerEvent.Item event) {
		ItemColors itemColors = event.getItemColors();
		itemColors.registerItemColorHandler(new ItemColorHandler(), HexWoolBlocks.COLORED_WOOL);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		registerItemBlockModel(HexWoolBlocks.WOOL_COLORIZER);
		registerItemBlockModel(HexWoolBlocks.COLORED_WOOL);
	}

	@SideOnly(Side.CLIENT)
	private void registerItemBlockModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), null));
	}
}

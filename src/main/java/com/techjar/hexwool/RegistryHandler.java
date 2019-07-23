package com.techjar.hexwool;

import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.client.render.colors.BlockColorHandler;
import com.techjar.hexwool.client.render.colors.ItemColorHandler;
import com.techjar.hexwool.item.ItemBlockRGBColored;
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
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("unused")
public final class RegistryHandler {
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
				HexWoolBlocks.WOOL_COLORIZER,
				HexWoolBlocks.COLORED_WOOL,
				HexWoolBlocks.COLORED_GLASS,
				HexWoolBlocks.COLORED_CONCRETE,
				HexWoolBlocks.COLORED_TERRACOTTA,
				HexWoolBlocks.COLORED_GLASS_PANE
		);
		Blocks.FIRE.setFireInfo(HexWoolBlocks.COLORED_WOOL, 30, 60);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(HexWoolBlocks.WOOL_COLORIZER).setRegistryName(HexWoolBlocks.WOOL_COLORIZER.getRegistryName()));
		registerColoredItemBlock(event.getRegistry(), HexWoolBlocks.COLORED_WOOL);
		registerColoredItemBlock(event.getRegistry(), HexWoolBlocks.COLORED_GLASS);
		registerColoredItemBlock(event.getRegistry(), HexWoolBlocks.COLORED_CONCRETE);
		registerColoredItemBlock(event.getRegistry(), HexWoolBlocks.COLORED_TERRACOTTA);
		registerColoredItemBlock(event.getRegistry(), HexWoolBlocks.COLORED_GLASS_PANE);
	}

	private void registerColoredItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlockRGBColored(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerBlockColors(ColorHandlerEvent.Block event) {
		BlockColors blockColors = event.getBlockColors();
		BlockColorHandler colorHandler = new BlockColorHandler();
		blockColors.registerBlockColorHandler(colorHandler, HexWoolBlocks.COLORED_WOOL);
		blockColors.registerBlockColorHandler(colorHandler, HexWoolBlocks.COLORED_GLASS);
		blockColors.registerBlockColorHandler(colorHandler, HexWoolBlocks.COLORED_CONCRETE);
		blockColors.registerBlockColorHandler(colorHandler, HexWoolBlocks.COLORED_TERRACOTTA);
		blockColors.registerBlockColorHandler(colorHandler, HexWoolBlocks.COLORED_GLASS_PANE);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerBlockColors(ColorHandlerEvent.Item event) {
		ItemColors itemColors = event.getItemColors();
		ItemColorHandler colorHandler = new ItemColorHandler();
		itemColors.registerItemColorHandler(colorHandler, HexWoolBlocks.COLORED_WOOL);
		itemColors.registerItemColorHandler(colorHandler, HexWoolBlocks.COLORED_GLASS);
		itemColors.registerItemColorHandler(colorHandler, HexWoolBlocks.COLORED_CONCRETE);
		itemColors.registerItemColorHandler(colorHandler, HexWoolBlocks.COLORED_TERRACOTTA);
		itemColors.registerItemColorHandler(colorHandler, HexWoolBlocks.COLORED_GLASS_PANE);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		registerItemBlockModel(HexWoolBlocks.WOOL_COLORIZER);
		registerItemBlockModel(HexWoolBlocks.COLORED_WOOL);
		registerItemBlockModel(HexWoolBlocks.COLORED_GLASS);
		registerItemBlockModel(HexWoolBlocks.COLORED_CONCRETE);
		registerItemBlockModel(HexWoolBlocks.COLORED_TERRACOTTA);
		registerItemBlockModel(HexWoolBlocks.COLORED_GLASS_PANE);
	}

	@SideOnly(Side.CLIENT)
	private void registerItemBlockModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), null));
	}
}

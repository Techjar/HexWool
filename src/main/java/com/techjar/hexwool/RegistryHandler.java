package com.techjar.hexwool;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
		event.getRegistry().register(HexWoolBlocks.WOOL_COLORIZER);
		event.getRegistry().registerAll(getColoredBlocks());
		Blocks.FIRE.setFireInfo(HexWoolBlocks.COLORED_WOOL, 30, 60);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(HexWoolBlocks.WOOL_COLORIZER).setRegistryName(HexWoolBlocks.WOOL_COLORIZER.getRegistryName()));
		for (Block block : getColoredBlocks())
			registerColoredItemBlock(event.getRegistry(), block);
	}

	private void registerColoredItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlockRGBColored(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerBlockColors(ColorHandlerEvent.Block event) {
		BlockColors blockColors = event.getBlockColors();
		BlockColorHandler colorHandler = new BlockColorHandler();
		for (Block block : getColoredBlocks())
			blockColors.registerBlockColorHandler(colorHandler, block);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerBlockColors(ColorHandlerEvent.Item event) {
		ItemColors itemColors = event.getItemColors();
		ItemColorHandler colorHandler = new ItemColorHandler();
		for (Block block : getColoredBlocks())
			itemColors.registerItemColorHandler(colorHandler, block);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		registerItemBlockModel(HexWoolBlocks.WOOL_COLORIZER);
		for (Block block : getColoredBlocks())
			registerItemBlockModel(block);
	}

	@SideOnly(Side.CLIENT)
	private void registerItemBlockModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), null));
	}

	private Block[] getColoredBlocks() {
		ArrayList<Block> list = new ArrayList<>();
		try {
			for (Field f : HexWoolBlocks.class.getFields()) {
				if (f.getName().startsWith("COLORED_"))
					list.add((Block)f.get(null));
			}
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}

		return list.toArray(new Block[0]);
	}
}

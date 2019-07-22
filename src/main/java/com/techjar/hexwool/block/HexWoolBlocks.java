package com.techjar.hexwool.block;

import com.techjar.hexwool.HexWool;
import net.minecraft.block.Block;

public class HexWoolBlocks {
	public static final Block WOOL_COLORIZER = new BlockWoolColorizer().setHardness(1.0F).setRegistryName(HexWool.ID, "wool_colorizer").setTranslationKey("hexwool.block.woolColorizer");
	public static final Block COLORED_WOOL = new BlockColoredWool().setHardness(0.8F).setRegistryName(HexWool.ID, "colored_wool").setTranslationKey("hexwool.block.coloredWool");
}

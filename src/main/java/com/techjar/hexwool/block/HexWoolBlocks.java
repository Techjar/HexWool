package com.techjar.hexwool.block;

import com.techjar.hexwool.HexWool;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class HexWoolBlocks {
	public static final Block WOOL_COLORIZER = new BlockWoolColorizer().setHardness(1.0F).setRegistryName(HexWool.ID, "wool_colorizer").setTranslationKey("hexwool.block.woolColorizer");
	public static final Block COLORED_WOOL = new BlockRGBColored(Material.CLOTH, SoundType.CLOTH).setHardness(0.8F).setRegistryName(HexWool.ID, "colored_wool").setTranslationKey("hexwool.block.coloredWool");
	public static final Block COLORED_GLASS = new BlockRGBColoredGlass(Material.GLASS, SoundType.GLASS).setHardness(0.3F).setRegistryName(HexWool.ID, "colored_glass").setTranslationKey("hexwool.block.coloredGlass");
	public static final Block COLORED_CONCRETE = new BlockRGBColored(Material.ROCK, SoundType.STONE).setHardness(1.8F).setRegistryName(HexWool.ID, "colored_concrete").setTranslationKey("hexwool.block.coloredConcrete");
	public static final Block COLORED_TERRACOTTA = new BlockRGBColored(Material.ROCK, SoundType.STONE).setHardness(1.25F).setResistance(7.0F).setRegistryName(HexWool.ID, "colored_terracotta").setTranslationKey("hexwool.block.coloredTerracotta");
	public static final Block COLORED_GLASS_PANE = new BlockRGBColoredGlassPane(Material.GLASS, SoundType.GLASS).setHardness(0.3F).setRegistryName(HexWool.ID, "colored_glass_pane").setTranslationKey("hexwool.block.coloredGlassPane");
}

package com.techjar.hexwool;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import com.techjar.hexwool.block.HexWoolBlocks;
import com.techjar.hexwool.gui.GuiHandler;
import com.techjar.hexwool.network.HexWoolChannelHandler;
import com.techjar.hexwool.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "HexWool", name = "HexWool", version = "@VERSION@", dependencies = "required-after:Forge@[10.13.0.1206,)", acceptableRemoteVersions = "@VERSION_MASK@")
// @NetworkMod(clientSideRequired = true, serverSideRequired = false,
// clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels =
// {"HexWool"}, packetHandler = PacketHandlerClient.class),
// serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels =
// {"HexWool"}, packetHandler = PacketHandlerServer.class))
public class HexWool {
	public static final String networkChannel = "HexWool";
	public static int dyePerWool;
	public static int dyePerItem;
	public static int colorizingTicks;
	public static boolean creative;

	@Instance("HexWool")
	public static HexWool instance;

	@SidedProxy(clientSide = "com.techjar.hexwool.proxy.ClientProxy", serverSide = "com.techjar.hexwool.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static HexWoolChannelHandler packetPipeline;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		dyePerWool = config.get(Configuration.CATEGORY_GENERAL, "dyePerWool", 25, "Millibuckets of dye used per wool. Default: 25").getInt(25);
		dyePerItem = config.get(Configuration.CATEGORY_GENERAL, "dyePerItem", 250, "Millibuckets of dye given per dye item. Default: 250").getInt(250);
		colorizingTicks = config.get(Configuration.CATEGORY_GENERAL, "colorizingTicks", 40, "Number of ticks it takes to colorize something. Default: 40").getInt(40);
		creative = config.get(Configuration.CATEGORY_GENERAL, "creative", false, "Enable this for no dye requirement and instant dyeing. Default: false").getBoolean(false);
		if (config.hasChanged())
			config.save();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		// Setup networking
		packetPipeline = HexWoolChannelHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		// IMCs
		FMLInterModComms.sendMessage("AppliedEnergistics", "movabletile", "com.techjar.hexwool.tileentity.TileEntityColoredWool");
		FMLInterModComms.sendMessage("AppliedEnergistics", "movabletile", "com.techjar.hexwool.tileentity.TileEntityWoolColorizer");
		FMLInterModComms.sendMessage("Waila", "register", "com.techjar.hexwool.integration.WailaDataProvider.callbackRegister");

		// Blocks and Tile Entities
		proxy.registerBlocks();
		proxy.registerTileEntities();

		// Ore Dictionary
		OreDictionary.registerOre("cloth", new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("cloth", new ItemStack(HexWoolBlocks.coloredWool, 1, OreDictionary.WILDCARD_VALUE));

		// Recipes
		proxy.registerRecipes();

		// Register renders (client only, duh!)
		proxy.registerRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
}

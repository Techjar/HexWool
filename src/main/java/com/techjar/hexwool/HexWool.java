package com.techjar.hexwool;

import com.techjar.hexwool.util.LogHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.techjar.hexwool.gui.GuiHandler;
import com.techjar.hexwool.network.HexWoolChannelHandler;
import com.techjar.hexwool.proxy.ProxyCommon;

@SuppressWarnings("unused")
@Mod(modid = HexWool.ID, name = HexWool.NAME, version = HexWool.VERSION, /*dependencies = "required-after:Forge@[14.23.5.2768,)",*/ acceptableRemoteVersions = "[@RAW_VERSION@,)")
public class HexWool {
	public static final String ID = "hexwool";
	public static final String NAME = "HexWool";
	public static final String VERSION = "@VERSION@";

	@Mod.Instance(ID)
	public static HexWool instance;

	@SidedProxy(clientSide = "com.techjar.hexwool.proxy.ProxyClient", serverSide = "com.techjar.hexwool.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static HexWoolChannelHandler packetPipeline;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.setLogger(event.getModLog());
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		Config.dyePerWool = config.get(Configuration.CATEGORY_GENERAL, "dyePerWool", 25, "Millibuckets of dye used per wool. Default: 25").getInt(25);
		Config.dyePerItem = config.get(Configuration.CATEGORY_GENERAL, "dyePerItem", 250, "Millibuckets of dye given per dye item. Default: 250").getInt(250);
		Config.colorizingTicks = config.get(Configuration.CATEGORY_GENERAL, "colorizingTicks", 40, "Number of ticks it takes to colorize something. Default: 40").getInt(40);
		Config.creative = config.get(Configuration.CATEGORY_GENERAL, "creative", false, "Enable this for no dye requirement and instant dyeing. Default: false").getBoolean(false);
		if (config.hasChanged())
			config.save();

		MinecraftForge.EVENT_BUS.register(new RegistryHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// Setup networking
		packetPipeline = HexWoolChannelHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		// Blocks and Tile Entities
		proxy.registerTileEntities();

		// Ore Dictionary and Recipes
		proxy.registerOres();

		// Renders (client only, duh!)
		proxy.registerRenderers();

		// IMCs
		proxy.fireInterModComms();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
}

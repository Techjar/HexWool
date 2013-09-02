package com.techjar.hexwool;

import com.techjar.hexwool.block.*;
import com.techjar.hexwool.item.*;
import com.techjar.hexwool.tileentity.*;
import com.techjar.hexwool.network.PacketHandlerClient;
import com.techjar.hexwool.network.PacketHandlerServer;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "hexwool", name = "HexWool", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec=@NetworkMod.SidedPacketHandler(channels={"HexWool"}, packetHandler=PacketHandlerClient.class), serverPacketHandlerSpec=@NetworkMod.SidedPacketHandler(channels={"HexWool"}, packetHandler=PacketHandlerServer.class))
public class HexWool {
    public static final String networkChannel = "HexWool";
    public static int idColoredWool;
    public static int idWoolColorizer;

    @Instance("HexWool")
    public static HexWool instance;

    @SidedProxy(clientSide = "com.techjar.hexwool.client.ClientProxy", serverSide = "com.techjar.hexwool.CommonProxy")
    public static CommonProxy proxy;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        /*
         * Configuration config = new
         * Configuration(event.getSuggestedConfigurationFile()); config.load();
         * idColoredWool = config.getBlock("coloredWool", 3540).getInt();
         * idWoolColorizer = config.getBlock("woolColorizer", 3541).getInt(); if
         * (config.hasChanged()) config.save();
         */
        idColoredWool = 3540;
        idWoolColorizer = 3541;
    }

    @Init
    public void load(FMLInitializationEvent event) {
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        GameRegistry.registerTileEntity(TileEntityColoredWool.class, "HW_ColoredWool");
        GameRegistry.registerTileEntity(TileEntityWoolColorizer.class, "HW_WoolColorizer");
        GameRegistry.registerBlock(new BlockColoredWool(idColoredWool), ItemBlockColoredWool.class, "hwColoredWool");
        GameRegistry.registerBlock(new BlockWoolColorizer(idWoolColorizer), "hwWoolColorizer");
        LanguageRegistry.addName(Block.blocksList[idColoredWool], "Colored Wool");
        LanguageRegistry.addName(Block.blocksList[idWoolColorizer], "Wool Colorizer");
        //LanguageRegistry.addName("item.hwColoredWool", "Colored Wool");
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        // Stub Method
    }
}

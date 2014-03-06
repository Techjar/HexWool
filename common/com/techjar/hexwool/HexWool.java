package com.techjar.hexwool;

import com.techjar.hexwool.block.*;
import com.techjar.hexwool.item.*;
import com.techjar.hexwool.tileentity.*;
import com.techjar.hexwool.network.PacketHandlerClient;
import com.techjar.hexwool.network.PacketHandlerServer;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "HexWool", name = "HexWool", version = "@VERSION@", dependencies = "required-after:Forge@[9.11.1.965,)")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = {"HexWool"}, packetHandler = PacketHandlerClient.class), serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = {"HexWool"}, packetHandler = PacketHandlerServer.class))
public class HexWool {
    public static final String networkChannel = "HexWool";
    public static int idColoredWool;
    public static int idWoolColorizer;
    public static int dyePerWool;
    public static int dyePerItem;
    public static int colorizingTicks;
    public static boolean creative;
    public static BlockColoredWool blockColoredWool;
    public static BlockWoolColorizer blockWoolColorizer;

    @Instance("HexWool")
    public static HexWool instance;

    @SidedProxy(clientSide = "com.techjar.hexwool.ClientProxy", serverSide = "com.techjar.hexwool.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
         Configuration config = new Configuration(event.getSuggestedConfigurationFile());
         config.load();
         idColoredWool = config.getBlock("coloredWool", 3540).getInt();
         idWoolColorizer = config.getBlock("woolColorizer", 3541).getInt();
         dyePerWool = config.get(Configuration.CATEGORY_GENERAL, "dyePerWool", 25, "Millibuckets of dye used per wool. Default: 25").getInt(25);
         dyePerItem = config.get(Configuration.CATEGORY_GENERAL, "dyePerItem", 250, "Millibuckets of dye given per dye item. Default: 250").getInt(250);
         colorizingTicks = config.get(Configuration.CATEGORY_GENERAL, "colorizingTicks", 40, "Number of ticks it takes to colorize something. Default: 40").getInt(40);
         creative = config.get(Configuration.CATEGORY_GENERAL, "creative", false, "Enable this for no dye requirement and instant dyeing. Default: false").getBoolean(false);
         if (config.hasChanged()) config.save();
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        
        // IMCs
        FMLInterModComms.sendMessage("AppliedEnergistics", "movabletile", "com.techjar.hexwool.tileentity.TileEntityColoredWool");
        FMLInterModComms.sendMessage("AppliedEnergistics", "movabletile", "com.techjar.hexwool.tileentity.TileEntityWoolColorizer");
        FMLInterModComms.sendMessage("Waila", "register", "com.techjar.hexwool.WailaDataProvider.callbackRegister");
        
        // Blocks and Tile Entities
        GameRegistry.registerTileEntity(TileEntityColoredWool.class, "HW_ColoredWool");
        GameRegistry.registerTileEntity(TileEntityWoolColorizer.class, "HW_WoolColorizer");
        GameRegistry.registerBlock(blockWoolColorizer = new BlockWoolColorizer(idWoolColorizer), "hwWoolColorizer");
        GameRegistry.registerBlock(blockColoredWool = new BlockColoredWool(idColoredWool), ItemBlockColoredWool.class, "hwColoredWool");
        Block.setBurnProperties(idColoredWool, 30, 60);
        
        // Ore Dictionary
        OreDictionary.registerOre("blockWool", new ItemStack(Block.cloth, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockWool", new ItemStack(blockColoredWool, 1, OreDictionary.WILDCARD_VALUE));
        
        // Recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockWoolColorizer), "iwi", "wdw", "iwi", 'w', "blockWool", 'i', new ItemStack(Item.ingotIron), 'd', new ItemStack(Item.dyePowder, 1, OreDictionary.WILDCARD_VALUE)));
        
        // Localization
        //LanguageRegistry.addName(Block.blocksList[idColoredWool], "Colored Wool");
        //LanguageRegistry.addName(Block.blocksList[idWoolColorizer], "Wool Colorizer");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Stub Method
    }
}

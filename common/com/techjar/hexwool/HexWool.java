package com.techjar.hexwool;

import com.techjar.hexwool.block.*;
import com.techjar.hexwool.item.*;
import com.techjar.hexwool.tileentity.*;
import com.techjar.hexwool.network.PacketHandlerClient;
import com.techjar.hexwool.network.PacketHandlerServer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
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

@Mod(modid = "HexWool", name = "HexWool", version = "@VERSION@", dependencies = "required-after:Forge@[7.8.1.738,)")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = {"HexWool"}, packetHandler = PacketHandlerClient.class), serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = {"HexWool"}, packetHandler = PacketHandlerServer.class))
public class HexWool {
    public static final String networkChannel = "HexWool";
    public static int idColoredWool;
    public static int idWoolColorizer;
    public static int dyePerWool;
    public static int dyePerItem;
    public static BlockColoredWool blockColoredWool;
    public static BlockWoolColorizer blockWoolColorizer;

    @Instance("HexWool")
    public static HexWool instance;

    @SidedProxy(clientSide = "com.techjar.hexwool.ClientProxy", serverSide = "com.techjar.hexwool.CommonProxy")
    public static CommonProxy proxy;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
         Configuration config = new Configuration(event.getSuggestedConfigurationFile());
         config.load();
         idColoredWool = config.getBlock("coloredWool", 3540).getInt();
         idWoolColorizer = config.getBlock("woolColorizer", 3541).getInt();
         dyePerWool = config.get(Configuration.CATEGORY_GENERAL, "dyePerWool", 25, "Millibuckets of dye used per wool. Default: 25").getInt();
         dyePerItem = config.get(Configuration.CATEGORY_GENERAL, "dyePerItem", 250, "Millibuckets of dye given per dye item. Default: 250").getInt();
         if (config.hasChanged()) config.save();
    }

    @Init
    public void load(FMLInitializationEvent event) {
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        GameRegistry.registerTileEntity(TileEntityColoredWool.class, "HW_ColoredWool");
        GameRegistry.registerTileEntity(TileEntityWoolColorizer.class, "HW_WoolColorizer");
        GameRegistry.registerBlock(blockColoredWool = new BlockColoredWool(idColoredWool), ItemBlockColoredWool.class, "hwColoredWool");
        Block.setBurnProperties(idColoredWool, 30, 60);
        GameRegistry.registerBlock(blockWoolColorizer = new BlockWoolColorizer(idWoolColorizer), "hwWoolColorizer");
        OreDictionary.registerOre("blockWool", new ItemStack(Block.cloth, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockWool", new ItemStack(blockColoredWool, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockWoolColorizer), "iwi", "wdw", "iwi", 'w', "blockWool", 'i', new ItemStack(Item.ingotIron), 'd', new ItemStack(Item.dyePowder, 1, OreDictionary.WILDCARD_VALUE)));
        //GameRegistry.addRecipe(new ItemStack(idWoolColorizer, 1, 0), "iwi", "wdw", "iwi", 'w', new ItemStack(idColoredWool, 1, OreDictionary.WILDCARD_VALUE), 'i', new ItemStack(Item.ingotIron), 'd', new ItemStack(Item.dyePowder, 1, OreDictionary.WILDCARD_VALUE));
        LanguageRegistry.addName(Block.blocksList[idColoredWool], "Colored Wool");
        LanguageRegistry.addName(Block.blocksList[idWoolColorizer], "Wool Colorizer");
        //LanguageRegistry.addName("item.hwColoredWool", "Colored Wool");
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        // Stub Method
    }
}

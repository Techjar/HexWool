package com.techjar.hexwool;

import net.minecraftforge.client.MinecraftForgeClient;

import com.techjar.hexwool.render.*;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    public static int renderId = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
    public void registerRenderers() {
        MinecraftForgeClient.registerItemRenderer(HexWool.idColoredWool, new RenderItemColoredWool());
        RenderingRegistry.registerBlockHandler(new HexWoolBlockRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoolColorizer.class, new RenderTileEntityWoolColorizer());
    }
}

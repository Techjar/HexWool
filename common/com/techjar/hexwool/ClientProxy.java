package com.techjar.hexwool;

import net.minecraftforge.client.MinecraftForgeClient;

import com.techjar.hexwool.render.RenderItemColoredWool;

public class ClientProxy extends CommonProxy {
    public void registerRenderers() {
        MinecraftForgeClient.registerItemRenderer(HexWool.idColoredWool, new RenderItemColoredWool());
    }
}

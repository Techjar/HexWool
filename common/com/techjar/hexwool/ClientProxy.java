package com.techjar.hexwool;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.MinecraftForgeClient;

import com.techjar.hexwool.render.RenderItemColoredWool;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        MinecraftForgeClient.registerItemRenderer(HexWool.idColoredWool, new RenderItemColoredWool());
    }
}

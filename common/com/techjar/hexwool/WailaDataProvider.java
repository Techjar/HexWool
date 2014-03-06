package com.techjar.hexwool;

import java.util.List;

import com.techjar.hexwool.block.BlockColoredWool;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaDataProvider implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if(accessor.getTileEntity() instanceof TileEntityColoredWool)
            currenttip.add(StatCollector.translateToLocal("hexwool.string.colorText") + ": #" + Util.colorToHex(((TileEntityColoredWool)accessor.getTileEntity()).color));
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.addConfig("HexWool", "hexwool.showwoolcolor", "Show Wool Color");
        registrar.registerBodyProvider(new WailaDataProvider(), BlockColoredWool.class);
    }
}

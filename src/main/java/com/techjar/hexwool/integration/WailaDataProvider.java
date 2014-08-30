package com.techjar.hexwool.integration;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.techjar.hexwool.block.BlockColoredBlock;
import com.techjar.hexwool.tileentity.TileEntityColoredBlock;
import com.techjar.hexwool.util.Util;

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
		if (accessor.getTileEntity() instanceof TileEntityColoredBlock) {
			int color = ((TileEntityColoredBlock)accessor.getTileEntity()).color;
			currenttip.add(StatCollector.translateToLocal("hexwool.string.colorText") + ": #" + (color == -1 ? "EASTER" : Util.colorToHex(color)));
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.addConfig("HexWool", "hexwool.showwoolcolor", "Show Wool Color");
		registrar.registerBodyProvider(new WailaDataProvider(), BlockColoredBlock.class);
	}
}

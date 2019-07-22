package com.techjar.hexwool.integration;

import java.util.List;

import com.techjar.hexwool.HexWool;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import com.techjar.hexwool.block.BlockColoredWool;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.util.Util;

@SuppressWarnings("unused")
public class WailaDataProvider implements IWailaDataProvider {
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (accessor.getTileEntity() instanceof TileEntityColoredWool && config.getConfig("hexwool.showWoolColor")) {
			int color = ((TileEntityColoredWool)accessor.getTileEntity()).color;
			currenttip.add(I18n.format("hexwool.string.colorText") + ": #" + (color == -1 ? "EASTER" : Util.colorToHex(color)));
		}
		return currenttip;
	}

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.addConfig(HexWool.NAME, "hexwool.showWoolColor", "Show Wool Color");
		registrar.registerBodyProvider(new WailaDataProvider(), BlockColoredWool.class);
	}
}

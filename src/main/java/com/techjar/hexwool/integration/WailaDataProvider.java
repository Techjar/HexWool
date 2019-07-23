package com.techjar.hexwool.integration;

import java.util.List;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.block.BlockRGBColoredGlassPane;
import com.techjar.hexwool.util.ColorHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import com.techjar.hexwool.block.BlockRGBColored;
import com.techjar.hexwool.tileentity.TileEntityRGBColored;

@SuppressWarnings("unused")
public class WailaDataProvider implements IWailaDataProvider {
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (accessor.getTileEntity() instanceof TileEntityRGBColored && config.getConfig("hexwool.showWoolColor")) {
			int color = ((TileEntityRGBColored)accessor.getTileEntity()).color;
			currenttip.add(I18n.format("hexwool.string.colorText") + ": #" + (color == -1 ? "EASTER" : ColorHelper.colorToHex(color)));
		}
		return currenttip;
	}

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.addConfig(HexWool.NAME, "hexwool.showWoolColor", "Show Wool Color");
		registrar.registerBodyProvider(new WailaDataProvider(), BlockRGBColored.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockRGBColoredGlassPane.class);
	}
}

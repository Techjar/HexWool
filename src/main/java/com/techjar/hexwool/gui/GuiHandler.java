package com.techjar.hexwool.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int WOOL_COLORIZER = 1;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			case WOOL_COLORIZER:
				TileEntity tile = world.getTileEntity(x, y, z);
				if (tile instanceof TileEntityWoolColorizer) {
					return new ContainerWoolColorizer(player.inventory, (TileEntityWoolColorizer)tile);
				}
				break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			case WOOL_COLORIZER:
				TileEntity tile = world.getTileEntity(x, y, z);
				if (tile instanceof TileEntityWoolColorizer) {
					return new GuiWoolColorizer(player.inventory, (TileEntityWoolColorizer)tile);
				}
				break;
		}
		return null;
	}
}

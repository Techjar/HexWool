package com.techjar.hexwool.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRGBColored extends TileEntity {
	public int color = 0xFFFFFF;
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("Color", color);
		return super.writeToNBT(tagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		color = tagCompound.getInteger("Color");
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", pos.getX());
		tag.setInteger("y", pos.getY());
		tag.setInteger("z", pos.getZ());
		tag.setInteger("c", color);
		return tag;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.color = tag.getInteger("c");
	}
}

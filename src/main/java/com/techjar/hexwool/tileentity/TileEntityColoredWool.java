package com.techjar.hexwool.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityColoredWool extends TileEntity {
	public int color = 0xFFFFFF;
	public Block block;
	public byte meta;

	@Override
	public boolean canUpdate() {
		return false;
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("c", color);
		tagCompound.setShort("b", (short)Block.getIdFromBlock(block));
		tagCompound.setByte("m", meta);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		color = tagCompound.hasKey("color") ? tagCompound.getInteger("color") : tagCompound.getInteger("c");
		if (tagCompound.hasKey("b")) {
			block = Block.getBlockById(tagCompound.getShort("b"));
			meta = tagCompound.getByte("m");
		} else {
			block = Blocks.wool;
			meta = 0;
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setInteger("c", color);
		tagCompound.setShort("b", (short)Block.getIdFromBlock(block));
		tagCompound.setByte("m", meta);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager network, S35PacketUpdateTileEntity packet) {
		NBTTagCompound nbt = packet.func_148857_g();
		this.color = nbt.getInteger("c");
		this.block = Block.getBlockById(nbt.getShort("b"));
		this.meta = nbt.getByte("m");
	}
}

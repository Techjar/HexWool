package com.techjar.hexwool.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityColoredWool extends TileEntity {
    public int color = 0xFFFFFF;
    
    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("color", color);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        color = tagCompound.getInteger("color");
    }
    
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
    }
    
    @Override
    public void onDataPacket(INetworkManager network, Packet132TileEntityData packet) {
        this.readFromNBT(packet.data);
    }
}

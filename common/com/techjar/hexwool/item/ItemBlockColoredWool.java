package com.techjar.hexwool.item;

import java.util.List;

import com.techjar.hexwool.Util;
import com.techjar.hexwool.block.BlockColoredWool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockColoredWool extends ItemBlock {
    public ItemBlockColoredWool(int par1) {
        super(par1);
        //this.setUnlocalizedName("hexwool.block.coloredWool");
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        if (itemStack.hasTagCompound()) {
            int color = itemStack.getTagCompound().getInteger("color");
            list.add("Color: #" + Util.rgbToHex(color));
        }
    }
    
    /*@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int par2)
    {
        //throw new RuntimeException("where are we");
        //System.out.println("YO DIGGA IN THE SHIZZLE I GOT RIZZLED");
        int ret = 16777215;
        if (itemStack.hasTagCompound()) {
            ret = itemStack.getTagCompound().getInteger("color");
        }
        BlockColoredWool.mostRecentColor = ret;
        return ret;
    }*/
}

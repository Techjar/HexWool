package com.techjar.hexwool.block;

import java.util.ArrayList;
import java.util.Random;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockColoredWool extends Block {
    //public static int mostRecentColor;
    
    public BlockColoredWool(int id) {
        super(id, Material.cloth);
        this.setHardness(0.8F);
        this.setStepSound(soundClothFootstep);
        //this.setCreativeTab(CreativeTabs.tabBlock);
        this.setUnlocalizedName("hexwool.block.coloredWool");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity tile = blockAccess.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEntityColoredWool) {
            return ((TileEntityColoredWool)tile).color;
        }
        return 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("cloth_0");
    }
    
    /*@SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return mostRecentColor;
    }*/
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        ItemStack itemStack = super.getPickBlock(target, world, x, y, z);
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEntityColoredWool) {
            itemStack.setTagCompound(new NBTTagCompound("tag"));
            itemStack.getTagCompound().setInteger("color", ((TileEntityColoredWool)tile).color);
        }
        return itemStack;
    }
    
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer entityPlayer) {
        if (!entityPlayer.capabilities.isCreativeMode) dropBlockAsItem(world, x, y, z, meta, 0);
    }
    
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        ItemStack itemStack = new ItemStack(this.blockID, 1, world.getBlockMetadata(x, y, z));
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEntityColoredWool) {
            itemStack.setTagCompound(new NBTTagCompound("tag"));
            itemStack.getTagCompound().setInteger("color", ((TileEntityColoredWool)tile).color);
            drops.add(itemStack);
        }
        return drops;
    }
    
    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityColoredWool();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile instanceof TileEntityColoredWool) {
                ((TileEntityColoredWool)tile).color = itemStack.getTagCompound().getInteger("color");
            }
        }
    }
}

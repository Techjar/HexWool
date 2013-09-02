package com.techjar.hexwool.block;

import java.util.ArrayList;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockWoolColorizer extends Block {
    public BlockWoolColorizer(int id) {
        super(id, Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setUnlocalizedName("hexwool.block.woolColorizer");
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("hexwool:woolColorizer");
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile instanceof TileEntityWoolColorizer && !player.isSneaking()) {
                player.openGui(HexWool.instance, 0, world, x, y, z);
            }
            return false;
    }
    
    public boolean hasTileEntity(int meta) {
        return true;
    }
    
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityWoolColorizer();
    }
    
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = super.getBlockDropped(world, x, y, z, meta, fortune);
        
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEntityWoolColorizer) {
            IInventory inventory = (IInventory)tile;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack != null && itemStack.stackSize > 0) {
                    drops.add(itemStack.copy());
                }
            }
        }
        return drops;
    }
}

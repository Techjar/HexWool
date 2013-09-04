package com.techjar.hexwool.block;

import java.util.ArrayList;
import java.util.Random;

import com.techjar.hexwool.GuiHandler;
import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class BlockWoolColorizer extends Block {
    private final Random random = new Random();
    
    public BlockWoolColorizer(int id) {
        super(id, Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setUnlocalizedName("hexwool.block.woolColorizer");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("hexwool:woolColorizer");
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile instanceof TileEntityWoolColorizer) {
                player.openGui(HexWool.instance, GuiHandler.WOOL_COLORIZER, world, x, y, z);
                return true;
            }
            return false;
    }
    
    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityWoolColorizer();
    }
    
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        TileEntity tileEntity = par1World.getBlockTileEntity(par2, par3, par4);
        if (tileEntity instanceof TileEntityWoolColorizer) {
            TileEntityWoolColorizer tile = (TileEntityWoolColorizer)tileEntity;
            for (int j1 = 0; j1 < tile.getSizeInventory(); ++j1) {
                ItemStack itemStack = tile.getStackInSlot(j1);

                if (itemStack != null) {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityItem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; par1World.spawnEntityInWorld(entityItem)) {
                        int k1 = this.random.nextInt(21) + 10;

                        if (k1 > itemStack.stackSize) {
                            k1 = itemStack.stackSize;
                        }

                        itemStack.stackSize -= k1;
                        entityItem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemStack.itemID, k1, itemStack.getItemDamage()));
                        float f3 = 0.05F;
                        entityItem.motionX = (double)((float)this.random.nextGaussian() * f3);
                        entityItem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityItem.motionZ = (double)((float)this.random.nextGaussian() * f3);

                        if (itemStack.hasTagCompound()) {
                            entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
                        }
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    /*@Override
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
    }*/
}

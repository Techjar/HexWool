package com.techjar.hexwool.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.techjar.hexwool.ClientProxy;
import com.techjar.hexwool.GuiHandler;
import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.network.packet.PacketGuiAction;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockWoolColorizer extends Block {
    private final Random random = new Random();
    protected Icon topIcon;
    protected Icon bottomIcon;
    public Icon dishIcon;
    
    public BlockWoolColorizer(int id) {
        super(id, Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("hexwool.block.woolColorizer");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.dishIcon = iconRegister.registerIcon("hexwool:wool_colorizer_dish");
        this.topIcon = iconRegister.registerIcon("hexwool:wool_colorizer_top");
        this.bottomIcon = iconRegister.registerIcon("hexwool:wool_colorizer_bottom");
        this.blockIcon = iconRegister.registerIcon("hexwool:wool_colorizer_side");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        if (side == 0) return this.bottomIcon;
        if (side == 1) return this.topIcon;
        return this.blockIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return ClientProxy.renderId;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile instanceof TileEntityWoolColorizer) {
                player.openGui(HexWool.instance, GuiHandler.WOOL_COLORIZER, world, x, y, z);
                try {
                    TileEntityWoolColorizer tileEntity = (TileEntityWoolColorizer)tile;
                    PacketDispatcher.sendPacketToPlayer(new PacketGuiAction(PacketGuiAction.SET_DYE_AMOUNTS, String.format("%s;%s;%s;%s", tileEntity.cyanDye, tileEntity.magentaDye, tileEntity.yellowDye, tileEntity.blackDye)).getPacket(), (Player)player);
                    PacketDispatcher.sendPacketToPlayer(new PacketGuiAction(PacketGuiAction.SET_HEX_CODE, tileEntity.colorCode).getPacket(), (Player)player);
                } catch (IOException ex) { ex.printStackTrace(); }
            }
        }
        return true;
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }
    
    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        return side != ForgeDirection.UP;
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
}

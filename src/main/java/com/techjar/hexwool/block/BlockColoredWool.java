package com.techjar.hexwool.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColoredWool extends Block {
	private Random random = new Random();

	public BlockColoredWool() {
		super(Material.cloth);
		this.setHardness(0.8F);
		this.setStepSound(soundTypeCloth);
		// this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("hexwool.block.coloredWool");
		this.setBlockTextureName("wool_colored");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		TileEntity tile = blockAccess.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			int color = ((TileEntityColoredWool)tile).color;
			if (color == -1)
				return Util.rgbToColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
			return color;
		}
		return 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(this.getTextureName() + "_" + ItemDye.field_150921_b[15]);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		ItemStack itemStack = super.getPickBlock(target, world, x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileEntityColoredWool) {
			if (!itemStack.hasTagCompound())
				itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("color", ((TileEntityColoredWool)tile).color);
		}
		return itemStack;
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer entityPlayer) {
		if (!entityPlayer.capabilities.isCreativeMode)
			dropBlockAsItem(world, x, y, z, meta, 0);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack itemStack = new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileEntityColoredWool) {
			itemStack.setTagCompound(new NBTTagCompound());
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
		if (itemStack.hasTagCompound()) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if (tile instanceof TileEntityColoredWool) {
				((TileEntityColoredWool)tile).color = itemStack.getTagCompound().getInteger("color");
			}
		}
	}
}

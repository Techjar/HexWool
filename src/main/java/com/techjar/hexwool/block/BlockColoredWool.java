package com.techjar.hexwool.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.techjar.hexwool.tileentity.TileEntityColoredWool;

public class BlockColoredWool extends Block {
	public BlockColoredWool() {
		super(Material.CLOTH);
		this.setSoundType(SoundType.CLOTH);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack itemStack = super.getPickBlock(state, target, world, pos, player);
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileEntityColoredWool) {
			if (!itemStack.hasTagCompound())
				itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("Color", ((TileEntityColoredWool)tile).color);
		}
		return itemStack;
	}

	/*@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode)
			dropBlockAsItem(worldIn, pos, state, 0);
	}*/

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack itemStack = new ItemStack(this, 1);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityColoredWool) {
			itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("Color", ((TileEntityColoredWool)tile).color);
			drops.add(itemStack);
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityColoredWool();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (stack.hasTagCompound()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileEntityColoredWool) {
				((TileEntityColoredWool)tile).color = stack.getTagCompound().getInteger("Color");
			}
		}
	}
}

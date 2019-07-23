package com.techjar.hexwool.block;

import javax.annotation.Nullable;

import com.techjar.hexwool.api.IColorizable;
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

import com.techjar.hexwool.tileentity.TileEntityRGBColored;

public class BlockRGBColored extends Block implements IColorizable {
	public BlockRGBColored(Material material, SoundType soundType) {
		super(material);
		this.setSoundType(soundType);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack itemStack = super.getPickBlock(state, target, world, pos, player);
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileEntityRGBColored) {
			if (!itemStack.hasTagCompound())
				itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("Color", ((TileEntityRGBColored)tile).color);
		}
		return itemStack;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack) {
		if (tile instanceof TileEntityRGBColored) {
			ItemStack itemStack = new ItemStack(this, 1);
			itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("Color", ((TileEntityRGBColored)tile).color);

			spawnAsEntity(worldIn, pos, itemStack);
		} else {
			super.harvestBlock(worldIn, player, pos, state, tile, stack);
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack itemStack = new ItemStack(this, 1);

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityRGBColored) {
			itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("Color", ((TileEntityRGBColored)tile).color);
		}

		drops.add(itemStack);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityRGBColored();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (stack.hasTagCompound()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileEntityRGBColored) {
				((TileEntityRGBColored)tile).color = stack.getTagCompound().getInteger("Color");
			}
		}
	}

	@Override
	public boolean hasColor(ItemStack itemStack) {
		if (itemStack.hasTagCompound())
			return itemStack.getTagCompound().hasKey("Color");
		return false;
	}

	@Override
	public int getColor(ItemStack itemStack) {
		if (itemStack.hasTagCompound())
			return itemStack.getTagCompound().getInteger("Color");
		return 0xFFFFFF;
	}

	@Override
	public boolean acceptsColor(ItemStack itemStack, int color) {
		return true;
	}

	@Override
	public ItemStack colorize(ItemStack itemStack, int color) {
		ItemStack stack = itemStack.copy();
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Color", color);
		return stack;
	}
}

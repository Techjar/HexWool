package com.techjar.hexwool.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.techjar.hexwool.tileentity.TileEntityRGBColored;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRGBColoredGlass extends BlockRGBColored {
	public BlockRGBColoredGlass(Material material, SoundType soundType) {
		super(material, soundType);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		BlockPos sidePos = pos.offset(side);
		Block block = blockAccess.getBlockState(sidePos).getBlock();

		if (block == this) {
			TileEntity tile = blockAccess.getTileEntity(pos);
			TileEntity sideTile = blockAccess.getTileEntity(sidePos);

			if (tile instanceof TileEntityRGBColored && sideTile instanceof TileEntityRGBColored) {
				return ((TileEntityRGBColored)tile).color != ((TileEntityRGBColored)sideTile).color;
			}
		}

		return true;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack) {
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
			super.harvestBlock(worldIn, player, pos, state, tile, stack);
	}
}

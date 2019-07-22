package com.techjar.hexwool.client.render.colors;

import java.util.Random;
import javax.annotation.Nullable;

import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColorHandler implements IBlockColor {
	private Random random = new Random();

	@Override
	public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
		if (worldIn != null && pos != null) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileEntityColoredWool) {
				int color = ((TileEntityColoredWool)tile).color;
				if (color == -1)
					return Util.rgbToColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
				return color | (0xFF << 24);
			}
		}
		return 0xFFFFFFFF;
	}
}

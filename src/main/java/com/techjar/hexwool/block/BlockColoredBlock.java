package com.techjar.hexwool.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.tileentity.TileEntityColoredWool;
import com.techjar.hexwool.util.Util;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColoredBlock extends Block {
	private Random random = new Random();

	public BlockColoredBlock() {
		super(Material.cloth);
		this.setHardness(0.8F);
		this.setStepSound(soundTypeCloth);
		// this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("hexwool.block.coloredBlock");
		//this.setBlockTextureName("wool_colored");
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

	/*@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("IGNORE_THIS_NOT_AN_ERROR");
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return HexWool.proxy.getBlockRender(this);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		ItemStack itemStack = super.getPickBlock(target, world, x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileEntityColoredWool) {
			TileEntityColoredWool tileEntity = (TileEntityColoredWool)tile;
			itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("color", tileEntity.color);
			itemStack.getTagCompound().setString("block", GameData.getBlockRegistry().getNameForObject(tileEntity.block));
			itemStack.getTagCompound().setByte("meta", tileEntity.meta);
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
			TileEntityColoredWool tileEntity = (TileEntityColoredWool)tile;
			itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setInteger("color", tileEntity.color);
			itemStack.getTagCompound().setString("block", GameData.getBlockRegistry().getNameForObject(tileEntity.block));
			itemStack.getTagCompound().setByte("meta", tileEntity.meta);
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
				TileEntityColoredWool tileEntity = (TileEntityColoredWool)tile;
				NBTTagCompound nbt = itemStack.getTagCompound();
				tileEntity.color = nbt.getInteger("color");
				if (nbt.hasKey("block")) {
					tileEntity.block = Block.getBlockFromName(nbt.getString("block"));
					tileEntity.meta = nbt.getByte("meta");
				} else {
					tileEntity.block = Blocks.wool;
					tileEntity.meta = 0;
				}
			}
		}
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.getBlockHardness(world, x, y, z);
			}
		}
		return this.blockHardness;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return Blocks.fire.getFlammability(block);
			}
		}
		return super.getFlammability(world, x, y, z, face);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return Blocks.fire.getEncouragement(block);
			}
		}
		return super.getFireSpreadSpeed(world, x, y, z, face);
	}
	
	@Override
	public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.isFireSource(world, x, y, z, side);
			}
		}
		return false;
	}
	
	@Override
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.getExplosionResistance(entity);
			}
		}
		return this.getExplosionResistance(entity);
	}
	
	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.isWood(world, x, y, z);
			}
		}
		return super.isWood(world, x, y, z);
	}
	
	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.canEntityDestroy(world, x, y, z, entity);
			}
		}
		return super.canEntityDestroy(world, x, y, z, entity);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				block.onEntityCollidedWithBlock(world, x, y, z, entity);
			}
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.canSustainPlant(world, x, y, z, direction, plantable);
			}
		}
		return super.canSustainPlant(world, x, y, z, direction, plantable);
	}

	@Override
	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.isBlockSolid(world, x, y, z, side);
			}
		}
		return super.isBlockSolid(world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityColoredWool) {
			Block block = ((TileEntityColoredWool)tile).block;
			if (block != null) {
				return block.shouldSideBeRendered(world, x, y, z, side);
			}
		}
		return super.shouldSideBeRendered(world, x, y, z, side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		
	}
}

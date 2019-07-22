package com.techjar.hexwool.tileentity;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.techjar.hexwool.util.ItemStackHandlerWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.techjar.hexwool.Config;
import com.techjar.hexwool.util.Util;
import com.techjar.hexwool.util.Util.CMYKColor;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityWoolColorizer extends TileEntity implements ITickable {
	Random random = new Random();
	public String colorCode = "";
	public int cyanDye;
	public int magentaDye;
	public int yellowDye;
	public int blackDye;
	public boolean colorizing;
	public int ticks;
	//private NonNullList<ItemStack> inv = NonNullList.withSize(7, ItemStack.EMPTY);
	private ItemStackHandler items = new ItemStackHandler(7) {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			if (slot == 0 || slot == 1)
				TileEntityWoolColorizer.this.markDirtyAndSync();
			else
				TileEntityWoolColorizer.this.markDirty();
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			switch (slot) {
				case 0:
					return Util.canColorizeItem(stack, 0);
				case 2:
					return Util.itemMatchesOre(stack, "dyeCyan");
				case 3:
					return Util.itemMatchesOre(stack, "dyeMagenta");
				case 4:
					return Util.itemMatchesOre(stack, "dyeYellow");
				case 5:
					return Util.itemMatchesOre(stack, "dyeBlack");
				default:
					return false;
			}
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (!isItemValid(slot, stack))
				return stack;
			return super.insertItem(slot, stack, simulate);
		}
	};

	public TileEntityWoolColorizer() {
	}

	public boolean colorizeWool(int color) {
		final ItemStack inputStack = items.getStackInSlot(0);
		final ItemStack outputStack = items.getStackInSlot(1);
		if (!Config.creative) {
			if (!inputStack.isEmpty() && hasRequiredDyes(color) && Util.canColorizeItem(inputStack, color)) {
				if (Util.getItemHasColor(inputStack) && Util.getItemColor(inputStack) == color) {
					if (outputStack.isEmpty()) {
						items.setStackInSlot(1, inputStack.copy());
						items.setStackInSlot(0, ItemStack.EMPTY);
						this.markDirtyAndSync();
						return true;
					} else {
						int maxStack = outputStack.getMaxStackSize();
						if (outputStack.getCount() < maxStack) {
							ItemStack resultStack = Util.colorizeItem(inputStack, color);

							if (resultStack.isItemEqual(outputStack) && ItemStack.areItemStackTagsEqual(resultStack, outputStack)) {
								int amountMade;
								if (resultStack.getCount() + outputStack.getCount() > maxStack) {
									amountMade = maxStack - outputStack.getCount();
								} else {
									amountMade = resultStack.getCount();
								}

								items.setStackInSlot(1, Util.growItemStack(outputStack, amountMade));
								items.setStackInSlot(0, Util.shrinkItemStack(inputStack, amountMade));
								this.markDirtyAndSync();
								return true;
							}
						}
					}
					return false;
				}

				int amountMade = 0;
				if (!outputStack.isEmpty()) {
					int maxStack = outputStack.getMaxStackSize();
					if (outputStack.getCount() < maxStack) {
						ItemStack resultStack = Util.colorizeItem(inputStack, color);

						if (Util.getItemHasColor(outputStack) && Util.getItemColor(outputStack) == color) {
							if (resultStack.getCount() + outputStack.getCount() > maxStack) {
								amountMade = maxStack - outputStack.getCount();
							} else {
								amountMade = resultStack.getCount();
							}
						}
					}
				} else {
					amountMade = inputStack.getCount();
				}

				if (amountMade > 0) {
					int[] dyes = getRequiredDyes(color);
					int cyan = dyes[0], magenta = dyes[1], yellow = dyes[2], black = dyes[3];
					for (int i = 0; i < amountMade; i++) {
						fillDyes();
						if (cyanDye < cyan || magentaDye < magenta || yellowDye < yellow || blackDye < black) {
							break;
						}

						cyanDye -= cyan;
						magentaDye -= magenta;
						yellowDye -= yellow;
						blackDye -= black;

						ItemStack hiddenStack = items.getStackInSlot(6);
						if (!hiddenStack.isEmpty())
							items.setStackInSlot(6, Util.growItemStack(hiddenStack, 1));
						else {
							ItemStack itemStack = Util.colorizeItem(inputStack, color);
							itemStack.setCount(1);
							items.setStackInSlot(6, itemStack);
						}
						items.setStackInSlot(0, Util.shrinkItemStack(items.getStackInSlot(0), 1));
					}
					this.colorizing = true;
					this.markDirtyAndSync();
					return true;
				}
			}
		} else {
			if (!inputStack.isEmpty() && Util.canColorizeItem(inputStack, color)) {
				if (outputStack.isEmpty()) {
					items.setStackInSlot(1, Util.colorizeItem(inputStack, color));
					items.setStackInSlot(0, ItemStack.EMPTY);
					this.markDirtyAndSync();
					return true;
				} else {
					int maxStack = outputStack.getMaxStackSize();
					if (outputStack.getCount() < maxStack) {
						ItemStack resultStack = Util.colorizeItem(inputStack, color);

						if (resultStack.isItemEqual(outputStack) && ItemStack.areItemStackTagsEqual(resultStack, outputStack)) {
							int amountMade;
							if (resultStack.getCount() + outputStack.getCount() > maxStack) {
								amountMade = maxStack - outputStack.getCount();
							} else {
								amountMade = resultStack.getCount();
							}

							items.setStackInSlot(1, Util.growItemStack(outputStack, amountMade));
							items.setStackInSlot(0, Util.shrinkItemStack(inputStack, amountMade));
							this.markDirtyAndSync();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void markDirtyAndSync() {
		this.markDirty();
		IBlockState blockState = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, blockState, blockState, 2);
	}

	public int[] getRequiredDyes(int color) {
		int[] arr = new int[4];
		if (color == -1) {
			for (int i = 0; i < 4; i++)
				arr[i] = (int)(random.nextFloat() * Config.dyePerWool);
			return arr;
		}
		CMYKColor cmyk = Util.colorToCmyk(color);
		arr[0] = (int)(cmyk.getCyan() * Config.dyePerWool);
		arr[1] = (int)(cmyk.getMagenta() * Config.dyePerWool);
		arr[2] = (int)(cmyk.getYellow() * Config.dyePerWool);
		arr[3] = (int)(cmyk.getBlack() * Config.dyePerWool);
		return arr;
	}

	public boolean hasRequiredDyes(int color) {
		int[] dyes = getRequiredDyes(color);
		if (cyanDye < dyes[0] || magentaDye < dyes[1] || yellowDye < dyes[2] || blackDye < dyes[3]) {
			return false;
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public int getProgressScaled(int scale) {
		return this.ticks * scale / Config.colorizingTicks;
	}

	/*@Override
	public int getSizeInventory() {
		return inv.size();
	}

	@Override
	public boolean isEmpty() {
		return inv.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv.get(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack itemStack = ItemStackHelper.getAndSplit(inv, slot, amount);
		if (!itemStack.isEmpty())
			this.markDirtyAndSync();
		return itemStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = inv.get(index);
		if (itemStack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			inv.set(index, ItemStack.EMPTY);
			return itemStack;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		inv.set(slot, itemStack);
		if (!itemStack.isEmpty() && itemStack.getCount() > getInventoryStackLimit()) {
			itemStack.setCount(getInventoryStackLimit());
		}
		this.markDirtyAndSync();
	}

	@Override
	public String getName() {
		return "tile.hexwool.block.woolColorizer.name";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		switch (slot) {
			case 0:
				return Util.canColorizeItem(itemStack, 0);
			case 1:
				return false;
			case 2:
				return Util.itemMatchesOre(itemStack, "dyeCyan");
			case 3:
				return Util.itemMatchesOre(itemStack, "dyeMagenta");
			case 4:
				return Util.itemMatchesOre(itemStack, "dyeYellow");
			case 5:
				return Util.itemMatchesOre(itemStack, "dyeBlack");
		}
		return false;
	}

	@Override
	public int getField(int id) {
		switch (id) {
			case 0:
				return this.ticks;
			case 1:
				return this.cyanDye;
			case 2:
				return this.magentaDye;
			case 3:
				return this.yellowDye;
			case 4:
				return this.blackDye;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
			case 0:
				this.ticks = value;
				break;
			case 1:
				this.cyanDye = value;
				break;
			case 2:
				this.magentaDye = value;
				break;
			case 3:
				this.yellowDye = value;
				break;
			case 4:
				this.blackDye = value;
				break;
		}
	}

	@Override
	public int getFieldCount() {
		return 5;
	}

	@Override
	public void clear() {
		inv.clear();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (world.getTileEntity(pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0, 1, 2, 3, 4, 5};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return slot == 0 || (slot >= 2 && slot <= 5);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		if (slot == 1) {
			return true;
		}
		return false;
	}*/

	public boolean canInteractWith(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(items);
			} else {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackHandlerWrapper(items) {
					@Nonnull
					@Override
					public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
						if (slot == 1 || slot == 6)
							return stack;
						return super.insertItem(slot, stack, simulate);
					}

					@Nonnull
					@Override
					public ItemStack extractItem(int slot, int amount, boolean simulate) {
						if (slot != 1)
							return ItemStack.EMPTY;
						return super.extractItem(slot, amount, simulate);
					}
				});
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER)
			return;

		fillDyes();
		if (!colorizing && world.isBlockPowered(pos)) {
			if (colorCode.length() == 6) {
				try {
					int color = Integer.parseInt(colorCode, 16);
					if (hasRequiredDyes(color))
						colorizeWool(color);
				} catch (NumberFormatException ex) {
				}
			}
		}
		if (colorizing) {
			if (this.ticks++ >= Config.colorizingTicks) {
				this.ticks = 0;
				this.colorizing = false;
				ItemStack hiddenStack = items.getStackInSlot(6);
				ItemStack outputStack = items.getStackInSlot(1);
				if (outputStack.isEmpty())
					items.setStackInSlot(1, hiddenStack.copy());
				else
					items.setStackInSlot(1, Util.growItemStack(outputStack, hiddenStack.getCount()));
				items.setStackInSlot(6, ItemStack.EMPTY);
				this.markDirtyAndSync();
			}
		}
	}

	private void fillDyes() {
		if (cyanDye <= 1000 - Config.dyePerItem && !items.getStackInSlot(2).isEmpty() && Util.itemMatchesOre(items.getStackInSlot(2), "dyeCyan")) {
			cyanDye += Config.dyePerItem;
			items.getStackInSlot(2).shrink(1);
		}
		if (magentaDye <= 1000 - Config.dyePerItem && !items.getStackInSlot(3).isEmpty() && Util.itemMatchesOre(items.getStackInSlot(3), "dyeMagenta")) {
			magentaDye += Config.dyePerItem;
			items.getStackInSlot(3).shrink(1);
		}
		if (yellowDye <= 1000 - Config.dyePerItem && !items.getStackInSlot(4).isEmpty() && Util.itemMatchesOre(items.getStackInSlot(4), "dyeYellow")) {
			yellowDye += Config.dyePerItem;
			items.getStackInSlot(4).shrink(1);
		}
		if (blackDye <= 1000 - Config.dyePerItem && !items.getStackInSlot(5).isEmpty() && Util.itemMatchesOre(items.getStackInSlot(5), "dyeBlack")) {
			blackDye += Config.dyePerItem;
			items.getStackInSlot(5).shrink(1);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		colorCode = tagCompound.getString("ColorCode");
		cyanDye = tagCompound.getInteger("CyanDye");
		magentaDye = tagCompound.getInteger("MagentaDye");
		yellowDye = tagCompound.getInteger("YellowDye");
		blackDye = tagCompound.getInteger("BlackDye");
		colorizing = tagCompound.getBoolean("Colorizing");
		if (tagCompound.hasKey("Inventory"))
			items.deserializeNBT(tagCompound.getCompoundTag("Inventory"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("ColorCode", colorCode);
		tagCompound.setInteger("CyanDye", cyanDye);
		tagCompound.setInteger("MagentaDye", magentaDye);
		tagCompound.setInteger("YellowDye", yellowDye);
		tagCompound.setInteger("BlackDye", blackDye);
		tagCompound.setBoolean("Colorizing", colorizing);
		tagCompound.setTag("Inventory", items.serializeNBT());
		return super.writeToNBT(tagCompound);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager netManager, SPacketUpdateTileEntity packet) {
		handleUpdateTag(packet.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tagCompound = super.getUpdateTag();
		tagCompound.setString("ColorCode", colorCode);
		tagCompound.setBoolean("Colorizing", colorizing);
		for (int i = 0; i < 2; i++) {
			ItemStack itemStack = items.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				NBTTagCompound tag = new NBTTagCompound();
				itemStack.writeToNBT(tag);
				tagCompound.setTag(i == 0 ? "Input" : "Output", tag);
			}
		}
		return tagCompound;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		colorCode = tag.getString("ColorCode");
		colorizing = tag.getBoolean("Colorizing");
		if (tag.hasKey("Input"))
			items.setStackInSlot(0, new ItemStack(tag.getCompoundTag("Input")));
		if (tag.hasKey("Output"))
			items.setStackInSlot(1, new ItemStack(tag.getCompoundTag("Output")));
	}
}

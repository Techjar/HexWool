package com.techjar.hexwool.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.util.Util;
import com.techjar.hexwool.util.Util.CMYKColor;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

@Optional.Interface(iface = "dan200.computer.api.IPeripheral", modid = "ComputerCraft")
public class TileEntityWoolColorizer extends TileEntity implements IInventory, ISidedInventory, IPeripheral {
	Random random = new Random();
	public String colorCode = "";
	public int cyanDye;
	public int magentaDye;
	public int yellowDye;
	public int blackDye;
	public boolean colorizing;
	public int ticks;
	public boolean dyesChanged;
	private ItemStack[] inv;
	private ItemStack hiddenStack;

	public TileEntityWoolColorizer() {
		inv = new ItemStack[6];
	}

	public boolean colorizeWool(int color) {
		ItemStack itemStack = inv[0];
		if (!HexWool.creative) {
			if (itemStack != null && hasRequiredDyes(color) && Util.canColorizeItem(itemStack, color)) {
				if (Util.getItemHasColor(itemStack) && Util.getItemColor(itemStack) == color) {
					if (inv[1] == null) {
						inv[1] = itemStack;
						inv[0] = null;
						this.markDirty();
						return true;
					} else {
						int maxStack = inv[1].getMaxStackSize();
						if (inv[1].stackSize < maxStack) {
							itemStack = Util.colorizeItem(itemStack, color);

							if (itemStack.isItemEqual(inv[1]) && ItemStack.areItemStackTagsEqual(itemStack, inv[1])) {
								int amountMade;
								if (itemStack.stackSize + inv[1].stackSize > maxStack) {
									amountMade = itemStack.stackSize - ((itemStack.stackSize + inv[1].stackSize) - maxStack);
								} else {
									amountMade = itemStack.stackSize;
								}

								inv[1].stackSize += amountMade;
								inv[0].stackSize -= amountMade;
								if (inv[0].stackSize < 1)
									inv[0] = null;
								this.markDirty();
								return true;
							}
						}
					}
				}
				int amountMade = 0;
				if (inv[1] != null) {
					int maxStack = inv[1].getMaxStackSize();
					if (inv[1].stackSize < maxStack) {
						itemStack = Util.colorizeItem(itemStack, color);

						if (itemStack.isItemEqual(inv[1]) && ItemStack.areItemStackTagsEqual(itemStack, inv[1])) {
							if (itemStack.stackSize + inv[1].stackSize > maxStack) {
								amountMade = itemStack.stackSize - ((itemStack.stackSize + inv[1].stackSize) - maxStack);
							} else {
								amountMade = itemStack.stackSize;
							}
						}
					}
				} else {
					amountMade = itemStack.stackSize;
				}

				if (amountMade > 0) {
					int[] dyes = getRequiredDyes(color);
					int cyan = dyes[0];
					int magenta = dyes[1];
					int yellow = dyes[2];
					int black = dyes[3];
					outer: for (int i = 0; i < amountMade; i++) {
						for (int j = 0; j < 2; j++) {
							if (cyanDye < cyan || magentaDye < magenta || yellowDye < yellow || blackDye < black) {
								if (j == 1)
									break outer;
								if (checkDyes())
									break;
							}
						}

						cyanDye -= cyan;
						magentaDye -= magenta;
						yellowDye -= yellow;
						blackDye -= black;
						dyesChanged = true;

						inv[0].stackSize--;
						if (hiddenStack != null)
							hiddenStack.stackSize++;
						else {
							hiddenStack = Util.colorizeItem(inv[0], color);
							hiddenStack.stackSize = 1;
						}
					}
					if (inv[0].stackSize < 1)
						inv[0] = null;
					this.colorizing = true;
					this.markDirty();
					return true;
				}
			}
		} else {
			if (itemStack != null && Util.canColorizeItem(itemStack, color)) {
				if (inv[1] == null) {
					inv[1] = Util.colorizeItem(itemStack, color);
					inv[0] = null;
					this.markDirty();
					return true;
				} else {
					int maxStack = inv[1].getMaxStackSize();
					if (inv[1].stackSize < maxStack) {
						itemStack = Util.colorizeItem(itemStack, color);

						if (itemStack.isItemEqual(inv[1]) && ItemStack.areItemStackTagsEqual(itemStack, inv[1])) {
							int amountMade;
							if (itemStack.stackSize + inv[1].stackSize > maxStack) {
								amountMade = itemStack.stackSize - ((itemStack.stackSize + inv[1].stackSize) - maxStack);
							} else {
								amountMade = itemStack.stackSize;
							}

							inv[1].stackSize += amountMade;
							inv[0].stackSize -= amountMade;
							if (inv[0].stackSize < 1)
								inv[0] = null;
							this.markDirty();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int[] getRequiredDyes(int color) {
		if (color == -1) {
			int[] arr = new int[4];
			for (int i = 0; i < 4; i++)
				arr[i] = (int)(random.nextFloat() * HexWool.dyePerWool);
			return arr;
		}
		int[] arr = new int[4];
		CMYKColor cmyk = Util.colorToCmyk(color);
		arr[0] = (int)(cmyk.getCyan() * HexWool.dyePerWool);
		arr[1] = (int)(cmyk.getMagenta() * HexWool.dyePerWool);
		arr[2] = (int)(cmyk.getYellow() * HexWool.dyePerWool);
		arr[3] = (int)(cmyk.getBlack() * HexWool.dyePerWool);
		return arr;
	}

	public boolean hasRequiredDyes(int color) {
		if (color == -1)
			return true;
		int[] dyes = getRequiredDyes(color);
		if (cyanDye < dyes[0] || magentaDye < dyes[1] || yellowDye < dyes[2] || blackDye < dyes[3]) {
			return false;
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public int getProgressScaled(int scale) {
		return this.ticks * scale / HexWool.colorizingTicks;
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amount) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		inv[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "tile.hexwool.block.woolColorizer.name";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
		// Useless
	}

	@Override
	public void closeInventory() {
		// Useless
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
	public void markDirty() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		// super.markDirty();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0, 1, 2, 3, 4, 5};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		if (slot == 0 || (slot >= 2 && slot <= 5)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		if (slot == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canUpdate() {
		return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		checkDyes();
		if (!colorizing && worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
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
			this.ticks++;
			if (this.ticks >= HexWool.colorizingTicks) {
				this.ticks = 0;
				this.colorizing = false;
				if (inv[1] == null)
					inv[1] = hiddenStack;
				else
					inv[1].stackSize += hiddenStack.stackSize;
				hiddenStack = null;
				this.markDirty();
			}
		}
	}

	public boolean checkDyes() {
		boolean didChange = false;
		if (cyanDye <= 1000 - HexWool.dyePerItem && inv[2] != null && Util.itemMatchesOre(inv[2], "dyeCyan")) {
			cyanDye += HexWool.dyePerItem;
			inv[2].stackSize--;
			if (inv[2].stackSize < 1)
				inv[2] = null;
			didChange = dyesChanged = true;
		}
		if (magentaDye <= 1000 - HexWool.dyePerItem && inv[3] != null && Util.itemMatchesOre(inv[3], "dyeMagenta")) {
			magentaDye += HexWool.dyePerItem;
			inv[3].stackSize--;
			if (inv[3].stackSize < 1)
				inv[3] = null;
			didChange = dyesChanged = true;
		}
		if (yellowDye <= 1000 - HexWool.dyePerItem && inv[4] != null && Util.itemMatchesOre(inv[4], "dyeYellow")) {
			yellowDye += HexWool.dyePerItem;
			inv[4].stackSize--;
			if (inv[4].stackSize < 1)
				inv[4] = null;
			didChange = dyesChanged = true;
		}
		if (blackDye <= 1000 - HexWool.dyePerItem && inv[5] != null && Util.itemMatchesOre(inv[5], "dyeBlack")) {
			blackDye += HexWool.dyePerItem;
			inv[5].stackSize--;
			if (inv[5].stackSize < 1)
				inv[5] = null;
			didChange = dyesChanged = true;
		}
		return didChange;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		colorCode = tagCompound.getString("colorCode");
		cyanDye = tagCompound.getInteger("cyanDye");
		magentaDye = tagCompound.getInteger("magentaDye");
		yellowDye = tagCompound.getInteger("yellowDye");
		blackDye = tagCompound.getInteger("blackDye");
		if (tagCompound.hasKey("colorizing"))
			colorizing = tagCompound.getBoolean("colorizing");
		if (tagCompound.hasKey("hiddenStack"))
			hiddenStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("hiddenStack"));

		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound)tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setString("colorCode", colorCode);
		tagCompound.setInteger("cyanDye", cyanDye);
		tagCompound.setInteger("magentaDye", magentaDye);
		tagCompound.setInteger("yellowDye", yellowDye);
		tagCompound.setInteger("blackDye", blackDye);
		tagCompound.setBoolean("colorizing", colorizing);
		if (hiddenStack != null) {
			NBTTagCompound tag = new NBTTagCompound();
			hiddenStack.writeToNBT(tag);
			tagCompound.setTag("hiddenStack", tag);
		}

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setString("colorCode", colorCode);
		tagCompound.setBoolean("colorizing", colorizing);
		for (int i = 0; i < 2; i++) {
			NBTTagCompound tag = new NBTTagCompound();
			if (inv[i] != null) {
				inv[i].writeToNBT(tag);
			} else
				tag.setShort("id", (short)0);
			tagCompound.setTag(i == 0 ? "input" : "output", tag);
		}
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager network, S35PacketUpdateTileEntity packet) {
		NBTTagCompound tagCompound = packet.func_148857_g();
		colorCode = tagCompound.getString("colorCode");
		colorizing = tagCompound.getBoolean("colorizing");
		NBTTagCompound tag = tagCompound.getCompoundTag("input");
		if (tag.getShort("id") != 0)
			inv[0] = ItemStack.loadItemStackFromNBT(tag);
		else
			inv[0] = null;
		tag = tagCompound.getCompoundTag("output");
		if (tag.getShort("id") != 0)
			inv[1] = ItemStack.loadItemStackFromNBT(tag);
		else
			inv[1] = null;
	}

	// *** Begin ComputerCraft Integration *** //
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String getType() {
		return "wool_colorizer";
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String[] getMethodNames() {
		return new String[]{"getHexCode", "setHexCode", "getCyanDye", "getMagentaDye", "getYellowDye", "getBlackDye"};
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException {
		switch (method) {
			case 0:
				return new Object[]{colorCode};
			case 1:
				if (arguments.length < 1)
					throw new LuaException("Not enough arguments");
				if (arguments[0].toString().length() != 6)
					throw new LuaException("Invalid hex code");
				try {
					Integer.parseInt(arguments[0].toString(), 16);
				} catch (NumberFormatException ex) {
					throw new LuaException("Invalid hex code");
				}
				colorCode = arguments[0].toString();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				break;
			case 2:
				return new Object[]{cyanDye};
			case 3:
				return new Object[]{magentaDye};
			case 4:
				return new Object[]{yellowDye};
			case 5:
				return new Object[]{blackDye};
		}
		return null;
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void attach(IComputerAccess computer) {
		// whatever
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void detach(IComputerAccess computer) {
		// whatever
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public boolean equals(IPeripheral other) {
		return other == this;
	}
	// *** End ComputerCraft Integration *** //
}

package com.techjar.hexwool.util;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerWrapper extends ItemStackHandler {
	protected ItemStackHandler itemStackHandler;

	public ItemStackHandlerWrapper(ItemStackHandler itemStackHandler) {
		this.itemStackHandler = itemStackHandler;
	}

	@Override
	public void setSize(int size) {
		itemStackHandler.setSize(size);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		itemStackHandler.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return itemStackHandler.getSlots();
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return itemStackHandler.getStackInSlot(slot);
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return itemStackHandler.insertItem(slot, stack, simulate);
	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return itemStackHandler.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return itemStackHandler.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return itemStackHandler.isItemValid(slot, stack);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return itemStackHandler.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		itemStackHandler.deserializeNBT(nbt);
	}
}

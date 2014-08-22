package com.techjar.hexwool.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.techjar.hexwool.gui.GuiWoolColorizer;

public class SlotColorizer extends Slot {
	public GuiWoolColorizer gui;

	public SlotColorizer(IInventory par1IInventory, int par2, int par3, int par4) {
		super(par1IInventory, par2, par3, par4);
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		if (gui != null) {
			gui.updateState();
		}
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return this.inventory.isItemValidForSlot(this.slotNumber, itemStack);
	}
}

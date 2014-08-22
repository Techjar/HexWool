package com.techjar.hexwool.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.network.packet.PacketGuiAction;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;
import com.techjar.hexwool.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerWoolColorizer extends Container {
	public TileEntityWoolColorizer tileEntity;
	public EntityPlayer lastEditor;
	private String oldColorCode = "";
	private int lastTicks;

	public ContainerWoolColorizer(InventoryPlayer inventoryPlayer, TileEntityWoolColorizer tile) {
		tileEntity = tile;

		addSlotToContainer(new SlotColorizer(tileEntity, 0, 17, 21));
		addSlotToContainer(new SlotColorizer(tileEntity, 1, 53, 21));
		for (int i = 0; i < 4; i++) {
			addSlotToContainer(new SlotColorizer(tileEntity, i + 2, 8 + i * 18, 47));
		}
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0, this.tileEntity.ticks);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (tileEntity.dyesChanged) {
			tileEntity.dyesChanged = false;
			for (int i = 0; i < this.crafters.size(); ++i) {
				ICrafting crafter = (ICrafting)this.crafters.get(i);
				if (crafter instanceof EntityPlayerMP) {
					HexWool.packetPipeline.sendToPlayer(new PacketGuiAction(PacketGuiAction.SET_DYE_AMOUNTS, String.format("%s;%s;%s;%s", tileEntity.cyanDye, tileEntity.magentaDye, tileEntity.yellowDye, tileEntity.blackDye)), (EntityPlayerMP)crafter);
				}
			}
		}
		if (!oldColorCode.equals(tileEntity.colorCode)) {
			oldColorCode = tileEntity.colorCode;
			for (int i = 0; i < this.crafters.size(); ++i) {
				ICrafting crafter = (ICrafting)this.crafters.get(i);
				if (crafter instanceof EntityPlayerMP) {
					if (crafter == lastEditor) {
						lastEditor = null;
						continue;
					}
					HexWool.packetPipeline.sendToPlayer(new PacketGuiAction(PacketGuiAction.SET_HEX_CODE, tileEntity.colorCode), (EntityPlayerMP)crafter);
				}
			}
		}

		if (this.lastTicks != this.tileEntity.ticks) {
			this.lastTicks = this.tileEntity.ticks;
			for (int i = 0; i < this.crafters.size(); ++i) {
				ICrafting crafter = (ICrafting)this.crafters.get(i);
				crafter.sendProgressBarUpdate(this, 0, this.tileEntity.ticks);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileEntity.ticks = par2;
		}
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 71 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 129));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot)inventorySlots.get(slot);

		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if (slot < 6) {
				if (!this.mergeItemStack(stackInSlot, 6, 42, true)) {
					return null;
				}
			} else if (Util.canColorizeItem(stackInSlot, 0)) {
				if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
					return null;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeCyan")) {
				if (!this.mergeItemStack(stackInSlot, 2, 3, false)) {
					return null;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeMagenta")) {
				if (!this.mergeItemStack(stackInSlot, 3, 4, false)) {
					return null;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeYellow")) {
				if (!this.mergeItemStack(stackInSlot, 4, 5, false)) {
					return null;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeBlack")) {
				if (!this.mergeItemStack(stackInSlot, 5, 6, false)) {
					return null;
				}
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}
}

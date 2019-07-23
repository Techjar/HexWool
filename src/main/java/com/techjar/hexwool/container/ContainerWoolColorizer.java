package com.techjar.hexwool.container;

import javax.annotation.Nonnull;

import com.techjar.hexwool.util.ColorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.network.packet.PacketGuiAction;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;
import com.techjar.hexwool.util.Util;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerWoolColorizer extends Container {
	public TileEntityWoolColorizer tileEntity;
	private String lastColorCode = "";
	private int lastTicks;
	private int lastCyanDye;
	private int lastMagentaDye;
	private int lastYellowDye;
	private int lastBlackDye;

	public ContainerWoolColorizer(InventoryPlayer inventoryPlayer, TileEntityWoolColorizer tile) {
		tileEntity = tile;

		IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 17, 21));
		addSlotToContainer(new SlotItemHandler(itemHandler, 1, 53, 21));
		for (int i = 0; i < 4; i++) {
			addSlotToContainer(new SlotItemHandler(itemHandler, i + 2, 8 + i * 18, 47));
		}
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendWindowProperty(this, 0, tileEntity.ticks);
		listener.sendWindowProperty(this, 1, tileEntity.cyanDye);
		listener.sendWindowProperty(this, 2, tileEntity.magentaDye);
		listener.sendWindowProperty(this, 3, tileEntity.yellowDye);
		listener.sendWindowProperty(this, 4, tileEntity.blackDye);
		if (listener instanceof EntityPlayerMP)
			HexWool.packetPipeline.sendToPlayer(new PacketGuiAction(PacketGuiAction.SET_HEX_CODE, tileEntity.colorCode), (EntityPlayerMP)listener);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.canInteractWith(player);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (IContainerListener listener : listeners) {
			if (lastTicks != tileEntity.ticks)
				listener.sendWindowProperty(this, 0, tileEntity.ticks);
			if (lastCyanDye != tileEntity.cyanDye)
				listener.sendWindowProperty(this, 1, tileEntity.cyanDye);
			if (lastMagentaDye != tileEntity.magentaDye)
				listener.sendWindowProperty(this, 2, tileEntity.magentaDye);
			if (lastYellowDye != tileEntity.yellowDye)
				listener.sendWindowProperty(this, 3, tileEntity.yellowDye);
			if (lastBlackDye != tileEntity.blackDye)
				listener.sendWindowProperty(this, 4, tileEntity.blackDye);

			if (listener instanceof EntityPlayerMP) {
				if (!lastColorCode.equals(tileEntity.colorCode)) {
					HexWool.packetPipeline.sendToPlayer(new PacketGuiAction(PacketGuiAction.SET_HEX_CODE, tileEntity.colorCode), (EntityPlayerMP)listener);
				}
			}
		}

		lastColorCode = tileEntity.colorCode;
		lastTicks = tileEntity.ticks;
		lastCyanDye = tileEntity.cyanDye;
		lastMagentaDye = tileEntity.magentaDye;
		lastYellowDye = tileEntity.yellowDye;
		lastBlackDye = tileEntity.blackDye;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		switch (id) {
			case 0:
				tileEntity.ticks = data;
				break;
			case 1:
				tileEntity.cyanDye = data;
				break;
			case 2:
				tileEntity.magentaDye = data;
				break;
			case 3:
				tileEntity.yellowDye = data;
				break;
			case 4:
				tileEntity.blackDye = data;
				break;
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
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = inventorySlots.get(slot);

		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if (slot < 6) {
				if (!this.mergeItemStack(stackInSlot, 6, 42, true)) {
					return ItemStack.EMPTY;
				}
			} else if (ColorHelper.canColorizeItem(stackInSlot, 0xFFFFFF)) {
				if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeCyan")) {
				if (!this.mergeItemStack(stackInSlot, 2, 3, false)) {
					return ItemStack.EMPTY;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeMagenta")) {
				if (!this.mergeItemStack(stackInSlot, 3, 4, false)) {
					return ItemStack.EMPTY;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeYellow")) {
				if (!this.mergeItemStack(stackInSlot, 4, 5, false)) {
					return ItemStack.EMPTY;
				}
			} else if (Util.itemMatchesOre(stackInSlot, "dyeBlack")) {
				if (!this.mergeItemStack(stackInSlot, 5, 6, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (stackInSlot.getCount() == 0) {
				slotObject.putStack(ItemStack.EMPTY);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			slotObject.onTake(player, stackInSlot);
		}
		return stack;
	}
}

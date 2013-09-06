package com.techjar.hexwool.container;

import java.io.IOException;

import com.techjar.hexwool.Util;
import com.techjar.hexwool.gui.GuiWoolColorizer;
import com.techjar.hexwool.network.packet.PacketGuiAction;
import com.techjar.hexwool.tileentity.TileEntityWoolColorizer;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWoolColorizer extends Container {
    public TileEntityWoolColorizer tileEntity;
    public Player lastEditor;
    private String oldColorCode = "";
    
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
                if (crafter instanceof Player) {
                    try {
                        PacketDispatcher.sendPacketToPlayer(new PacketGuiAction(PacketGuiAction.SET_DYE_AMOUNTS, String.format("%s;%s;%s;%s", tileEntity.cyanDye, tileEntity.magentaDye, tileEntity.yellowDye, tileEntity.blackDye)).getPacket(), (Player)crafter);
                    } catch (IOException ex) { ex.printStackTrace(); }
                }
            }
        }
        if (!oldColorCode.equals(tileEntity.colorCode)) {
            oldColorCode = tileEntity.colorCode;
            for (int i = 0; i < this.crafters.size(); ++i) {
                ICrafting crafter = (ICrafting)this.crafters.get(i);
                if (crafter instanceof Player) {
                    if (crafter == lastEditor) {
                        lastEditor = null;
                        continue;
                    }
                    try {
                        PacketDispatcher.sendPacketToPlayer(new PacketGuiAction(PacketGuiAction.SET_HEX_CODE, tileEntity.colorCode).getPacket(), (Player)crafter);
                    } catch (IOException ex) { ex.printStackTrace(); }
                }
            }
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
            }
            else if (Util.itemMatchesOre(stackInSlot, "blockWool")) {
                if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
                    return null;
                }
            }
            else if (Util.itemMatchesOre(stackInSlot, "dyeCyan")) {
                if (!this.mergeItemStack(stackInSlot, 2, 3, false)) {
                    return null;
                }
            }
            else if (Util.itemMatchesOre(stackInSlot, "dyeMagenta")) {
                if (!this.mergeItemStack(stackInSlot, 3, 4, false)) {
                    return null;
                }
            }
            else if (Util.itemMatchesOre(stackInSlot, "dyeYellow")) {
                if (!this.mergeItemStack(stackInSlot, 4, 5, false)) {
                    return null;
                }
            }
            else if (Util.itemMatchesOre(stackInSlot, "dyeBlack")) {
                if (!this.mergeItemStack(stackInSlot, 5, 6, false)) {
                    return null;
                }
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            }
            else {
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

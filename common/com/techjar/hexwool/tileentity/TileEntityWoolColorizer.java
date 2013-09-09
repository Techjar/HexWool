package com.techjar.hexwool.tileentity;

import java.io.IOException;

import com.techjar.hexwool.HexWool;
import com.techjar.hexwool.Util;
import com.techjar.hexwool.Util.CMYKColor;
import com.techjar.hexwool.container.ContainerWoolColorizer;
import com.techjar.hexwool.network.packet.PacketGuiAction;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityWoolColorizer extends TileEntity implements IInventory, ISidedInventory, IPeripheral {
    public String colorCode = "";
    public int cyanDye;
    public int magentaDye;
    public int yellowDye;
    public int blackDye;
    public boolean dyesChanged;
    private ItemStack[] inv;
    
    public TileEntityWoolColorizer() {
        inv = new ItemStack[6];
    }
    
    public boolean colorizeWool(int color) {
        ItemStack itemStack = inv[0];
        if (itemStack != null && hasRequiredDyes(color) && Util.itemMatchesOre(itemStack, "blockWool")) {
            if (itemStack.itemID == HexWool.idColoredWool && itemStack.hasTagCompound()) {
                if (itemStack.getTagCompound().hasKey("color") && itemStack.getTagCompound().getInteger("color") == color) {
                    inv[1] = itemStack;
                    inv[0] = null;
                    return true;
                }
            }
            int amountMade = 0;
            if (inv[1] != null) {
                if (inv[1].stackSize < 64) {
                    if (itemStack.itemID != HexWool.idColoredWool) itemStack.itemID = HexWool.idColoredWool;
                    itemStack.setItemDamage(0);
                    if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound("tag"));
                    itemStack.getTagCompound().setInteger("color", color);
                    
                    if (itemStack.isItemEqual(inv[1]) && ItemStack.areItemStackTagsEqual(itemStack, inv[1])) {
                        if (itemStack.stackSize + inv[1].stackSize > 64) {
                            amountMade = itemStack.stackSize - ((itemStack.stackSize + inv[1].stackSize) - 64);
                        }
                        else {
                            amountMade = itemStack.stackSize;
                        }
                    }
                }
            }
            else {
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
                            if (j == 1) break outer;
                            if (checkDyes()) break;
                        }
                    }
                    
                    cyanDye -= cyan;
                    magentaDye -= magenta;
                    yellowDye -= yellow;
                    blackDye -= black;
                    dyesChanged = true;
                    
                    itemStack.stackSize--;
                    if (inv[1] != null) inv[1].stackSize++;
                    else {
                        inv[1] = new ItemStack(HexWool.blockColoredWool);
                        inv[1].setTagCompound(new NBTTagCompound("tag"));
                        inv[1].getTagCompound().setInteger("color", color);
                    }
                    
                    if (itemStack.stackSize < 1) inv[0] = null;
                }
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            }
        }
        return false;
    }
    
    public int[] getRequiredDyes(int color) {
        int[] arr = new int[4];
        CMYKColor cmyk = Util.colorToCmyk(color);
        arr[0] = (int)(cmyk.getCyan() * HexWool.dyePerWool);
        arr[1] = (int)(cmyk.getMagenta() * HexWool.dyePerWool);
        arr[2] = (int)(cmyk.getYellow() * HexWool.dyePerWool);
        arr[3] = (int)(cmyk.getBlack() * HexWool.dyePerWool);
        return arr;
    }
    
    public boolean hasRequiredDyes(int color) {
        int[] dyes = getRequiredDyes(color);
        if (cyanDye < dyes[0] || magentaDye < dyes[1] || yellowDye < dyes[2] || blackDye < dyes[3]) {
            return false;
        }
        return true;
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
        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return "Wool Colorizer";
    }

    @Override
    public boolean isInvNameLocalized() {
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
    public void openChest() {
        // Useless
    }

    @Override
    public void closeChest() {
        // Useless
    }

    @Override
    public boolean isStackValidForSlot(int slot, ItemStack itemStack) {
        switch (slot) {
            case 0: return Util.itemMatchesOre(itemStack, "blockWool");
            case 1: return false;
            case 2: return Util.itemMatchesOre(itemStack, "dyeCyan");
            case 3: return Util.itemMatchesOre(itemStack, "dyeMagenta");
            case 4: return Util.itemMatchesOre(itemStack, "dyeYellow");
            case 5: return Util.itemMatchesOre(itemStack, "dyeBlack");
        }
        return false;
    }
    
    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{ 0, 1, 2, 3, 4, 5 };
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
        if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
            if (colorCode.length() == 6) {
                try {
                    int color = Integer.parseInt(colorCode, 16);
                    if (hasRequiredDyes(color)) colorizeWool(color);
                } catch (NumberFormatException ex) {}
            }
        }
    }
    
    public boolean checkDyes() {
        boolean didChange = false;
        if (cyanDye <= 1000 - HexWool.dyePerItem && inv[2] != null && Util.itemMatchesOre(inv[2], "dyeCyan")) {
            cyanDye += HexWool.dyePerItem;
            inv[2].stackSize--;
            if (inv[2].stackSize < 1) inv[2] = null;
            didChange = dyesChanged = true;
        }
        if (magentaDye <= 1000 - HexWool.dyePerItem && inv[3] != null && Util.itemMatchesOre(inv[3], "dyeMagenta")) {
            magentaDye += HexWool.dyePerItem;
            inv[3].stackSize--;
            if (inv[3].stackSize < 1) inv[3] = null;
            didChange = dyesChanged = true;
        }
        if (yellowDye <= 1000 - HexWool.dyePerItem && inv[4] != null && Util.itemMatchesOre(inv[4], "dyeYellow")) {
            yellowDye += HexWool.dyePerItem;
            inv[4].stackSize--;
            if (inv[4].stackSize < 1) inv[4] = null;
            didChange = dyesChanged = true;
        }
        if (blackDye <= 1000 - HexWool.dyePerItem && inv[5] != null && Util.itemMatchesOre(inv[5], "dyeBlack")) {
            blackDye += HexWool.dyePerItem;
            inv[5].stackSize--;
            if (inv[5].stackSize < 1) inv[5] = null;
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
        
        NBTTagList tagList = tagCompound.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound)tagList.tagAt(i);
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
        this.writeToNBT(tagCompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
    }
    
    @Override
    public void onDataPacket(INetworkManager network, Packet132TileEntityData packet) {
        this.readFromNBT(packet.customParam1);
    }

    //*** Begin ComputerCraft Integration ***//
    @Override
    public String getType() {
        return "wool_colorizer";
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{ "getHexCode", "setHexCode", "getCyanDye", "getMagentaDye", "getYellowDye", "getBlackDye" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
        switch(method) {
            case 0: return new Object[]{ colorCode };
            case 1:
                if (arguments.length < 1) throw new Exception("Not enough arguments");
                if (arguments[0].toString().length() != 6) throw new Exception("Invalid hex code");
                try { Integer.parseInt(arguments[0].toString(), 16); }
                catch(NumberFormatException ex) { throw new Exception("Invalid hex code"); }
                colorCode = arguments[0].toString();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                break;
            case 2: return new Object[]{ cyanDye };
            case 3: return new Object[]{ magentaDye };
            case 4: return new Object[]{ yellowDye };
            case 5: return new Object[]{ blackDye };
        }
        return null;
    }

    @Override
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer) {
        // whatever
    }

    @Override
    public void detach(IComputerAccess computer) {
        // whatever
    }
    //*** End ComputerCraft Integration ***//
}

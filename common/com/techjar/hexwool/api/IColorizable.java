package com.techjar.hexwool.api;

import net.minecraft.item.ItemStack;

public interface IColorizable {
    /**
     * Returns <em>true</em> if the ItemStack has a color explicitly set, <em>false</em> otherwise.
     * @param itemStack the color of the ItemStack to check
     * @return whether or not the ItemStack has a color set
     */
    public boolean hasColor(ItemStack itemStack);
    /**
     * Returns the color of the ItemStack passed to it as an integer.
     * @param itemStack the ItemStack to get the color of
     * @return the color of the ItemStack to return the color of
     */
    public int getColor(ItemStack itemStack);
    /**
     * Returns a copy of the ItemStack with the color set to the one passed to it.
     * @param itemStack the ItemStack to be colorized
     * @param color the color to be set
     * @return the colorized ItemStack
     */
    public ItemStack colorize(ItemStack itemStack, int color);
}

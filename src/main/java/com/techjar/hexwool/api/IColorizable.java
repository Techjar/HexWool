package com.techjar.hexwool.api;

import net.minecraft.item.ItemStack;

/**
 * Implement this on your Block or Item class to make it colorizable.
 * 
 * @author Techjar
 * 
 */
public interface IColorizable {
	/**
	 * Returns <em>true</em> if the ItemStack has a color explicitly set,
	 * <em>false</em> otherwise.
	 * 
	 * @param itemStack the color of the ItemStack to check
	 * @return whether or not the ItemStack has a color set
	 */
	boolean hasColor(ItemStack itemStack);

	/**
	 * Returns the color of the ItemStack passed to it as an integer.
	 * 
	 * @param itemStack the ItemStack to get the color of
	 * @return the color of the ItemStack to return the color of
	 */
	int getColor(ItemStack itemStack);

	/**
	 * Returns whether the color is acceptable for the ItemStack.
	 * White (0xFFFFFF) must always be accepted.
	 *
	 * @param itemStack the ItemStack in question
	 * @param color the color value in question
	 * @return whether the color is acceptable
	 */
	boolean acceptsColor(ItemStack itemStack, int color);

	/**
	 * Returns a copy of the ItemStack with the color set to the one passed to
	 * it, or the original ItemStack if colorization fails.
	 * 
	 * @param itemStack the ItemStack to be colorized
	 * @param color the color to be set
	 * @return the colorized ItemStack
	 */
	ItemStack colorize(ItemStack itemStack, int color);
}

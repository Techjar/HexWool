package com.techjar.hexwool.util;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.techjar.hexwool.api.IColorizable;
import com.techjar.hexwool.block.HexWoolBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ColorHelper {
	private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private static final ImmutableMap<Object, IColorizable> colorizerTable;

	static {
		IColorizable armorColorizer = new IColorizable() {
			@Override
			public boolean hasColor(ItemStack itemStack) {
				if (itemStack.hasTagCompound())
					return itemStack.getTagCompound().getCompoundTag("display").hasKey("color");
				return false;
			}

			@Override
			public int getColor(ItemStack itemStack) {
				if (hasColor(itemStack))
					return itemStack.getTagCompound().getCompoundTag("display").getInteger("color");
				return 0xFFFFFF;
			}

			@Override
			public boolean acceptsColor(ItemStack itemStack, int color) {
				return color >= 0 && color <= 0xFFFFFF;
			}

			@Override
			public ItemStack colorize(ItemStack itemStack, int color) {
				ItemStack stack = itemStack.copy();
				if (!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				if (!stack.getTagCompound().hasKey("display"))
					stack.getTagCompound().setTag("display", new NBTTagCompound());
				stack.getTagCompound().getCompoundTag("display").setInteger("color", color);
				return stack;
			}
		};

		colorizerTable = ImmutableMap.<Object, IColorizable>builder()
				.put(Items.LEATHER_HELMET, armorColorizer)
				.put(Items.LEATHER_CHESTPLATE, armorColorizer)
				.put(Items.LEATHER_LEGGINGS, armorColorizer)
				.put(Items.LEATHER_BOOTS, armorColorizer)
				.put("wool", new BlockColorizer(HexWoolBlocks.COLORED_WOOL))
				.put("blockGlass", new BlockColorizer(HexWoolBlocks.COLORED_GLASS))
				.put(Blocks.CONCRETE, new BlockColorizer(HexWoolBlocks.COLORED_CONCRETE))
				.put(Blocks.HARDENED_CLAY, new BlockColorizer(HexWoolBlocks.COLORED_TERRACOTTA))
				.put(Blocks.STAINED_HARDENED_CLAY, new BlockColorizer(HexWoolBlocks.COLORED_TERRACOTTA))
				.put("paneGlass", new BlockColorizer(HexWoolBlocks.COLORED_GLASS_PANE))
				.put(Blocks.CARPET, new BlockColorizer(HexWoolBlocks.COLORED_CARPET))
				.build();
	}

	public static IColorizable getColorizer(ItemStack itemStack) {
		Object itemObj = Util.getItemOrBlockFromStack(itemStack);
		if (itemObj instanceof IColorizable)
			return (IColorizable)itemObj;

		for (Map.Entry<Object, IColorizable> entry : colorizerTable.entrySet()) {
			if (entry.getKey() instanceof String) {
				if (Util.itemMatchesOre(itemStack, (String)entry.getKey()))
					return entry.getValue();
			} else if (entry.getKey() instanceof Item) {
				if (itemStack.getItem() == entry.getKey())
					return entry.getValue();
			} else if (entry.getKey() instanceof Block) {
				Block block = Block.getBlockFromItem(itemStack.getItem());
				if (block == entry.getKey())
					return entry.getValue();
			}
		}

		return null;
	}

	public static boolean canColorizeItem(ItemStack itemStack, int color) {
		IColorizable colorizer = getColorizer(itemStack);
		if (colorizer != null)
			return colorizer.acceptsColor(itemStack, color);
		return false;
	}

	public static boolean getItemHasColor(ItemStack itemStack) {
		IColorizable colorizer = getColorizer(itemStack);
		if (colorizer != null)
			return colorizer.hasColor(itemStack);
		return false;
	}

	public static int getItemColor(ItemStack itemStack) {
		IColorizable colorizer = getColorizer(itemStack);
		if (colorizer != null)
			return colorizer.getColor(itemStack);
		return 0xFFFFFF;
	}

	public static ItemStack colorizeItem(ItemStack itemStack, int color) {
		IColorizable colorizer = getColorizer(itemStack);
		if (colorizer != null)
			return colorizer.colorize(itemStack, color);
		return itemStack;
	}

	public static String colorToHex(int color) {
		StringBuilder hexBuilder = new StringBuilder(6);
		hexBuilder.setLength(6);
		for (int i = 5; i >= 0; i--) {
			int j = color & 0x0F;
			hexBuilder.setCharAt(i, hexDigits[j]);
			color >>= 4;
		}
		return hexBuilder.toString();
	}

	public static CMYKColor rgbToCmyk(int r, int g, int b) {
		if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
			throw new IllegalArgumentException("Invalid RGB value: " + r + "," + g + "," + b);
		}

		if (r == 0 && g == 0 && b == 0) {
			return new CMYKColor(0, 0, 0, 1);
		}

		float c = 1 - (r / 255.0F);
		float m = 1 - (g / 255.0F);
		float y = 1 - (b / 255.0F);

		float k = Math.min(c, Math.min(m, y));
		c = (c - k) / (1 - k);
		m = (m - k) / (1 - k);
		y = (y - k) / (1 - k);

		return new CMYKColor(c, m, y, k);
	}

	public static RGBColor colorToRgb(int color) {
		return new RGBColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
	}

	public static int rgbToColor(int r, int g, int b) {
		return ((r << 16) & 0xFF0000) | ((g << 8) & 0xFF00) | (b & 0xFF);
	}

	public static int rgbaToColor(int r, int g, int b, int a) {
		return ((a << 24) & 0xFF000000) | ((r << 16) & 0xFF0000) | ((g << 8) & 0xFF00) | (b & 0xFF);
	}

	public static int rgbToColor(RGBColor rgb) {
		return rgbToColor(rgb.r, rgb.g, rgb.b);
	}

	public static CMYKColor rgbToCmyk(RGBColor rgb) {
		return rgbToCmyk(rgb.r, rgb.g, rgb.b);
	}

	public static CMYKColor colorToCmyk(int color) {
		return rgbToCmyk((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
	}

	/**
	 * HSB to RGB conversion, pinched from java.awt.Color.
	 * @param hue (0..1.0f)
	 * @param saturation (0..1.0f)
	 * @param brightness (0..1.0f)
	 */
	public static RGBColor hsbToRgb(float hue, float saturation, float brightness) {
		RGBColor color = new RGBColor(0, 0, 0);
		if (saturation == 0.0F) {
			color.r = color.g = color.b = (int)(brightness * 255f + 0.5f);
		} else {
			float f3 = (hue - (float) Math.floor(hue)) * 6F;
			float f4 = f3 - (float) Math.floor(f3);
			float f5 = brightness * (1.0F - saturation);
			float f6 = brightness * (1.0F - saturation * f4);
			float f7 = brightness * (1.0F - saturation * (1.0F - f4));
			switch ((int) f3) {
				case 0 :
					color.r = (int)(brightness * 255f + 0.5f);
					color.g = (int)(f7 * 255f + 0.5f);
					color.b = (int)(f5 * 255f + 0.5f);
					break;
				case 1 :
					color.r = (int)(f6 * 255f + 0.5f);
					color.g = (int)(brightness * 255f + 0.5f);
					color.b = (int)(f5 * 255f + 0.5f);
					break;
				case 2 :
					color.r = (int)(f5 * 255f + 0.5f);
					color.g = (int)(brightness * 255f + 0.5f);
					color.b = (int)(f7 * 255f + 0.5f);
					break;
				case 3 :
					color.r = (int)(f5 * 255f + 0.5f);
					color.g = (int)(f6 * 255f + 0.5f);
					color.b = (int)(brightness * 255f + 0.5f);
					break;
				case 4 :
					color.r = (int)(f7 * 255f + 0.5f);
					color.g = (int)(f5 * 255f + 0.5f);
					color.b = (int)(brightness * 255f + 0.5f);
					break;
				case 5 :
					color.r = (int)(brightness * 255f + 0.5f);
					color.g = (int)(f5 * 255f + 0.5f);
					color.b = (int)(f6 * 255f + 0.5f);
					break;
			}
		}
		return color;
	}
	/**
	 * RGB to HSB conversion, pinched from java.awt.Color.
	 * @param r (0..255)
	 * @param g (0..255)
	 * @param b (0..255)
	 */
	public static float[] rgbToHsb(int r, int g, int b) {
		float[] dest = new float[3];
		int l = r <= g ? g : r;
		if (b > l)
			l = b;
		int i1 = r >= g ? g : r;
		if (b < i1)
			i1 = b;
		float brightness = l / 255F;
		float saturation;
		if (l != 0)
			saturation = (float) (l - i1) / (float) l;
		else
			saturation = 0.0F;
		float hue;
		if (saturation == 0.0F) {
			hue = 0.0F;
		} else {
			float f3 = (float) (l - r) / (float) (l - i1);
			float f4 = (float) (l - g) / (float) (l - i1);
			float f5 = (float) (l - b) / (float) (l - i1);
			if (r == l)
				hue = f5 - f4;
			else if (g == l)
				hue = (2.0F + f3) - f5;
			else
				hue = (4F + f4) - f3;
			hue /= 6F;
			if (hue < 0.0F)
				hue++;
		}
		dest[0] = hue;
		dest[1] = saturation;
		dest[2] = brightness;
		return dest;
	}

	public static float[] rgbToHsb(RGBColor color) {
		return rgbToHsb(color.r, color.g, color.b);
	}

	public static class RGBColor {
		private int r, g, b;

		public RGBColor(int red, int green, int blue) {
			this.r = red;
			this.g = green;
			this.b = blue;
		}

		public int getRed() {
			return r;
		}

		public void setRed(int red) {
			this.r = red;
		}

		public int getGreen() {
			return g;
		}

		public void setGreen(int green) {
			this.g = green;
		}

		public int getBlue() {
			return b;
		}

		public void setBlue(int blue) {
			this.b = blue;
		}
	}

	public static class CMYKColor {
		private float c, m, y, k;

		public CMYKColor(float cyan, float magenta, float yellow, float black) {
			this.c = cyan;
			this.m = magenta;
			this.y = yellow;
			this.k = black;
		}

		public float getCyan() {
			return c;
		}

		public void setCyan(float cyan) {
			this.c = cyan;
		}

		public float getMagenta() {
			return m;
		}

		public void setMagenta(float magenta) {
			this.m = magenta;
		}

		public float getYellow() {
			return y;
		}

		public void setYellow(float yellow) {
			this.y = yellow;
		}

		public float getBlack() {
			return k;
		}

		public void setBlack(float black) {
			this.k = black;
		}

		public CMYKColor add(CMYKColor other) {
			return new CMYKColor(this.c + other.c, this.m + other.m, this.y + other.y, this.k + other.k);
		}

		public CMYKColor subtract(CMYKColor other) {
			return new CMYKColor(this.c - other.c, this.m - other.m, this.y - other.y, this.k - other.k);
		}
	}

	private static class BlockColorizer implements IColorizable {
		private final Block outputBlock;

		public BlockColorizer(Block outputBlock) {
			this.outputBlock = outputBlock;
		}

		@Override
		public boolean hasColor(ItemStack itemStack) {
			return false;
		}

		@Override
		public int getColor(ItemStack itemStack) {
			return 0xFFFFFF;
		}

		@Override
		public boolean acceptsColor(ItemStack itemStack, int color) {
			return true;
		}

		@Override
		public ItemStack colorize(ItemStack itemStack, int color) {
			ItemStack stack = new ItemStack(outputBlock, itemStack.getCount());
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("Color", color);
			return stack;
		}
	}
}

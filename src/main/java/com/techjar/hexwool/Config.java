package com.techjar.hexwool;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

public class Config {
	public static int dyePerWool;
	public static int dyePerItem;
	public static int colorizingTicks;
	public static boolean creative;
	public static Map<ColorizableBlock, ColorizableBlock> colorizableBlocks = new HashMap<ColorizableBlock, ColorizableBlock>();
	
	public static class ColorizableBlock {
		Block block;
		byte meta;
		
		public ColorizableBlock(Block block, int meta) {
			this.block = block;
			this.meta = (byte)meta;
		}

		public Block getBlock() {
			return block;
		}

		public byte getMeta() {
			return meta;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColorizableBlock other = (ColorizableBlock)obj;
			if (block != other.block)
				return false;
			if (meta != other.meta)
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((block == null) ? 0 : block.hashCode());
			result = prime * result + meta;
			return result;
		}
	}
}

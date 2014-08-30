package com.techjar.hexwool.handlers;

import com.techjar.hexwool.util.HexWoolLog;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class HandlerInterModComms {
	@EventHandler
	public void onInterModComms(IMCEvent event) {
		for (IMCMessage msg : event.getMessages()) {
			if ("add-colorizable-block".equals(msg.key)) {
				if (msg.isNBTMessage()) {
					this.addColorizableBlock(msg.getNBTValue());
				} else {
					HexWoolLog.warning("%s sent a %s message with the wrong type! Should be an NBT message!", msg.getSender(), msg.key);
				}
			} else {
				HexWoolLog.warning("%s sent an unknown message: %s", msg.getSender(), msg.key);
			}
		}
	}
	
	private void addColorizableBlock(NBTTagCompound nbt) {
		
	}
}

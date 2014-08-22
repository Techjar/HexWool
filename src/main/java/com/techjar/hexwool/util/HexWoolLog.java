package com.techjar.hexwool.util;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class HexWoolLog {
	public static void info(String message, Object... data) {
		FMLRelaunchLog.log("HexWool", Level.INFO, message, data);
	}

	public static void warning(String message, Object... data) {
		FMLRelaunchLog.log("HexWool", Level.WARN, message, data);
	}

	public static void severe(String message, Object... data) {
		FMLRelaunchLog.log("HexWool", Level.ERROR, message, data);
	}

	public static void debug(String message, Object... data) {
		FMLRelaunchLog.log("HexWool", Level.DEBUG, message, data);
	}
}

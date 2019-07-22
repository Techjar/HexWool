package com.techjar.hexwool.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LogHelper {
	private static Logger logger;

	public static void setLogger(Logger l) {
		logger = l;
	}

	public static void info(String message, Object... params) {
		if (logger == null)
			return;
		logger.info(message, params);
	}

	public static void warning(String message, Object... params) {
		if (logger == null)
			return;
		logger.warn(message, params);
	}

	public static void severe(String message, Object... params) {
		if (logger == null)
			return;
		logger.fatal(message, params);
	}

	public static void debug(String message, Object... params) {
		if (logger == null)
			return;
		logger.debug(message, params);
	}
}

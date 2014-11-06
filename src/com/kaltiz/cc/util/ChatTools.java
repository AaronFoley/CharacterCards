package com.kaltiz.cc.util;

import org.bukkit.ChatColor;

public class ChatTools {

	public static String formatTitle(String title) {
		String format = "---------------------------------------------";
		int i = format.length() / 2;

		String titleText = "~[ " + ChatColor.WHITE + title + ChatColor.RED + " ]~";

		String retVal = ChatColor.RED + format.substring(0, Math.max(0, i - titleText.length() / 2));
		retVal = retVal + titleText + format.substring(i + titleText.length() / 2);

		return retVal;
	}
}

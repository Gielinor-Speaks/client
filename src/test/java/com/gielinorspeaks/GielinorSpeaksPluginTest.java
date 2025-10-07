package com.gielinorspeaks;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GielinorSpeaksPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GielinorSpeaksPlugin.class);
		RuneLite.main(args);
	}
}
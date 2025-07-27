package com.hoojplugins;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HoojPluginsTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HoojPlugins.class);
		RuneLite.main(args);
	}
}
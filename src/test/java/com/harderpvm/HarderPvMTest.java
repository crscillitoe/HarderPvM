package com.harderpvm;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HarderPvMTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HarderPvMPlugin.class);
		RuneLite.main(args);
	}
}
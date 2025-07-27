package com.hoojplugins;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("hooj_plugins")
public interface HoojPluginsConfig extends Config
{
	@ConfigItem(
			keyName = "delvePoisonDataCollection",
			name = "Collect Data on Delve Poison",
			description = ""
	)
	default boolean delvePoisonDataCollection()
	{
		return false;
	}
}

package com.dankscape;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface DankConfig extends Config
{
	@ConfigItem(
		keyName = "offsetX",
		name = "Camera Offset X",
		description = ""
	)
	default int offsetX()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "offsetY",
			name = "Camera Offset Y",
			description = ""
	)
	default int offsetY()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "extraWidth",
			name = "Extra Width",
			description = ""
	)
	default int extraWidth()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "extraHeight",
			name = "Extra Height",
			description = ""
	)
	default int extraHeight()
	{
		return 0;
	}
}

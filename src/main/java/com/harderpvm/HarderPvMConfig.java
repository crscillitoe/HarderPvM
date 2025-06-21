package com.harderpvm;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("harder_pvm")
public interface HarderPvMConfig extends Config
{
	// WIP
	//	@ConfigItem(
	//		keyName = "maximumGearValue",
	//		name = "Maximum Gear Value",
	//		description = ""
	//	)
	//	default int maximumGearValue()
	//	{
	//		return -1;
	//	}
	//
	//	@ConfigItem(
	//			keyName = "harderPrayer",
	//			name = "Harder Prayer",
	//			description = ""
	//	)
	//	default boolean harderPrayer()
	//	{
	//		return false;
	//	}

	@ConfigItem(
			keyName = "harderInventory",
			name = "Harder Inventory",
			description = ""
	)
	default boolean harderInventory()
	{
		return false;
	}
}

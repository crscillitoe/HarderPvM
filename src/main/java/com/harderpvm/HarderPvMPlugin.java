package com.harderpvm;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ClientTick;
import net.runelite.client.util.Text;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.HashSet;

@Slf4j
@PluginDescriptor(
	name = "Harder PvM"
)
public class HarderPvMPlugin extends Plugin
{
	private HashSet<String> itemList = new HashSet<>();

	@Inject
	private Client client;

	@Inject
	private HarderPvMConfig config;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		if (config.harderInventory()) {
			leftClickDropEverything();
		}
	}

	private void leftClickDropEverything() {
		// The menu is not rebuilt when it is open, so don't swap or else it will
		// repeatedly swap entries
		if (client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
		{
			return;
		}

		MenuEntry[] menuEntries = client.getMenuEntries();

		// Build option map for quick lookup in findIndex
		int idx = 0;
		for (MenuEntry entry : menuEntries)
		{
			String option = Text.removeTags(entry.getOption()).toLowerCase();
		}

		swapMenuEntry(menuEntries);
	}


	private int searchIndex(MenuEntry[] entries, String option, String target, boolean strict)
	{
		for (int i = entries.length - 1; i >= 0; i--)
		{
			MenuEntry entry = entries[i];
			String entryOption = Text.removeTags(entry.getOption()).toLowerCase();
			String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();

			if (strict)
			{
				if (entryOption.equals(option) && entryTarget.equals(target))
				{
					return i;
				}
			}
			else
			{
				if (entryOption.contains(option.toLowerCase()) && entryTarget.equals(target))
				{
					return i;
				}
			}
		}
		return -1;
	}

	private void swap(String optionA, String optionB, String target, boolean strict)
	{
		MenuEntry[] entries = client.getMenuEntries();

		int idxA = searchIndex(entries, optionA, target, strict);
		int idxB = searchIndex(entries, optionB, target, strict);

		if (idxA != idxB)
		{
			MenuEntry entry1 = entries[idxB];
			MenuEntry entry2 = entries[idxA];
			entries[idxA] = entry1;
			entries[idxB] = entry2;

			// Item op4 and op5 are CC_OP_LOW_PRIORITY so they get added underneath Use,
			// but this also causes them to get sorted after client tick. Change them to
			// CC_OP to avoid this.
			if (entry1.isItemOp() && entry1.getType() == MenuAction.CC_OP_LOW_PRIORITY)
			{
				entry1.setType(MenuAction.CC_OP);
			}
			if (entry2.isItemOp() && entry2.getType() == MenuAction.CC_OP_LOW_PRIORITY)
			{
				entry2.setType(MenuAction.CC_OP);
			}

			client.setMenuEntries(entries);
		}
	}

	private void swapMenuEntry(MenuEntry[] menuEntry)
	{
		try
		{
			if (menuEntry == null)
			{
				return;
			}

			// menuEntry.length - 1 is the default left click option. Use,Wear,Wield,Break, etc.
			final String option = Text.removeTags(menuEntry[menuEntry.length - 1].getOption()).toLowerCase();
			final String target = Text.removeTags(menuEntry[menuEntry.length - 1].getTarget()).toLowerCase();

			// swap first option with drop
			swap("drop", option, target, true);

		} catch (Exception ignored) {
			// ignored
		}
	}

	@Provides
	HarderPvMConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HarderPvMConfig.class);
	}
}

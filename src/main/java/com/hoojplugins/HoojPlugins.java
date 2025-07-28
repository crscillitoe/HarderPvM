package com.hoojplugins;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hoojplugins.logging.DelveLogger;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.events.ClientTick;
import com.google.inject.Provides;
import javax.inject.Inject;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.ArrayList;
import java.util.List;

import static com.hoojplugins.shared.HoojCommon.getRegionId;

@Slf4j
@PluginDescriptor(
	name = "Hooj Plugins"
)
public class HoojPlugins extends Plugin
{
	private final DelveLogger delveLogger = new DelveLogger();

	@Inject
	private Client client;

	@Inject
	private HoojPluginsConfig config;

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
		if (config.delvePoisonDataCollection()) {
			delveLogger.onTick(client, clientTick);
		}
	}

	@Provides
	HoojPluginsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HoojPluginsConfig.class);
	}
}

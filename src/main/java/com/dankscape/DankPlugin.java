package com.dankscape;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;

@Slf4j
@PluginDescriptor(
	name = "DankScape"
)
public class DankPlugin extends Plugin
{
	int initialWidth = 0;
	int initialHeight = 0;

	@Inject
	private Client client;

	@Inject
	private DankConfig config;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		this.updateCamera();
	}

	void updateCamera()
	{
		var offsetX = config.offsetX();
		var offsetY = config.offsetY();
		var extraWidth = config.extraWidth();
		var extraHeight = config.extraHeight();
		var canvas = client.getCanvas();
		var width = canvas.getWidth();
		var height = canvas.getHeight();

		canvas.setPreferredSize(new Dimension(width + extraWidth, height + extraHeight));
		canvas.setBounds(-offsetX, -offsetY, width + extraWidth, height + extraHeight);



		System.out.println("--------------------------");
		System.out.println(canvas.getMaximumSize());
		System.out.println(canvas.getSize());
		System.out.println(canvas.getPreferredSize());
		System.out.println("--------------------------");
	}


	@Provides
	DankConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DankConfig.class);
	}
}

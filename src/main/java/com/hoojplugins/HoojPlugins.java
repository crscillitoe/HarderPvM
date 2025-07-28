package com.hoojplugins;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
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
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@PluginDescriptor(
	name = "Hooj Plugins"
)
public class HoojPlugins extends Plugin
{
	private List<PoisonPoint> poisonPoints = new ArrayList<>();
	private int tick = 0;

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
			LocalPoint localPoint = client.getLocalPlayer().getLocalLocation();
			WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, localPoint);
			int regionID = worldPoint.getRegionID();
			boolean delving = regionID == 13668 || regionID == 14180;
			System.out.println(regionID);
			if (delving) {
				tick++;
				Scene scene = client.getScene();
				Tile[][][] tiles = scene.getTiles();
				for (int z = 0; z < tiles.length; z++) {
					for (int x = 0; x < tiles[z].length; x++) {
						for (int y = 0; y < tiles[z][x].length; y++) {
							Tile tile = tiles[z][x][y];
							TileObject[] tileObjects = tile.getGameObjects();
							for (TileObject tileObject : tileObjects) {
								// Poison
								if (tileObject != null) {
									if (tileObject.getId() == 57283) {
										boolean recorded = false;
										for (PoisonPoint poisonPoint : poisonPoints) {
											if (poisonPoint.x == x && poisonPoint.y == y) {
												recorded = true;
											}
										}

										if (!recorded) {
											PoisonPoint toAdd = new PoisonPoint();
											toAdd.x = x;
											toAdd.y = y;
											toAdd.tickAdded = tick;

											poisonPoints.add(toAdd);
										}
									}
								}
							}
						}
					}
				}
			} else {
				// not delving
				if (tick > 0) {
					// but we haven't saved data yet
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

					JsonArray outerArray = new JsonArray();

					JsonObject parent = new JsonObject();
					JsonArray innerArray = new JsonArray();
					int previousTick = poisonPoints.get(0).tickAdded;

					for (PoisonPoint poisonPoint : poisonPoints) {
						if (poisonPoint.tickAdded != previousTick) {
							parent.addProperty("tick", previousTick);
							parent.addProperty("poison", innerArray.toString());

							outerArray.add(parent);

							parent = new JsonObject();
							previousTick = poisonPoint.tickAdded;
							innerArray = new JsonArray();
						}

						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("x", poisonPoint.x);
						jsonObject.addProperty("y", poisonPoint.y);
						innerArray.add(jsonObject);
					}

					parent.addProperty("tick", previousTick);
					parent.addProperty("poison", innerArray.toString());

					clipboard.setContents(new StringSelection(outerArray.toString()), null);
					tick = 0;
					poisonPoints.clear();
				}
			}
		}
	}

	@Provides
	HoojPluginsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HoojPluginsConfig.class);
	}
}

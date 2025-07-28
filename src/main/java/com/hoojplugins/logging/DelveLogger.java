package com.hoojplugins.logging;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hoojplugins.PoisonPoint;
import net.runelite.api.Client;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.events.ClientTick;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

import static com.hoojplugins.shared.HoojCommon.getRegionId;

public class DelveLogger {
    private final List<PoisonPoint> poisonPoints;
    private int tick = 0;

    public DelveLogger() {
        poisonPoints = new ArrayList<>();
    }

    public void onTick(Client client, ClientTick clientTick) {
        int regionID = getRegionId(client);
        boolean delving = regionID == 13668 || regionID == 14180;
        if (delving) {
            tick++;
            Scene scene = client.getScene();
            processDelvingTick(scene);
        } else {
            saveAndReset();
        }
    }

    private void processDelvingTick(Scene scene) {
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
    }

    private void saveAndReset() {
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

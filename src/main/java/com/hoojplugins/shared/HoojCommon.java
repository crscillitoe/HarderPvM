package com.hoojplugins.shared;

import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public class HoojCommon {
    /**
     * Returns the regionID of the player. Matches logic used for
     * native tilemarker plugin.
     *
     * @param client
     * @return RegionID
     */
    public static int getRegionId(Client client) {
        LocalPoint localPoint = client.getLocalPlayer().getLocalLocation();
        WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, localPoint);

        return worldPoint.getRegionID();
    }
}

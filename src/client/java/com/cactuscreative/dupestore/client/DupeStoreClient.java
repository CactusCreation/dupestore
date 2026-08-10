package com.cactuscreative.dupestore.client;

import com.cactuscreative.dupestore.client.gui.MainScreen;
import com.cactuscreative.dupestore.client.gui.PinnedHudOverlay;
import com.cactuscreative.dupestore.client.util.Exploit;
import com.cactuscreative.dupestore.client.util.ExploitApi;
import com.cactuscreative.dupestore.client.util.KeyBinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.List;

public class DupeStoreClient implements ClientModInitializer {
    public static List<Exploit> exploits;
    @Override
	public void onInitializeClient() {
        KeyBinds.register();
        PinnedHudOverlay.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeyBinds.openGUIKeybind.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new MainScreen(Text.literal("Dupe Store")) {
                });
            }
        });
		ExploitApi exploitApi = new ExploitApi();
        try {
			exploits = exploitApi.getExploits(1000000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
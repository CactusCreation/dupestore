package com.cactuscreative.dupestore.client;

import com.cactuscreative.dupestore.client.util.Exploit;
import com.cactuscreative.dupestore.client.util.ExploitApi;
import net.fabricmc.api.ClientModInitializer;

import java.io.IOException;
import java.util.List;

public class DupeStoreClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ExploitApi exploitApi = new ExploitApi();
        try {
			List<Exploit> exploits = exploitApi.getExploits(1000000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
package dev.paulsoporan.pingnametags;

import dev.paulsoporan.pingnametags.config.PingNametagsConfigManager;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingNametagsClientMod implements ClientModInitializer {
	public static final String MOD_ID = "pingnametags";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		PingNametagsConfigManager.init();
	}
}

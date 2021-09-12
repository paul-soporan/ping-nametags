package dev.paulsoporan.pingnametags;

import dev.paulsoporan.pingnametags.config.PingNametagsConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;

public class PingNametagsClientMod implements ClientModInitializer {
	public static final String MOD_ID = "pingnametags";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static KeyBinding TOGGLE_ENABLED_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			String.format("config.%s.toggleEnabledKey", MOD_ID),
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_G,
			String.format("config.%s.keyCategory", MOD_ID)
	));

	@Override
	public void onInitializeClient() {
		PingNametagsConfigManager.init();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (TOGGLE_ENABLED_KEYBINDING.wasPressed()) {
				PingNametagsConfigManager.getConfig().toggleEnabled();
				PingNametagsConfigManager.save();
			}
		});
	}
}

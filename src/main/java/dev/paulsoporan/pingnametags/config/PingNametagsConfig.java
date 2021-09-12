package dev.paulsoporan.pingnametags.config;

import dev.paulsoporan.pingnametags.PingNametagsClientMod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

public class PingNametagsConfig {
    private static final boolean DEFAULT_ENABLED = true;

    private static final PingTextPosition DEFAULT_PING_TEXT_POSITION = PingTextPosition.Right;

    private static final String DEFAULT_PING_TEXT_FORMAT_STRING = "(%dms)";

    @NotNull
    private boolean enabled = DEFAULT_ENABLED;

    @NotNull
    private PingTextPosition pingTextPosition = DEFAULT_PING_TEXT_POSITION;

    @NotNull
    private String pingTextFormatString = DEFAULT_PING_TEXT_FORMAT_STRING;

    static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText(String.format("config.%s.title", PingNametagsClientMod.MOD_ID)));

        PingNametagsConfig config = PingNametagsConfigManager.getConfig();

        builder.getOrCreateCategory(new LiteralText("general"))
                .addEntry(ConfigEntryBuilder.create()
                        .startBooleanToggle(new TranslatableText(String.format("config.%s.enabled", PingNametagsClientMod.MOD_ID)), config.getEnabled())
                        .setDefaultValue(DEFAULT_ENABLED)
                        .setTooltip(new TranslatableText(String.format("config.%s.enabled.description", PingNametagsClientMod.MOD_ID)))
                        .setSaveConsumer(b -> config.enabled = b)
                        .build())
                .addEntry(ConfigEntryBuilder.create()
                        .startEnumSelector(new TranslatableText(String.format("config.%s.pingTextPosition", PingNametagsClientMod.MOD_ID)), PingTextPosition.class, config.getPingTextPosition())
                        .setDefaultValue(DEFAULT_PING_TEXT_POSITION)
                        .setTooltip(new TranslatableText(String.format("config.%s.pingTextPosition.description", PingNametagsClientMod.MOD_ID)))
                        .setSaveConsumer(p -> config.pingTextPosition = p)
                        .build())
                .addEntry(ConfigEntryBuilder.create()
                        .startStrField(new TranslatableText(String.format("config.%s.pingTextFormatString", PingNametagsClientMod.MOD_ID)), config.getPingTextFormatString())
                        .setDefaultValue(DEFAULT_PING_TEXT_FORMAT_STRING)
                        .setTooltip(new TranslatableText(String.format("config.%s.pingTextFormatString.description", PingNametagsClientMod.MOD_ID)))
                        .setSaveConsumer(s -> config.pingTextFormatString = s)
                        .build());

        builder.setSavingRunnable(PingNametagsConfigManager::save);

        return builder.build();
    }

    public boolean getEnabled() {
        return enabled;
    }

    public PingTextPosition getPingTextPosition() {
        return pingTextPosition;
    }

    public String getPingTextFormatString() {
        return pingTextFormatString;
    }
}

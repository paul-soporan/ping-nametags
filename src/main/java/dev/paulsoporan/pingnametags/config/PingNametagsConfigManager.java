package dev.paulsoporan.pingnametags.config;

import com.google.gson.*;
import dev.paulsoporan.pingnametags.PingNametagsClientMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PingNametagsConfigManager {
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor(r -> new Thread(r, "Ping Nametags Config Manager"));
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    private static PingNametagsConfig config;
    private static Path configFile;

    public static PingNametagsConfig getConfig() {
        return config != null ? config : init();
    }

    public static PingNametagsConfig init() {
        configFile = FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve(PingNametagsClientMod.MOD_ID + ".json");
        if(!Files.exists(configFile)) {
            PingNametagsClientMod.LOGGER.info(String.format("creating %s config file ({})", PingNametagsClientMod.MOD_ID), configFile::getFileName);
            save().join();
        }
        load().thenApply(c -> config = c).join();
        return Objects.requireNonNull(config, "failed to init config");
    }

    public static CompletableFuture<PingNametagsConfig> load() {
        return CompletableFuture.supplyAsync(() -> {
            try(BufferedReader reader = Files.newBufferedReader(configFile)) {
                return GSON.fromJson(reader, PingNametagsConfig.class);
            } catch (IOException | JsonParseException e) {
                PingNametagsClientMod.LOGGER.error("unable to read config file, restoring defaults", e);
                save();
                return new PingNametagsConfig();
            }
        }, EXECUTOR);
    }

    public static CompletableFuture<Void> save() {
        PingNametagsClientMod.LOGGER.trace(String.format("saving %s config file to {}", PingNametagsClientMod.MOD_ID), configFile);
        return CompletableFuture.runAsync(() -> {
            try(BufferedWriter writer = Files.newBufferedWriter(configFile)) {
                GSON.toJson(Optional.ofNullable(config).orElseGet(PingNametagsConfig::new), writer);
            } catch (IOException | JsonIOException e) {
                PingNametagsClientMod.LOGGER.error("unable to write config file", e);
            }
        }, EXECUTOR);
    }
}

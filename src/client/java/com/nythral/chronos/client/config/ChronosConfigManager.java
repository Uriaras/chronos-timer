package com.nythral.chronos.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nythral.chronos.ChronosTimer;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class ChronosConfigManager {

    private static final Gson GSON =
        new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path CONFIG_PATH =
        FabricLoader.getInstance()
            .getConfigDir()
            .resolve("chronos-timer.json");

    private ChronosConfigManager() {
    }

    public static void load() {
        ChronosConfig config =
            ChronosConfig.INSTANCE;

        if (!Files.exists(CONFIG_PATH)) {
            config.reset();
            save();
            return;
        }

        try (
            Reader reader =
                Files.newBufferedReader(
                    CONFIG_PATH
                )
        ) {
            ChronosConfig loadedConfig =
                GSON.fromJson(
                    reader,
                    ChronosConfig.class
                );

            if (loadedConfig == null) {
                config.reset();
                save();
                return;
            }

            config.copyFrom(loadedConfig);
        } catch (
            IOException
                | RuntimeException exception
        ) {
            ChronosTimer.LOGGER.error(
                "Failed to load Chronos Timer config.",
                exception
            );

            config.reset();
            save();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(
                CONFIG_PATH.getParent()
            );

            try (
                Writer writer =
                    Files.newBufferedWriter(
                        CONFIG_PATH
                    )
            ) {
                GSON.toJson(
                    ChronosConfig.INSTANCE,
                    writer
                );
            }
        } catch (IOException exception) {
            ChronosTimer.LOGGER.error(
                "Failed to save Chronos Timer config.",
                exception
            );
        }
    }
}
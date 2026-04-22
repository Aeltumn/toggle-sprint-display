package com.aeltumn.togglesprintdisplay.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Vector2i;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class SprintConfig {
    private static final Gson GSON = new Gson();
    private static SprintConfig instance;

    public VisualsType visualsType = VisualsType.ICON;
    public Anchor anchor = Anchor.TOP_RIGHT;
    public boolean showSprinting = true;
    public boolean showSneaking = true;
    public boolean showSwimming = true;
    public boolean showDoll = true;
    public boolean dollBelow = false;
    public double positionX = 0.02;
    public double positionY = 0.03;
    public double scale = 1.0;

    /**
     * Returns the configured position.
     */
    public Vector2i getPosition(GuiGraphicsExtractor guiGraphics) {
        return new Vector2i(
                (int) (positionX * guiGraphics.guiWidth() / scale),
                (int) (positionY * guiGraphics.guiHeight() / scale));
    }

    /**
     * Returns the current config state.
     */
    public static SprintConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    /**
     * Loads this configuration file.
     */
    private static SprintConfig load() {
        var file = getConfigFile();
        if (Files.exists(file)) {
            try (var reader = new FileReader(file.toFile())) {
                return GSON.fromJson(reader, SprintConfig.class);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return new SprintConfig();
    }

    /**
     * Saves the changes to this configuration file.
     */
    public void save() {
        try {
            Files.writeString(getConfigFile(), GSON.toJson(this));
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Returns the path where the configuration file is stored.
     */
    public static Path getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("toggle-sprint-display.json");
    }
}


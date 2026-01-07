package com.aeltumn.togglesprintdisplay;

import com.aeltumn.togglesprintdisplay.config.ToggleSprintConfig;
import net.fabricmc.api.ClientModInitializer;

public class ToggleSprintDisplayMod implements ClientModInitializer {
    public static final String MOD_ID = "toggle-sprint-display";

    @Override
    public void onInitializeClient() {
        // Load config on startup
        ToggleSprintConfig.getInstance();
    }
}

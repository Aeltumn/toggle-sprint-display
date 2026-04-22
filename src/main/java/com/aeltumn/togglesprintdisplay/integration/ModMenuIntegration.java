package com.aeltumn.togglesprintdisplay.integration;

import com.aeltumn.togglesprintdisplay.config.SettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Adds a custom settings menu when clicking on the mod in Mod Menu.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SettingsScreen::new;
    }
}

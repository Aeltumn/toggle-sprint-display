package com.aeltumn.togglesprintdisplay.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends OptionsSubScreen {
    public SettingsScreen(Screen screen) {
        super(screen, Minecraft.getInstance().options, Component.translatable("toggle_sprint_display.options.screen"));
    }

    @Override
    protected void addOptions() {
        this.list.addHeader(Component.translatable("toggle_sprint_display.options.display"));
        this.list.addBig(SprintOptions.VISUALS_TYPE);
        this.list.addSmall(
                SprintOptions.SHOW_SPRINTING,
                SprintOptions.SHOW_SNEAKING,
                SprintOptions.SHOW_SWIMMING
        );
        this.list.addHeader(Component.translatable("toggle_sprint_display.options.positioning"));
        this.list.addBig(SprintOptions.ANCHOR);
        this.list.addSmall(
                SprintOptions.POSITION_X,
                SprintOptions.POSITION_Y,
                SprintOptions.SCALE
        );

        this.list.addHeader(Component.translatable("toggle_sprint_display.options.doll"));
        this.list.addSmall(
                SprintOptions.SHOW_DOLL,
                SprintOptions.PLACE_DOLL_BELOW
        );
    }
}

package com.aeltumn.togglesprintdisplay.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ToggleSprintConfig config = ToggleSprintConfig.getInstance();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.translatable("config.toggle_sprint_display.title"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory position = builder.getOrCreateCategory(
                    Component.translatable("config.toggle_sprint_display.category.position"));

            // Anchor position dropdown
            position.addEntry(entryBuilder
                    .startEnumSelector(
                            Component.translatable("config.toggle_sprint_display.anchor"),
                            ToggleSprintConfig.PositionAnchor.class,
                            config.getAnchor())
                    .setDefaultValue(ToggleSprintConfig.PositionAnchor.TOP_LEFT)
                    .setTooltip(Component.translatable("config.toggle_sprint_display.anchor.tooltip"))
                    .setEnumNameProvider(value -> Component.translatable(
                            "config.toggle_sprint_display.anchor." + value.name().toLowerCase()))
                    .setSaveConsumer(config::setAnchor)
                    .build());

            // X offset slider
            position.addEntry(entryBuilder
                    .startIntSlider(
                            Component.translatable("config.toggle_sprint_display.offset_x"),
                            config.getOffsetX(),
                            -500,
                            500)
                    .setDefaultValue(7)
                    .setTooltip(Component.translatable("config.toggle_sprint_display.offset_x.tooltip"))
                    .setSaveConsumer(config::setOffsetX)
                    .build());

            // Y offset slider
            position.addEntry(entryBuilder
                    .startIntSlider(
                            Component.translatable("config.toggle_sprint_display.offset_y"),
                            config.getOffsetY(),
                            -500,
                            500)
                    .setDefaultValue(7)
                    .setTooltip(Component.translatable("config.toggle_sprint_display.offset_y.tooltip"))
                    .setSaveConsumer(config::setOffsetY)
                    .build());

            builder.setSavingRunnable(config::save);

            return builder.build();
        };
    }
}

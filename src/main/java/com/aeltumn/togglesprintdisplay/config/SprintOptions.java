package com.aeltumn.togglesprintdisplay.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.Arrays;

/**
 * Stores the option instances for the various options.
 */
public class SprintOptions {
    public static final OptionInstance<VisualsType> VISUALS_TYPE = new OptionInstance<>(
            "toggle_sprint_display.options.visuals_type.name",
            (value) -> Tooltip.create(
                    Component.translatable("toggle_sprint_display.options.visuals_type.tooltip")
                            .append("\n\n")
                            .append(Component.translatable("toggle_sprint_display.options.enum." + value.name().toLowerCase()).withStyle(Style.EMPTY.withBold(true)))
                            .append("\n")
                            .append(Component.translatable("toggle_sprint_display.options.visuals_type.tooltip." + value.name().toLowerCase()))
            ),
            SprintOptions::enumLabel,
            new OptionInstance.Enum<>(
                    Arrays.asList(VisualsType.values()),
                    Codec.STRING.xmap(VisualsType::valueOf, VisualsType::name)),
            SprintConfig.get().visualsType,
            (newValue) -> {
                var config = SprintConfig.get();
                config.visualsType = newValue;
                config.save();
            });

    public static final OptionInstance<Anchor> ANCHOR = new OptionInstance<>(
            "toggle_sprint_display.options.anchor.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("toggle_sprint_display.options.anchor.tooltip")),
            SprintOptions::enumLabel,
            new OptionInstance.Enum<>(
                    Arrays.asList(Anchor.values()),
                    Codec.STRING.xmap(Anchor::valueOf, Anchor::name)),
            SprintConfig.get().anchor,
            (newValue) -> {
                var config = SprintConfig.get();
                config.anchor = newValue;
                config.save();
            });

    public static final OptionInstance<Boolean> SHOW_SPRINTING = OptionInstance.createBoolean(
            "toggle_sprint_display.options.show_sprinting.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("toggle_sprint_display.options.show_sprinting.tooltip")),
            SprintConfig.get().showSprinting,
            (newValue) -> {
                var config = SprintConfig.get();
                config.showSprinting = newValue;
                config.save();
            });

    public static final OptionInstance<Boolean> SHOW_SNEAKING = OptionInstance.createBoolean(
            "toggle_sprint_display.options.show_sneaking.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("toggle_sprint_display.options.show_sneaking.tooltip")),
            SprintConfig.get().showSneaking,
            (newValue) -> {
                var config = SprintConfig.get();
                config.showSneaking = newValue;
                config.save();
            });

    public static final OptionInstance<Boolean> SHOW_SWIMMING = OptionInstance.createBoolean(
            "toggle_sprint_display.options.show_swimming.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("toggle_sprint_display.options.show_swimming.tooltip")),
            SprintConfig.get().showSwimming,
            (newValue) -> {
                var config = SprintConfig.get();
                config.showSwimming = newValue;
                config.save();
            });

    public static final OptionInstance<Boolean> SHOW_DOLL = OptionInstance.createBoolean(
            "toggle_sprint_display.options.show_doll.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("toggle_sprint_display.options.show_doll.tooltip")),
            SprintConfig.get().showDoll,
            (newValue) -> {
                var config = SprintConfig.get();
                config.showDoll = newValue;
                config.save();
            });

    public static final OptionInstance<Boolean> PLACE_DOLL_BELOW = OptionInstance.createBoolean(
            "toggle_sprint_display.options.place_doll_below.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("toggle_sprint_display.options.place_doll_below.tooltip")),
            SprintConfig.get().dollBelow,
            (newValue) -> {
                var config = SprintConfig.get();
                config.dollBelow = newValue;
                config.save();
            });

    public static final OptionInstance<Double> POSITION_X = new OptionInstance<>(
            "toggle_sprint_display.options.position_x.name",
            OptionInstance.cachedConstantTooltip(Component.translatable("toggle_sprint_display.options.position_x.tooltip")),
            SprintOptions::valueLabel,
            new OptionInstance.IntRange(0, 100).xmap(it -> (double) it / 100.0, it -> (int) (it * 100.0), true),
            Codec.doubleRange(0.0, 1.0),
            SprintConfig.get().posX,
            (newValue) -> {
                var config = SprintConfig.get();
                config.posX = newValue;
                config.save();
            });

    public static final OptionInstance<Double> POSITION_Y = new OptionInstance<>(
            "toggle_sprint_display.options.position_y.name",
            OptionInstance.cachedConstantTooltip(Component.translatable("toggle_sprint_display.options.position_y.tooltip")),
            SprintOptions::valueLabel,
            new OptionInstance.IntRange(0, 100).xmap(it -> (double) it / 100.0, it -> (int) (it * 100.0), true),
            Codec.doubleRange(0.0, 1.0),
            SprintConfig.get().posY,
            (newValue) -> {
                var config = SprintConfig.get();
                config.posY = newValue;
                config.save();
            });

    public static final OptionInstance<Double> SCALE = new OptionInstance<>(
            "toggle_sprint_display.options.scale.name",
            OptionInstance.cachedConstantTooltip(Component.translatable("toggle_sprint_display.options.scale.tooltip")),
            SprintOptions::percentageLabel,
            new OptionInstance.IntRange(1, 200).xmap(it -> (double) it / 100.0, it -> (int) (it * 100.0), true),
            Codec.doubleRange(0.01, 2.0),
            SprintConfig.get().scale,
            (newValue) -> {
                var config = SprintConfig.get();
                config.scale = newValue;
                config.save();
            });

    private static Component enumLabel(Component component, Enum<?> e) {
        return Component.translatable("toggle_sprint_display.options.enum." + e.name().toLowerCase());
    }

    private static Component percentageLabel(Component component, double value) {
        return Component.translatable("options.percent_value", component, (int) (value * 100.0));
    }

    private static Component valueLabel(Component component, double value) {
        var integer = (int) (value * 100.0);
        if (integer < 0) {
            return Component.translatable("toggle_sprint_display.options.simple_value", component, Integer.toString(integer));
        } else {
            return Component.translatable("toggle_sprint_display.options.simple_value", component, "+" + integer);
        }
    }
}

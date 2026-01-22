package com.aeltumn.togglesprintdisplay.mixin;

import com.aeltumn.togglesprintdisplay.config.ToggleSprintConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    public abstract Font getFont();

    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        tsd$renderSprintingOverlay(graphics);
    }

    @Unique
    private void tsd$renderSprintingOverlay(GuiGraphics guiGraphics) {
        var minecraft = Minecraft.getInstance();

        // If nothing is rendering, don't show!
        if (minecraft.noRender) return;

        // Don't show while still loading
        if (!minecraft.isGameLoadFinished()) return;

        // Check that the main GUI is not hidden
        if (minecraft.options.hideGui)
            return;

        var isSprinting = minecraft.options.keySprint.isDown();
        var toggleSprint = minecraft.options.toggleSprint().get();
        var text = Component.translatable("menu.toggle_sprint_display." + (toggleSprint ? "toggle" : "hold") + "_"
                + (isSprinting ? "on" : "off"));

        // If there is no text to be drawn, do nothing!
        if (text.getString().isEmpty())
            return;

        var font = getFont();
        var config = ToggleSprintConfig.getInstance();

        int textWidth = font.width(text);
        int textHeight = font.lineHeight;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        int x = config.calculateX(screenWidth, textWidth);
        int y = config.calculateY(screenHeight, textHeight);

        guiGraphics.nextStratum();
        guiGraphics.drawString(font, text, x, y, -1);
    }
}

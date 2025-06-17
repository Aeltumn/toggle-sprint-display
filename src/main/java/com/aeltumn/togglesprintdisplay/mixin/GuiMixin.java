package com.aeltumn.togglesprintdisplay.mixin;

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
    public abstract DebugScreenOverlay getDebugOverlay();

    @Shadow
    public abstract Font getFont();

    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        tsd$renderSprintingOverlay(graphics);
    }

    @Unique
    private void tsd$renderSprintingOverlay(GuiGraphics guiGraphics) {
        var minecraft = Minecraft.getInstance();

        // Check that the main GUI is not hidden
        if (minecraft.options.hideGui) return;

        // Check that the debug screen is not up
        if (this.getDebugOverlay().showDebugScreen()) return;

        var isSprinting = minecraft.options.keySprint.isDown();
        var toggleSprint = minecraft.options.toggleSprint().get();
        var text = Component.translatable("menu.toggle_sprint_display." + (toggleSprint ? "toggle" : "hold") + "_" + (isSprinting ? "on" : "off"));

        // If there is no text to be drawn, do nothing!
        if (text.getString().isEmpty()) return;

        var font = getFont();
        guiGraphics.nextStratum();
        guiGraphics.drawString(font, text, 7, 7, -1);
    }
}

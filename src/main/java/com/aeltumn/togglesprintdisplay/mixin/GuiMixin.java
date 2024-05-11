package com.aeltumn.togglesprintdisplay.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    @Final
    private LayeredDraw layers;

    @Shadow
    public abstract DebugScreenOverlay getDebugOverlay();

    @Shadow
    public abstract Font getFont();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(Minecraft minecraft, CallbackInfo ci) {
        var overlay = new LayeredDraw();
        overlay.add(this::tsd$renderSprintingOverlay);
        this.layers.add(overlay, () ->
            // Check that the main GUI is not hidden
            !minecraft.options.hideGui &&
                // Check that the debug screen is not up
                !this.getDebugOverlay().showDebugScreen()
        );
    }

    @Unique
    private void tsd$renderSprintingOverlay(GuiGraphics guiGraphics, float partialTicks) {
        var minecraft = Minecraft.getInstance();
        var isSprinting = minecraft.options.keySprint.isDown();
        var toggleSprint = minecraft.options.toggleSprint().get();
        var text = Component.translatable("menu.toggle_sprint_display." + (toggleSprint ? "toggle" : "hold") + "_" + (isSprinting ? "on" : "off"));

        // If there is no text to be drawn, do nothing!
        if (text.getString().isEmpty()) return;

        var k = 16777215;
        var font = getFont();
        guiGraphics.drawString(font, text, 7, 7, k);
    }
}

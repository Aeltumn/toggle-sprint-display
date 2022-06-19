package com.aeltumn.togglesprintdisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderSavingIndicator", at = @At("RETURN"))
    private void injected(PoseStack poseStack, CallbackInfo ci) {
        Gui gui = (Gui) ((Object) this);
        Font font = gui.getFont();

        // Don't render when the debug screen is shown as it would overlap
        if (minecraft.options.renderDebug) return;

        boolean isSprinting = minecraft.options.keySprint.isDown();
        boolean toggleSprint = minecraft.options.toggleSprint;
        Component text = new TranslatableComponent("menu.toggle_sprint_display." + (toggleSprint ? "toggle" : "hold") + "_" + (isSprinting ? "on" : "off"));
        int k = 16777215;
        font.drawShadow(poseStack, text, 7.5f, 7.5f, k);
    }
}

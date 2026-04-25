package com.aeltumn.togglesprintdisplay.mixin;

import com.aeltumn.togglesprintdisplay.config.SprintConfig;
import com.aeltumn.togglesprintdisplay.config.VisualsType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    public abstract Font getFont();

    @Inject(method = "extractSavingIndicator", at = @At("TAIL"))
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (player == null) return;

        // Check that the main GUI is not hidden
        if (minecraft.options.hideGui) return;

        var config = SprintConfig.get();
        var isSprinting = config.showSprinting && minecraft.options.keySprint.isDown();
        var isSneaking = config.showSneaking && minecraft.options.keyShift.isDown();
        var toggleSprint = minecraft.options.toggleSprint().get();
        var toggleSneak = minecraft.options.toggleCrouch().get();
        var isSwimming = config.showSwimming && player.isSwimming();
        var rightAligned = config.anchor.isRightAligned();
        var bottomAligned = config.anchor.isBottomAligned();

        var text = new ArrayList<MutableComponent>();
        var style = Style.EMPTY.applyFormat(ChatFormatting.GRAY).withItalic(true);
        if (isSprinting || (config.visualsType != VisualsType.FULL && isSwimming)) {
            switch (config.visualsType) {
                case FULL -> {
                    text.add(Component.empty()
                            .append(Component.translatable("toggle_sprint_display.type.sprint.long"))
                            .append(" ")
                            .append(Component.translatable("toggle_sprint_display.type." + (toggleSprint ? "toggle" : "hold"))));
                }
                case SHORT -> {
                    text.add(Component.translatable("toggle_sprint_display.type." + (isSwimming ? "swim.regular" : "sprint.short")));
                }
                case ICON -> {
                    text.add(Component.translatable("toggle_sprint_display.type." + (isSwimming ? "swim" : "sprint") + ".emoji"));
                }
            }
        }
        if (isSneaking) {
            switch (config.visualsType) {
                case FULL -> {
                    text.add(Component.empty()
                            .append(Component.translatable("toggle_sprint_display.type.shift.long"))
                            .append(" ")
                            .append(Component.translatable("toggle_sprint_display.type." + (toggleSneak ? "toggle" : "hold"))));
                }
                case SHORT -> {
                    text.add(Component.translatable("toggle_sprint_display.type.shift.short"));
                }
                case ICON -> {
                    text.add(Component.translatable("toggle_sprint_display.type.shift.emoji"));
                }
            }
        }

        if (!text.isEmpty()) {
            // If we're in icon mode, merge everything to one line!
            if (config.visualsType == VisualsType.ICON) {
                var mergedText = Component.empty();
                for (var line : text) {
                    mergedText = mergedText.append(line).append(" ");
                }
                text.clear();
                text.add(mergedText);
            }
        }

        graphics.pose().pushMatrix();
        graphics.pose().scale((float) config.scale);

        // Determine the size that we need to render
        var font = getFont();
        var width = 0;
        var height = text.size() * font.lineHeight;
        var dollHeight = 0;

        // Determine the total width of the longest line
        for (var line : text) {
            var lineWidth = font.width(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }

        // Add spacing between text and doll if there is text only!
        var spacing = text.isEmpty() ? 0 : 8;

        LivingEntityRenderState livingRenderState = null;
        Quaternionf rotation = null, xRotation = null;
        if (config.showDoll) {
            // If we are showing the doll, find out its dimensions!
            float xAngle = (float) Math.atan(-0.25F);
            float yAngle = (float) Math.atan(-0.2F);
            rotation = (new Quaternionf()).rotateZ((float) Math.PI);
            xRotation = (new Quaternionf()).rotateX(yAngle * 20.0F * ((float) Math.PI / 180F));
            rotation.mul(xRotation);

            livingRenderState = tsd$extractRenderState(player);
            livingRenderState.bodyRot = 180.0F + xAngle * 20.0F;
            livingRenderState.yRot = xAngle * 20.0F;
            if (livingRenderState.pose != Pose.FALL_FLYING) {
                livingRenderState.xRot = -yAngle * 20.0F;
            } else {
                livingRenderState.xRot = 0.0F;
            }

            livingRenderState.boundingBoxWidth /= livingRenderState.scale;
            livingRenderState.boundingBoxHeight /= livingRenderState.scale;
            livingRenderState.scale = (float) config.scale;

            // Set the width and height of the entire UI properly based on the doll's size
            var dollWidth = (int) (config.scale * 16f * livingRenderState.boundingBoxWidth);
            width = Math.max(width, dollWidth);
            dollHeight = (int) (config.scale * 16f * livingRenderState.boundingBoxHeight);
            height += spacing + dollHeight;
        }

        // Determine the final rendering position
        var size = new Vector2i((int) (graphics.guiWidth() / config.scale), (int) (graphics.guiHeight() / config.scale));
        var position = config.anchor.apply(size, config.getPosition(graphics, new Vector2i(width, height)));
        var x = position.x;
        var y = position.y;

        // Determine the top left corner to draw relative to
        var topLeftX = x;
        var topLeftY = y;
        if (rightAligned) topLeftX -= width;
        if (bottomAligned) topLeftY -= height;

        // If applicable draw the text relative to the top left corner
        if (!text.isEmpty()) {
            // If the doll goes above, we move down the text to below it!
            var lineY = topLeftY;
            if (config.showDoll && !config.dollBelow) {
                lineY += dollHeight + spacing;
            }

            graphics.nextStratum();
            for (var line : text) {
                var styledLine = line.withStyle(style);
                var lineX = topLeftX;
                if (rightAligned) {
                    // If right aligned we need to align the text with the right side still!
                    lineX += width - font.width(styledLine);
                }
                graphics.text(font, styledLine, lineX, lineY, -1);
                lineY += font.lineHeight;
            }
        }

        // If enabled show the doll showing the current state
        if (livingRenderState != null) {
            var clipSize = 256;
            var translation = new Vector3f(
                    (float) config.scale * (rightAligned ? -livingRenderState.boundingBoxWidth / 2.0F : livingRenderState.boundingBoxWidth / 2.0F),
                    (float) config.scale * (config.dollBelow ? livingRenderState.boundingBoxHeight : 0.0F),
                    0.0F
            );
            var dollX = x + (rightAligned ? -4 : 4);
            var dollY = (config.dollBelow ? topLeftY + height - dollHeight : topLeftY + dollHeight);
            var transformed = graphics.pose().transformPosition(new Vector2f(dollX, dollY));
            graphics.entity(livingRenderState, 16f, translation, rotation, xRotation, (int) transformed.x - clipSize, (int) transformed.y - clipSize, (int) transformed.x + clipSize, (int) transformed.y + clipSize);
        }
        graphics.pose().popMatrix();
    }

    @Unique
    private static LivingEntityRenderState tsd$extractRenderState(final LivingEntity entity) {
        var entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        var renderer = entityRenderDispatcher.getRenderer(entity);
        var renderState = renderer.createRenderState(entity, 1.0F);
        renderState.shadowPieces.clear();
        renderState.outlineColor = 0;
        return (LivingEntityRenderState) renderState;
    }
}

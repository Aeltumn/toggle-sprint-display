package com.aeltumn.togglesprintdisplay.config;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.Minecraft;
import org.joml.Vector2i;

/** Determines the corner to anchor to. */
public enum Anchor {
    TOP_LEFT {
        @Override
        public Vector2i apply(Vector2i size, Vector2i offset) {
            return offset;
        }
    },
    TOP_RIGHT {
        @Override
        public boolean isRightAligned() {
            return true;
        }

        @Override
        public Vector2i apply(Vector2i size, Vector2i offset) {
            return new Vector2i(size.x - offset.x, offset.y);
        }
    },
    BOTTOM_LEFT {
        @Override
        public boolean isBottomAligned() {
            return true;
        }

        @Override
        public Vector2i apply(Vector2i size, Vector2i offset) {
            return new Vector2i(offset.x, size.y - offset.y);
        }
    },
    BOTTOM_RIGHT {
        @Override
        public boolean isRightAligned() {
            return true;
        }

        @Override
        public boolean isBottomAligned() {
            return true;
        }

        @Override
        public Vector2i apply(Vector2i size, Vector2i offset) {
            return new Vector2i(size.x - offset.x, size.y - offset.y);
        }
    };

    /**
     * Whether to right-align at this anchor.
     */
    public boolean isRightAligned() {
        return false;
    }

    /**
     * Whether to bottom-align at this anchor.
     */
    public boolean isBottomAligned() {
        return false;
    }

    /**
     * Gets the final location for rendering given the input offset.
     */
    public abstract Vector2i apply(Vector2i size, Vector2i offset);
}

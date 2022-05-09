package net.thewinnt.dominoes.util;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {

    /**
     * Returns a new {@link Color} from the given RGB components.
     * @param r Red, [0-255]
     * @param g Green, [0-255]
     * @param b Blue, [0-255]
     * @return The Color from given components. If the values are out of range, they're clamped
     * in range [0-255]
     */
    public static Color rgbColor(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }

    /**
     * Returns a new {@link Color} from the given RGB components.
     * @param r Red, [0-255]
     * @param g Green, [0-255]
     * @param b Blue, [0-255]
     * @param a Alpha, [0-255]
     * @return The Color from given components. If the values are out of range, they're clamped
     * in range [0-255]
     */
    public static Color rgbColor(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}

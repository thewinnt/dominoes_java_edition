package net.thewinnt.dominoes.ui;

import com.badlogic.gdx.graphics.Color;

/** A collection of some basic utility functions */
public class Utils {
    public static Color rgbColor(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }

    public static Color rgbColor(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}

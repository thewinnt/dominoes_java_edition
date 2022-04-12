package net.thewinnt.dominoes.ui;

import com.badlogic.gdx.graphics.Color;

/** A theme for Dominoes: Java Edition. Completely compatible with PE themes. */
public class Theme {
    public Color[] colors;
    public String author;
    public String name;
    public String desc;
    public int version;

    public Theme() {
        colors = new Color[31];
        author = null;
        name = null;
        desc = null;
        version = -1;
    }

    public Theme(Color[] colors) {
        this();
        this.colors = colors;
    }

    public Theme(Color[] colors, String author, String name, String desc, int version) {
        this.colors = colors;
        this.author = author;
        this.name = name;
        this.desc = desc;
        this.version = version;
    }

    // TODO load themes from json
}
